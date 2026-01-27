#!/usr/bin/env python3
"""
Android String Resource Translator

Production-ready translation of Android string resources using Google Gemini API.

Features:
- Comment preservation (copies comments from source file exactly)
- Spacing preservation (maintains blank lines and structure from source)
- Placeholder preservation with validation (%s, %1$s, etc.)
- Markup tag preservation with validation (<b>, <xliff:g>, etc.)
- Token order validation (not just presence)
- Source attribute propagation (formatted, product, tools:*)
- Conditional placeholder handling for formatted="false" strings
- Whitespace preservation (no stripping of source text)
- HTML entity conversion (case-insensitive)
- Android special character escaping
- Proper xliff namespace handling for AAPT2 compatibility
- Batch translation with individual fallback
- Better 503/overload error handling
- Comprehensive validation and error handling

Usage:
    python translate.py --mode check --locales es,de,fr
    python translate.py --mode apply --locales es,de,fr
    python translate.py --mode apply --locales ar --model gemma-3-27b-it --batch-size 15

Environment:
    GEMINI_API_KEY=your_api_key_here
"""

from __future__ import annotations

import argparse
import copy
import json
import logging
import os
import re
import sys
import time
from dataclasses import dataclass, field
from pathlib import Path
from typing import Dict, FrozenSet, List, Optional, Set, Tuple

from lxml import etree as ET
from google import genai
from google.genai import types

# ============================================================================
# Logging Configuration
# ============================================================================

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
)
logger = logging.getLogger(__name__)

# ============================================================================
# Constants & Namespaces
# ============================================================================

XML_PARSER = ET.XMLParser(
    remove_blank_text=False,
    remove_comments=False,
    strip_cdata=False,
)

XLIFF_NAMESPACE = "urn:oasis:names:tc:xliff:document:1.2"
TOOLS_NAMESPACE = "http://schemas.android.com/tools"

ET.register_namespace("xliff", XLIFF_NAMESPACE)
ET.register_namespace("tools", TOOLS_NAMESPACE)

DEFAULT_EXCLUDE_DIRS: FrozenSet[str] = frozenset({
    ".git", ".gradle", "build", ".idea", "node_modules",
    "__pycache__", "venv", ".venv", ".svn", ".hg", "target",
    "bin", "obj", ".dart_tool", ".pub-cache",
})

PROPAGATE_ATTRIBUTES: FrozenSet[str] = frozenset({
    "formatted",
    "product",
})

ALLOWED_TAGS: FrozenSet[str] = frozenset({
    "b", "i", "u", "s", "strike", "del", "ins",
    "strong", "em", "cite", "dfn", "code", "samp", "kbd", "var",
    "big", "small", "sup", "sub", "tt",
    "a", "font", "annotation", "span",
    "xliff:g", "g",
})

# ============================================================================
# Regex Patterns
# ============================================================================

PLACEHOLDER_PATTERNS = [
    r"%%",
    r"%n",
    r"%(?:\d+\$)?[-+# 0,(]*\d*(?:\.\d+)?[sdbBhHoOxXeEfgGaAcC]",
    r"%(?:\d+\$)?[-+# 0,(]*\d*(?:\.\d+)?t[HIklMSLNpzZsQBbhAaCYyjmdeRTrDFc]",
]
PLACEHOLDER_RE = re.compile("|".join(PLACEHOLDER_PATTERNS))

XLIFF_TAG_RE = re.compile(
    r"<xliff:g[^>]*>.*?</xliff:g>|<xliff:g[^>]*/\s*>",
    re.DOTALL | re.IGNORECASE,
)

MARKUP_TAG_RE = re.compile(r"</?[a-zA-Z][^>]*>")
MARKUP_PATTERN = re.compile(r"</?[a-zA-Z][^>]*>")
TAG_NAME_PATTERN = re.compile(r"</?([a-zA-Z][a-zA-Z0-9:]*)")

BARE_AMPERSAND_PATTERN = re.compile(
    r"&(?!(amp|lt|gt|quot|apos);|#\d+;|#x[0-9a-fA-F]+;)"
)

HTML_ENTITY_PATTERN = re.compile(
    r"&(nbsp|copy|reg|trade|mdash|ndash|hellip|bull|euro|pound|yen|cent);?",
    re.IGNORECASE,
)

HTML_ENTITY_TO_NUMERIC: Dict[str, str] = {
    "nbsp": "&#160;",
    "copy": "&#169;",
    "reg": "&#174;",
    "trade": "&#8482;",
    "mdash": "&#8212;",
    "ndash": "&#8211;",
    "hellip": "&#8230;",
    "bull": "&#8226;",
    "euro": "&#8364;",
    "pound": "&#163;",
    "yen": "&#165;",
    "cent": "&#162;",
}

TOKEN_SEQUENCE_RE = re.compile(r"$$\[(?:PH|TAG)_\d+$$\]")

# ============================================================================
# Exceptions
# ============================================================================


class TranslationError(Exception):
    """Raised when translation API call fails."""
    pass


class XmlWriteError(Exception):
    """Raised when writing XML fails validation."""
    pass


class ValidationError(Exception):
    """Raised when translation validation fails."""
    pass


# ============================================================================
# Data Structures
# ============================================================================


@dataclass
class Config:
    """Application configuration."""
    repo_root: Path
    mode: str
    locales: List[str]
    model: str
    batch_size: int
    api_key: str
    exclude_dirs: FrozenSet[str] = DEFAULT_EXCLUDE_DIRS
    max_retries: int = 5
    base_retry_delay: float = 10.0
    request_delay: float = 2.0
    validate_output: bool = True
    warn_unknown_tags: bool = True


@dataclass
class StringEntry:
    """A string resource entry with text and attributes."""
    key: str
    text: str
    attributes: Dict[str, str] = field(default_factory=dict)

    @property
    def is_formatted(self) -> bool:
        return self.attributes.get("formatted", "true").lower() != "false"

    def get_propagated_attributes(self) -> Dict[str, str]:
        """Get attributes to propagate to translation (name, formatted, product, tools:*)."""
        result = {"name": self.key}
        for attr in PROPAGATE_ATTRIBUTES:
            if attr in self.attributes:
                result[attr] = self.attributes[attr]
        for key, value in self.attributes.items():
            if key.startswith(f"{{{TOOLS_NAMESPACE}}}") or key.startswith("tools:"):
                result[key] = value
        return result


@dataclass
class FrozenText:
    """Text with placeholders and markup tags replaced by tokens."""
    original: str
    frozen: str
    placeholders: List[str]
    tags: List[str]

    def unfreeze(self, translated_frozen: str) -> str:
        result = translated_frozen
        for i, ph in enumerate(self.placeholders):
            result = result.replace(f"[[PH_{i}]]", ph)
        for i, tag in enumerate(self.tags):
            result = result.replace(f"[[TAG_{i}]]", tag)
        return result

    def validate(self, translated_frozen: str) -> Tuple[bool, List[str]]:
        errors: List[str] = []
        for i, ph in enumerate(self.placeholders):
            token = f"[[PH_{i}]]"
            if token not in translated_frozen:
                errors.append(f"Missing placeholder {token} (was: {ph})")
        for i, tag in enumerate(self.tags):
            token = f"[[TAG_{i}]]"
            if token not in translated_frozen:
                tag_preview = tag[:40] + "..." if len(tag) > 40 else tag
                errors.append(f"Missing tag {token} (was: {tag_preview})")
        expected_tokens = TOKEN_SEQUENCE_RE.findall(self.frozen)
        actual_tokens = TOKEN_SEQUENCE_RE.findall(translated_frozen)
        if expected_tokens != actual_tokens and not errors:
            errors.append(f"Token order changed: expected {expected_tokens}, got {actual_tokens}")
        return len(errors) == 0, errors

    @property
    def has_tokens(self) -> bool:
        return bool(self.placeholders or self.tags)

    @property
    def token_count(self) -> int:
        return len(self.placeholders) + len(self.tags)


@dataclass
class LocaleResult:
    """Translation results for a single locale and source file."""
    locale: str
    source_path: Path
    target_path: Path
    total_source: int = 0
    already_translated: int = 0
    newly_translated: int = 0
    failed: int = 0
    errors: List[str] = field(default_factory=list)

    @property
    def missing_before(self) -> int:
        return self.total_source - self.already_translated


@dataclass
class ProcessingResult:
    """Overall processing results across all files and locales."""
    locale_results: List[LocaleResult] = field(default_factory=list)

    @property
    def total_missing_before(self) -> int:
        return sum(r.missing_before for r in self.locale_results)

    @property
    def total_translated(self) -> int:
        return sum(r.newly_translated for r in self.locale_results)

    @property
    def total_failed(self) -> int:
        return sum(r.failed for r in self.locale_results)

    @property
    def has_missing(self) -> bool:
        return (self.total_missing_before - self.total_translated) > 0

    @property
    def has_failures(self) -> bool:
        return self.total_failed > 0


# ============================================================================
# Text Freezing Functions
# ============================================================================


def freeze_text(text: str, freeze_placeholders: bool = True) -> FrozenText:
    """Replace placeholders and markup tags with tokens for safe translation."""
    frozen = text
    placeholders: List[str] = []
    tags: List[str] = []

    def freeze_xliff(match: re.Match) -> str:
        tags.append(match.group(0))
        return f"[[TAG_{len(tags) - 1}]]"

    frozen = XLIFF_TAG_RE.sub(freeze_xliff, frozen)

    def freeze_tag(match: re.Match) -> str:
        tags.append(match.group(0))
        return f"[[TAG_{len(tags) - 1}]]"

    frozen = MARKUP_TAG_RE.sub(freeze_tag, frozen)

    if freeze_placeholders:
        def freeze_ph(match: re.Match) -> str:
            placeholders.append(match.group(0))
            return f"[[PH_{len(placeholders) - 1}]]"
        frozen = PLACEHOLDER_RE.sub(freeze_ph, frozen)

    return FrozenText(original=text, frozen=frozen, placeholders=placeholders, tags=tags)


# ============================================================================
# Text Sanitization Functions
# ============================================================================


def convert_html_entities_to_numeric(text: str) -> str:
    """Convert HTML named entities to XML numeric entities."""
    def replace_entity(match: re.Match) -> str:
        name = match.group(1).lower()
        return HTML_ENTITY_TO_NUMERIC.get(name, match.group(0))
    return HTML_ENTITY_PATTERN.sub(replace_entity, text)


def fix_bare_ampersands(text: str) -> str:
    """Replace bare ampersands with &amp; for XML validity."""
    return BARE_AMPERSAND_PATTERN.sub("&amp;", text)


def sanitize_for_xml_parse(text: str) -> str:
    """Prepare text for XML parsing."""
    result = convert_html_entities_to_numeric(text)
    return fix_bare_ampersands(result)


def escape_android_string(text: str) -> str:
    """Escape Android special characters in string resources."""
    if not text:
        return text
    result: List[str] = []
    i = 0
    length = len(text)
    while i < length:
        char = text[i]
        if char == '\\' and i + 1 < length:
            next_char = text[i + 1]
            if next_char in ("'", '"', '\\', 'n', 't', 'r', '@', '?'):
                result.append(char)
                result.append(next_char)
                i += 2
                continue
            if next_char == 'u' and i + 5 <= length:
                hex_chars = text[i + 2:i + 6]
                if len(hex_chars) == 4 and all(c in '0123456789abcdefABCDEF' for c in hex_chars):
                    result.append(text[i:i + 6])
                    i += 6
                    continue
        if char == "'":
            result.append("\\'")
        elif char == '@' and i == 0:
            result.append('\\@')
        elif char == '?' and i == 0:
            result.append('\\?')
        else:
            result.append(char)
        i += 1
    return ''.join(result)


def escape_android_text_nodes(element: ET._Element) -> None:
    """Recursively escape Android special characters in text and tail content."""
    if element.text:
        element.text = escape_android_string(element.text)
    for child in element:
        if not callable(child.tag):
            escape_android_text_nodes(child)
            if child.tail:
                child.tail = escape_android_string(child.tail)


def validate_allowed_tags(value: str) -> Tuple[bool, List[str]]:
    """Check if all markup tags in value are in the allowlist."""
    if not MARKUP_PATTERN.search(value):
        return True, []
    found = set(TAG_NAME_PATTERN.findall(value))
    unknown = [t for t in found if t.lower() not in ALLOWED_TAGS]
    return len(unknown) == 0, unknown


# ============================================================================
# XML Helper Functions
# ============================================================================


def is_comment(elem) -> bool:
    """Check if element is a comment (lxml comments have callable tag)."""
    return callable(elem.tag)


def get_comment_text(elem) -> str:
    """Get the text content of a comment element."""
    if is_comment(elem):
        return elem.text or ""
    return ""


def get_element_full_text(elem: ET._Element) -> str:
    """Get full text content including child elements as markup."""
    parts: List[str] = []
    if elem.text:
        parts.append(elem.text)
    for child in elem:
        if not is_comment(child):
            parts.append(ET.tostring(child, encoding="unicode"))
        if child.tail:
            parts.append(child.tail)
    return "".join(parts)


# ============================================================================
# XML Reading Functions
# ============================================================================


def read_source_strings(source_xml: Path) -> List[StringEntry]:
    """Read translatable strings from source XML, preserving attributes."""
    tree = ET.parse(str(source_xml), parser=XML_PARSER)
    root = tree.getroot()
    entries: List[StringEntry] = []

    for node in root.iter("string"):
        name = node.get("name")
        if not name:
            continue
        if node.get("translatable", "true").lower() == "false":
            continue
        raw_text = get_element_full_text(node)
        if not raw_text or not raw_text.strip():
            continue
        preserved: Dict[str, str] = {}
        for attr_key, attr_val in node.attrib.items():
            if attr_key in ("name", "translatable"):
                continue
            if attr_key in PROPAGATE_ATTRIBUTES:
                preserved[attr_key] = attr_val
            elif attr_key.startswith(f"{{{TOOLS_NAMESPACE}}}"):
                preserved[attr_key] = attr_val
        entries.append(StringEntry(key=name, text=raw_text, attributes=preserved))

    return entries


def read_existing_keys(target_xml: Path) -> Set[str]:
    """Read existing string keys from target file."""
    if not target_xml.exists():
        return set()
    try:
        tree = ET.parse(str(target_xml), parser=XML_PARSER)
        root = tree.getroot()
        return set(root.xpath("./string/@name"))
    except ET.XMLSyntaxError:
        return set()


# ============================================================================
# XML Writing Functions
# ============================================================================


def set_mixed_string_value(
    node: ET._Element,
    value: str,
    key: Optional[str] = None,
    warn_unknown_tags: bool = True,
) -> None:
    """Set string node value, preserving embedded markup."""
    node.text = None
    for child in list(node):
        node.remove(child)

    key_prefix = f"[{key}] " if key else ""

    if warn_unknown_tags and MARKUP_PATTERN.search(value):
        is_valid, unknown = validate_allowed_tags(value)
        if not is_valid:
            logger.warning(f"{key_prefix}Unknown tags (may not render): {unknown}")

    if not MARKUP_PATTERN.search(value):
        converted = convert_html_entities_to_numeric(value)
        node.text = escape_android_string(converted)
        return

    sanitized = sanitize_for_xml_parse(value)
    wrapped = f"<_root xmlns:xliff='{XLIFF_NAMESPACE}'>{sanitized}</_root>"

    try:
        fragment = ET.fromstring(wrapped.encode('utf-8'))
    except ET.XMLSyntaxError as e:
        logger.warning(f"{key_prefix}XML parse failed, using plain text: {e}")
        fallback = convert_html_entities_to_numeric(value)
        node.text = escape_android_string(fallback)
        return

    node.text = fragment.text
    for child in list(fragment):
        fragment.remove(child)
        node.append(child)

    escape_android_text_nodes(node)


def write_translations(
    target_xml: Path,
    translations: Dict[str, str],
    source_entries: List[StringEntry],
    source_xml: Path,
    validate: bool = True,
    warn_unknown_tags: bool = True,
) -> int:
    """
    Write translations to target XML, preserving EXACT source structure.

    For NEW files: Deep copies source, replaces content, removes untranslated strings
    For EXISTING files: Merges new translations while preserving structure
    """
    target_xml.parent.mkdir(parents=True, exist_ok=True)

    # Parse source with all whitespace and comments preserved
    source_tree = ET.parse(str(source_xml), parser=XML_PARSER)
    source_root = source_tree.getroot()

    # Check if target already exists
    if target_xml.exists():
        try:
            existing_tree = ET.parse(str(target_xml), parser=XML_PARSER)
            existing_root = existing_tree.getroot()
            existing_keys = set(existing_root.xpath("./string/@name"))

            return _merge_into_existing(
                target_xml, existing_root, translations, source_entries,
                source_root, existing_keys, validate, warn_unknown_tags
            )
        except ET.XMLSyntaxError as e:
            logger.warning(f"Corrupted '{target_xml}', recreating: {e}")

    # Create new file from source structure
    return _create_from_source(
        target_xml, translations, source_entries,
        source_root, validate, warn_unknown_tags
    )


def _create_from_source(
    target_xml: Path,
    translations: Dict[str, str],
    source_entries: List[StringEntry],
    source_root: ET._Element,
    validate: bool,
    warn_unknown_tags: bool,
) -> int:
    """
    Create new translation file by deep copying source and replacing text.
    Preserves all comments, whitespace, and exact ordering.
    """
    # Deep copy preserves everything
    root = copy.deepcopy(source_root)

    # Build set of keys that have translations
    translated_keys: Set[str] = set(translations.keys())

    # Build sections: list of (comment_elements, string_elements)
    # Each section starts with zero or more comments followed by strings
    sections: List[Tuple[List[ET._Element], List[ET._Element]]] = []
    current_comments: List[ET._Element] = []
    current_strings: List[ET._Element] = []

    for elem in list(root):
        if is_comment(elem):
            if current_strings:
                # Save previous section and start new one
                sections.append((current_comments, current_strings))
                current_comments = []
                current_strings = []
            current_comments.append(elem)
        elif elem.tag == "string":
            current_strings.append(elem)


    if current_comments or current_strings:
        sections.append((current_comments, current_strings))

    written = 0
    elements_to_remove: List[ET._Element] = []

    # Process each section
    for comments, strings in sections:
        # Check if this section has any translated strings
        section_has_translation = False
        for string_elem in strings:
            name = string_elem.get("name")
            if name and name in translated_keys:
                section_has_translation = True
                break
            # Non-translatable strings don't count
            if string_elem.get("translatable", "true").lower() == "false":
                section_has_translation = True  # Keep non-translatable
                break

        if not section_has_translation:
            # Remove entire section (comments + strings)
            elements_to_remove.extend(comments)
            elements_to_remove.extend(strings)
            continue

        # Process strings in this section
        for string_elem in strings:
            name = string_elem.get("name")

            if not name:
                elements_to_remove.append(string_elem)
                continue

            # Keep non-translatable strings unchanged
            if string_elem.get("translatable", "true").lower() == "false":
                continue

            if name in translations:
                # Update with translation
                value = translations[name]

                # Clear content
                string_elem.text = None
                for child in list(string_elem):
                    string_elem.remove(child)

                set_mixed_string_value(string_elem, value, key=name, warn_unknown_tags=warn_unknown_tags)
                written += 1
            else:
                # No translation - remove this string
                elements_to_remove.append(string_elem)

    # Remove elements while preserving whitespace
    for elem in elements_to_remove:
        _remove_element_preserve_whitespace(root, elem)

    # Clean up redundant namespace declarations
    ET.cleanup_namespaces(root)

    # Write file
    tree = ET.ElementTree(root)
    tree.write(
        str(target_xml),
        encoding="utf-8",
        xml_declaration=True,
        pretty_print=False,
    )

    # Post-process to fix any xliff namespace prefix issues (ns0, ns1 -> xliff)
    _fix_xliff_namespaces_in_file(target_xml)

    if validate:
        try:
            ET.parse(str(target_xml), parser=XML_PARSER)
        except ET.XMLSyntaxError as e:
            raise XmlWriteError(f"Written file is malformed: {target_xml}: {e}")

    return written


def _fix_xliff_namespaces_in_file(target_xml: Path) -> None:
    """
    Post-process the written XML file to fix xliff namespace issues.

    lxml may generate auto-prefixed namespaces (ns0, ns1, etc.) instead of
    using the proper 'xliff' prefix. This function:
    - Replaces ns#: prefixes with xliff: for XLIFF namespace
    - Removes inline xmlns:ns# declarations for XLIFF
    - Ensures xliff namespace is declared at root level
    """
    content = target_xml.read_text(encoding='utf-8')
    original_content = content

    import re

    # Find all ns# prefixes that might be used for xliff
    ns_pattern = re.compile(r'xmlns:(ns\d+)="urn:oasis:names:tc:xliff:document:1\.2"')
    ns_matches = ns_pattern.findall(content)

    for ns_prefix in set(ns_matches):
        # Replace the prefix in tags
        content = content.replace(f'<{ns_prefix}:', '<xliff:')
        content = content.replace(f'</{ns_prefix}:', '</xliff:')
        # Remove inline namespace declarations
        content = re.sub(
            rf'\s*xmlns:{ns_prefix}="urn:oasis:names:tc:xliff:document:1\.2"',
            '',
            content
        )

    # Ensure xliff namespace is declared at root if xliff: tags are present
    if 'xliff:' in content and 'xmlns:xliff=' not in content:
        # Add xliff namespace declaration to the resources tag
        content = content.replace(
            '<resources',
            '<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2"',
            1
        )

    # Only rewrite if changes were made
    if content != original_content:
        target_xml.write_text(content, encoding='utf-8')



def _remove_element_preserve_whitespace(root: ET._Element, elem: ET._Element) -> None:
    """Remove element while preserving surrounding whitespace structure."""
    parent = elem.getparent()
    if parent is None:
        return

    prev = elem.getprevious()

    # Transfer tail to previous sibling or parent.text
    if elem.tail:
        if prev is not None:
            prev.tail = (prev.tail or "") + elem.tail
        else:
            parent.text = (parent.text or "") + elem.tail

    parent.remove(elem)


def _merge_into_existing(
    target_xml: Path,
    existing_root: ET._Element,
    translations: Dict[str, str],
    source_entries: List[StringEntry],
    source_root: ET._Element,
    existing_keys: Set[str],
    validate: bool,
    warn_unknown_tags: bool,
) -> int:
    """Merge new translations into existing file, preserving source order and comments."""

    # Build source structure: sections with their comments and strings
    source_sections: List[Tuple[List[str], List[str]]] = []  # (comment_texts, string_names)
    current_comments: List[str] = []
    current_strings: List[str] = []

    # Also track string -> section mapping and string -> preceding whitespace
    string_to_section: Dict[str, int] = {}
    string_tail: Dict[str, str] = {}
    comment_tails: Dict[int, str] = {}  # section_index -> tail after last comment

    for elem in source_root:
        if is_comment(elem):
            if current_strings:
                source_sections.append((current_comments, current_strings))
                current_comments = []
                current_strings = []
            current_comments.append(elem.text or "")
            if elem.tail:
                comment_tails[len(source_sections)] = elem.tail
        elif elem.tag == "string":
            name = elem.get("name")
            if name:
                current_strings.append(name)
                string_to_section[name] = len(source_sections)
                string_tail[name] = elem.tail or "\n    "

    if current_comments or current_strings:
        source_sections.append((current_comments, current_strings))

    # Build flat source order
    source_order: List[str] = []
    for comments, strings in source_sections:
        source_order.extend(strings)

    # Get existing elements map
    existing_elems: Dict[str, ET._Element] = {}
    for elem in existing_root:
        if is_comment(elem):
            continue
        if elem.tag == "string":
            name = elem.get("name")
            if name:
                existing_elems[name] = elem

    entry_map = {e.key: e for e in source_entries}
    written = 0

    # Track which sections we've added comments for
    added_section_comments: Set[int] = set()

    # Add new translations in source order
    for key in source_order:
        if key in existing_keys:
            continue
        if key not in translations:
            continue

        entry = entry_map.get(key)
        if not entry:
            continue

        value = translations[key]
        section_idx = string_to_section.get(key, 0)

        # Find insertion point
        key_idx = source_order.index(key)
        insert_before: Optional[ET._Element] = None
        for next_key in source_order[key_idx + 1:]:
            if next_key in existing_elems:
                insert_before = existing_elems[next_key]
                break

        # Add section comments if not already added
        if section_idx not in added_section_comments:
            comments, _ = source_sections[section_idx]
            if comments:
                for comment_text in comments:
                    comment = ET.Comment(comment_text)
                    comment.tail = "\n    "

                    if insert_before is not None:
                        # Add blank line before section
                        prev = insert_before.getprevious()
                        if prev is not None and not is_comment(prev):
                            prev.tail = "\n\n    "
                        insert_before.addprevious(comment)
                    else:
                        # Append at end
                        children = list(existing_root)
                        if children:
                            last = children[-1]
                            if not is_comment(last):
                                last.tail = "\n\n    "
                        existing_root.append(comment)

                added_section_comments.add(section_idx)

        # Create string element
        attrs = entry.get_propagated_attributes()
        node = ET.Element("string", **attrs)

        set_mixed_string_value(node, value, key=key, warn_unknown_tags=warn_unknown_tags)

        # Set tail from source
        node.tail = string_tail.get(key, "\n    ")

        # Insert element
        if insert_before is not None:
            insert_before.addprevious(node)
        else:
            existing_root.append(node)

        existing_keys.add(key)
        existing_elems[key] = node
        written += 1

    # Fix final element tail
    children = list(existing_root)
    if children:
        for child in reversed(children):
            if not is_comment(child):
                if not child.tail or not child.tail.endswith("\n"):
                    child.tail = "\n"
                break

    if written > 0:
        # Clean up redundant namespace declarations
        ET.cleanup_namespaces(existing_root)

        tree = ET.ElementTree(existing_root)
        tree.write(
            str(target_xml),
            encoding="utf-8",
            xml_declaration=True,
            pretty_print=False,
        )

        # Post-process to fix any xliff namespace prefix issues (ns0, ns1 -> xliff)
        _fix_xliff_namespaces_in_file(target_xml)

        if validate:
            try:
                ET.parse(str(target_xml), parser=XML_PARSER)
            except ET.XMLSyntaxError as e:
                raise XmlWriteError(f"Written file is malformed: {target_xml}: {e}")

    return written


# ============================================================================
# File Discovery
# ============================================================================


def find_source_files(repo_root: Path, exclude_dirs: FrozenSet[str]) -> List[Path]:
    """Find all source strings.xml files in the repository."""
    paths: List[Path] = []
    patterns = [
        "src/*/res/values/strings.xml",
        "src/*/composeResources/values/strings.xml",
    ]
    for pat in patterns:
        for p in repo_root.rglob(pat):
            if any(part in exclude_dirs for part in p.parts):
                continue
            if "src" in p.parts:
                src_set = p.parts[p.parts.index("src") + 1]
                if src_set in ("test", "androidTest"):
                    continue
            paths.append(p)
    return sorted(set(paths))


def get_target_path(source_xml: Path, locale: str) -> Path:
    """Get target path for a locale based on source path."""
    if '/' in locale or '\\' in locale or '..' in locale:
        raise ValueError(f"Invalid locale: {locale}")
    values_dir = source_xml.parent
    parent = values_dir.parent
    return parent / f"values-{locale}" / "strings.xml"


def get_module_name(source_path: Path) -> str:
    """Extract module name from source strings.xml path."""
    parts = source_path.parts
    src_indexes = [i for i, p in enumerate(parts) if p == "src"]
    if src_indexes:
        first_src_index = src_indexes[0]
        if first_src_index > 0:
            return parts[first_src_index - 1]
    if len(source_path.parents) > 4:
        return source_path.parents[4].name
    return "unknown"


# ============================================================================
# Translation API
# ============================================================================


class GeminiTranslator:
    """Translator using Google Gemini API with retry and rate limiting."""

    SYSTEM_PROMPT = """You are a professional translator for a mobile finance/ banking Android app.
Translate UI strings accurately and naturally for the target locale.

CRITICAL RULES — FOLLOW EXACTLY:

1. OUTPUT FORMAT:
   - Return ONLY a valid JSON object mapping keys to translated strings.
   - Do NOT include markdown, code blocks, backticks, or any commentary.

2. PLACEHOLDER TOKENS (e.g., [[PH_0]], [[PH_1]]):
   - Preserve ALL placeholder tokens EXACTLY as written.
   - Do NOT translate, modify, reorder, or remove any [[PH_N]] tokens.

3. TAG TOKENS (e.g., [[TAG_0]], [[TAG_1]]):
   - Preserve ALL tag tokens EXACTLY as written.
   - Keep tokens in the SAME ORDER as the original.

4. KEYS:
   - Every input key must appear exactly once in the output.

5. WHITESPACE:
   - Preserve leading and trailing spaces if present in the original.

6. SPECIAL CHARACTERS:
   - Preserve newline characters (\\n) exactly as they appear.
   - Do not add or remove any escape sequences.

Example input:
{"items": [{"key": "greeting", "text": "Hello [[PH_0]]!"}]}

Example output:
{"greeting": "مرحبا [[PH_0]]!"}"""

    def __init__(self, config: Config):
        self.config = config
        self.client = genai.Client(api_key=config.api_key)
        self._last_request_time = 0.0

    def _rate_limit(self) -> None:
        """Enforce minimum delay between API requests."""
        elapsed = time.time() - self._last_request_time
        if elapsed < self.config.request_delay:
            time.sleep(self.config.request_delay - elapsed)
        self._last_request_time = time.time()

    def translate_batch(
        self,
        locale: str,
        items: List[Tuple[str, FrozenText]],
    ) -> Dict[str, str]:
        """Translate a batch of frozen texts with retry logic."""
        self._rate_limit()

        payload = {
            "target_locale": locale,
            "items": [{"key": k, "text": ft.frozen} for k, ft in items],
        }

        model_lower = self.config.model.lower()
        supports_system_instruction = not any(
            x in model_lower for x in ["gemma", "embedding", "aqa"]
        )

        if supports_system_instruction:
            user_prompt = (
                f"Translate all items from English to '{locale}'.\n"
                f"Return a JSON object mapping each key to its translation.\n"
                f"Preserve all [[PH_N]] and [[TAG_N]] tokens in the SAME ORDER.\n\n"
                f"Input:\n{json.dumps(payload, ensure_ascii=False, indent=2)}"
            )
            gen_config = types.GenerateContentConfig(
                system_instruction=self.SYSTEM_PROMPT,
                temperature=0.1,
                response_mime_type="application/json",
                max_output_tokens=4096,
            )
        else:
            user_prompt = f"""{self.SYSTEM_PROMPT}

---

Now translate all items from English to '{locale}'.
Return a JSON object mapping each key to its translation.

Input:
{json.dumps(payload, ensure_ascii=False, indent=2)}

Output (JSON only, no markdown):"""

            gen_config = types.GenerateContentConfig(
                temperature=0.1,
                max_output_tokens=4096,
            )

        last_error: Optional[Exception] = None
        requested_keys = {k for k, _ in items}

        for attempt in range(1, self.config.max_retries + 1):
            try:
                response = self.client.models.generate_content(
                    model=self.config.model,
                    contents=user_prompt,
                    config=gen_config,
                )

                text = (response.text or "").strip()

                if text.startswith("```"):
                    lines = text.split("\n")
                    if lines[0].startswith("```"):
                        lines = lines[1:]
                    if lines and lines[-1].strip() == "```":
                        lines = lines[:-1]
                    text = "\n".join(lines).strip()

                data = json.loads(text)

                if not isinstance(data, dict):
                    raise TranslationError("Response is not a JSON object")

                returned_keys = set(data.keys())
                missing_keys = requested_keys - returned_keys

                if missing_keys:
                    logger.warning(f"API omitted {len(missing_keys)} key(s)")

                return {str(k): str(v) for k, v in data.items() if k in requested_keys}

            except json.JSONDecodeError as e:
                last_error = TranslationError(f"Invalid JSON: {e}")
            except Exception as e:
                last_error = e
                error_str = str(e).lower()

                is_rate_limited = "429" in str(e) or "resource_exhausted" in error_str
                is_overloaded = (
                    "503" in str(e) or
                    "unavailable" in error_str or
                    "overloaded" in error_str
                )

                if is_rate_limited or is_overloaded:
                    match = re.search(r"retry in (\d+(?:\.\d+)?)", str(e), re.IGNORECASE)

                    if match:
                        delay = float(match.group(1)) + 5
                    elif is_overloaded:
                        delay = 30.0 * (1.5 ** (attempt - 1))
                        delay = min(delay, 180.0)
                    else:
                        delay = 60.0

                    error_type = "Rate limited" if is_rate_limited else "Model overloaded"
                    logger.warning(
                        f"{error_type}! Waiting {delay:.0f}s before retry "
                        f"{attempt}/{self.config.max_retries}..."
                    )
                    time.sleep(delay)
                    continue

            delay = self.config.base_retry_delay * (2 ** (attempt - 1))
            logger.warning(
                f"Attempt {attempt}/{self.config.max_retries} failed: {last_error}. "
                f"Retrying in {delay:.1f}s..."
            )
            time.sleep(delay)

        raise TranslationError(
            f"Failed after {self.config.max_retries} attempts: {last_error}"
        )

    def translate_single(
        self,
        locale: str,
        key: str,
        frozen_text: FrozenText,
    ) -> Optional[str]:
        """Translate a single string. Returns None on failure."""
        try:
            result = self.translate_batch(locale, [(key, frozen_text)])
            return result.get(key)
        except TranslationError as e:
            logger.error(f"Failed to translate '{key}': {e}")
            return None


# ============================================================================
# Main Processing Logic
# ============================================================================


def create_batches(
    items: List[Tuple[str, FrozenText]],
    batch_size: int,
) -> List[List[Tuple[str, FrozenText]]]:
    """Split items into batches of specified size."""
    return [items[i:i + batch_size] for i in range(0, len(items), batch_size)]


def process_locale(
    source_xml: Path,
    locale: str,
    config: Config,
    translator: Optional[GeminiTranslator],
) -> LocaleResult:
    """Process translations for a single source file and locale."""
    target_xml = get_target_path(source_xml, locale)
    result = LocaleResult(locale=locale, source_path=source_xml, target_path=target_xml)

    source_entries = read_source_strings(source_xml)

    result.total_source = len(source_entries)

    if not source_entries:
        logger.warning(f"No translatable strings in {source_xml}")
        return result

    existing_keys = read_existing_keys(target_xml)
    result.already_translated = len(existing_keys & {e.key for e in source_entries})

    missing_entries = [e for e in source_entries if e.key not in existing_keys]

    if not missing_entries:
        logger.info(f"  [{locale}] All {result.total_source} strings already translated")
        return result

    logger.info(
        f"  [{locale}] {len(missing_entries)} of {result.total_source} "
        f"strings need translation"
    )

    if config.mode == "check" or translator is None:
        return result

    frozen_map: Dict[str, FrozenText] = {}
    for entry in missing_entries:
        frozen_map[entry.key] = freeze_text(
            entry.text,
            freeze_placeholders=entry.is_formatted
        )

    translations: Dict[str, str] = {}
    items = [(e.key, frozen_map[e.key]) for e in missing_entries]
    batches = create_batches(items, config.batch_size)

    for batch_idx, batch in enumerate(batches, 1):
        logger.info(f"    Batch {batch_idx}/{len(batches)} ({len(batch)} strings)")

        try:
            batch_result = translator.translate_batch(locale, batch)
            logger.debug(f"    Batch {batch_idx} completed successfully")
        except TranslationError as e:
            logger.error(f"    Batch {batch_idx} failed: {e}")
            result.errors.append(f"Batch {batch_idx} failed: {e}")
            batch_result = {}

        for key, frozen_text in batch:
            translated_frozen = batch_result.get(key)

            if translated_frozen is None:
                logger.warning(f"    [{key}] Missing from batch, retrying individually")
                translated_frozen = translator.translate_single(locale, key, frozen_text)

            if translated_frozen is None:
                logger.error(f"    [{key}] Translation failed")
                result.failed += 1
                result.errors.append(f"Translation failed for '{key}'")
                continue

            is_valid, errors = frozen_text.validate(translated_frozen)

            if not is_valid:
                logger.warning(f"    [{key}] Validation failed: {errors}")
                retry_result = translator.translate_single(locale, key, frozen_text)
                if retry_result:
                    is_valid, errors = frozen_text.validate(retry_result)
                    if is_valid:
                        translated_frozen = retry_result
                        logger.info(f"    [{key}] Retry succeeded")

            if is_valid:
                translations[key] = frozen_text.unfreeze(translated_frozen)
            else:
                logger.error(f"    [{key}] Skipped - validation failed: {errors}")
                result.failed += 1
                result.errors.append(f"Validation failed for '{key}': {errors}")

    if translations:
        try:
            written = write_translations(
                target_xml=target_xml,
                translations=translations,
                source_entries=missing_entries,
                source_xml=source_xml,
                validate=config.validate_output,
                warn_unknown_tags=config.warn_unknown_tags,
            )
            result.newly_translated = written
            logger.info(f"  [{locale}] Wrote {written} translations")
        except XmlWriteError as e:
            logger.error(f"  [{locale}] Write failed: {e}")
            result.errors.append(str(e))

    return result


def process_all(config: Config) -> ProcessingResult:
    """Process all source files for all configured locales."""
    result = ProcessingResult()
    sources = find_source_files(config.repo_root, config.exclude_dirs)

    if not sources:
        logger.error("No source strings.xml files found")
        return result

    logger.info(f"Found {len(sources)} source file(s)")

    translator: Optional[GeminiTranslator] = None
    if config.mode == "apply":
        translator = GeminiTranslator(config)

    for source_xml in sources:
        logger.info(f"\nProcessing: {source_xml}")
        for locale in config.locales:
            locale_result = process_locale(source_xml, locale, config, translator)
            result.locale_results.append(locale_result)

    return result


# ============================================================================
# Summary & CLI
# ============================================================================


def print_summary(result: ProcessingResult, mode: str) -> None:
    """Print formatted processing summary."""
    print("\n" + "=" * 70)
    print("SUMMARY")
    print("=" * 70)

    if not result.locale_results:
        print("\n  No results to display.")
        print("=" * 70)
        return

    by_source: Dict[Path, List[LocaleResult]] = {}
    for lr in result.locale_results:
        by_source.setdefault(lr.source_path, []).append(lr)

    for source_path, locale_results in sorted(by_source.items()):
        module_name = get_module_name(source_path)
        print(f"\n  {module_name}/")

        for lr in sorted(locale_results, key=lambda x: x.locale):
            remaining = lr.missing_before - lr.newly_translated
            if remaining == 0 and lr.failed == 0:
                status = "✓"
            elif lr.failed > 0:
                status = "✗"
            elif lr.newly_translated > 0:
                status = "◐"
            else:
                status = "○"

            translated_total = lr.already_translated + lr.newly_translated
            print(
                f"    [{lr.locale:5}] "
                f"{translated_total:3}/{lr.total_source:3} translated "
                f"(+{lr.newly_translated:2} new, {lr.failed:2} failed) {status}"
            )

    print("\n" + "-" * 70)
    print(f"  Missing (before): {result.total_missing_before:5}")
    print(f"  Translated:       {result.total_translated:5}")
    print(f"  Failed:           {result.total_failed:5}")
    print("=" * 70)


def parse_args() -> argparse.Namespace:
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(
        description="Translate Android string resources using Gemini API",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  %(prog)s --mode check --locales es,de,fr
  %(prog)s --mode apply --locales es,de,fr
  %(prog)s --mode apply --locales ar --model gemma-3-27b-it --batch-size 10
  %(prog)s --mode apply --locales es --request-delay 4.0 --batch-size 10
   python translate.py --repo-root ./feature/transfer-process --mode apply --locales es,de,ar --model gemma-3-27b-it --batch-size 15

Environment Variables:
  GEMINI_API_KEY    API key for Google Gemini (required for apply mode)

Exit Codes:
  0 - Success
  1 - Failure (some translations failed)
  2 - Missing (check mode: missing translations detected)
""",
    )

    parser.add_argument("--repo-root", type=Path, default=Path("."))
    parser.add_argument("--mode", choices=["check", "apply"], required=True)
    parser.add_argument("--locales", default="es,de")
    parser.add_argument("--model", default="gemini-2.0-flash")
    parser.add_argument("--batch-size", type=int, default=20)
    parser.add_argument("--api-key-env", default="GEMINI_API_KEY")
    parser.add_argument("--no-validate", action="store_true")
    parser.add_argument("--verbose", "-v", action="store_true")
    parser.add_argument("--request-delay", type=float, default=2.0)

    return parser.parse_args()


def main() -> int:
    """Main entry point."""
    args = parse_args()

    if args.verbose:
        logging.getLogger().setLevel(logging.DEBUG)

    locales = [loc.strip() for loc in args.locales.split(",") if loc.strip()]
    if not locales:
        logger.error("No locales specified")
        return 1

    api_key = ""
    if args.mode == "apply":
        api_key = os.environ.get(args.api_key_env, "").strip()
        if not api_key:
            logger.error(f"Set {args.api_key_env} environment variable")
            return 1

    request_delay = args.request_delay
    batch_size = args.batch_size
    if "gemma" in args.model.lower():
        if request_delay < 4.0:
            request_delay = 4.0
            logger.info(f"Adjusted request_delay to {request_delay}s for Gemma model")
        if batch_size > 15:
            batch_size = 15
            logger.info(f"Adjusted batch_size to {batch_size} for Gemma model")

    config = Config(
        repo_root=args.repo_root.resolve(),
        mode=args.mode,
        locales=locales,
        model=args.model,
        batch_size=batch_size,
        api_key=api_key,
        validate_output=not args.no_validate,
        request_delay=request_delay,
    )

    logger.info(f"Mode: {config.mode}")
    logger.info(f"Locales: {', '.join(config.locales)}")
    logger.info(f"Repository: {config.repo_root}")
    if config.mode == "apply":
        logger.info(f"Model: {config.model}")
        logger.info(f"Batch size: {config.batch_size}")
        logger.info(f"Request delay: {config.request_delay}s")

    result = process_all(config)
    print_summary(result, config.mode)

    if config.mode == "check":
        if result.total_missing_before > 0:
            logger.warning(f"\n⚠ Missing translations: {result.total_missing_before} string(s)")
            return 2
        logger.info("\n✓ All translations present.")
        return 0

    if result.has_failures:
        logger.warning(f"\n⚠ {result.total_failed} translation(s) failed.")
        return 1

    if result.total_translated > 0:
        logger.info(f"\n✓ Successfully translated {result.total_translated} string(s).")
        logger.info("  Validate with: ./gradlew assembleDebug")
    else:
        logger.info("\n✓ No new translations needed.")

    return 0


if __name__ == "__main__":
    sys.exit(main())
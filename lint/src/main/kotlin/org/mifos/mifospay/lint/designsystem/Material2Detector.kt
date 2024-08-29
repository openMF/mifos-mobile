/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */
package org.mifos.mifospay.lint.designsystem

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.StringOption
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.isKotlin
import com.intellij.psi.PsiNamedElement
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.mifos.mifospay.lint.config.Priorities
import org.mifos.mifospay.lint.util.OptionLoadingDetector
import org.mifos.mifospay.lint.util.StringSetLintOption
import org.mifos.mifospay.lint.util.sourceImplementation

/**
 * Checks and reports any usage of Material2 for compose. Material3 should be used everywhere instead.
 */
@Suppress("ReturnCount")
internal class Material2Detector @JvmOverloads constructor(
    private val allowList: StringSetLintOption = StringSetLintOption(ALLOW_LIST),
) : OptionLoadingDetector(allowList), SourceCodeScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf<Class<out UElement>>(
        UCallExpression::class.java,
        UQualifiedReferenceExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        // Only applicable to Kotlin files
        val language = context.uastFile?.lang ?: return null
        if (!isKotlin(language)) return null
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) = checkNode(node)

            override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) =
                checkNode(node)

            private fun checkNode(node: UResolvable) {
                val resolved = node.resolve() ?: return
                val packageName = context.evaluator.getPackage(resolved)?.qualifiedName ?: return
                if (packageName == M2_PACKAGE) {
                    // https://github.com/slackhq/compose-lints/issues/167
                    // https://issuetracker.google.com/issues/297544175
                    val resolvedName = (resolved as? PsiNamedElement)?.name
                    val unmanagedResolvedName = resolvedName?.substringBefore("-")
                    if (unmanagedResolvedName in allowList.value) {
                        // Ignore any in the allow-list.
                        return
                    }
                    context.report(
                        issue = ISSUE,
                        location = context.getLocation(node),
                        message = ISSUE.getExplanation(TextFormat.TEXT),
                    )
                }
            }
        }
    }

    companion object {
        private const val M2_PACKAGE = "androidx.compose.material"
        private const val MATERIAL2_DETECTOR_ISSUE_ID = "ComposeM2Api"

        internal val ALLOW_LIST = StringOption(
            "allowed-m2-apis",
            "A comma-separated list of APIs in androidx.compose.material " +
                "that should be allowed.",
            null,
            "This property should define a comma-separated list " +
                "of APIs in androidx.compose.material that should be allowed",
        )

        val ISSUE = Issue.create(
            id = MATERIAL2_DETECTOR_ISSUE_ID,
            briefDescription = "Using a Compose M2 API is not recommended",
            explanation = "Compose Material 2 (M2) is succeeded by Material 3 (M3)." +
                " Please use M3 APIs. " +
                "See https://slackhq.github.io/compose-lints/rules/#use-material-3 " +
                "for more information.",
            category = Category.CORRECTNESS,
            priority = Priorities.NORMAL,
            severity = Severity.ERROR,
            implementation = sourceImplementation<Material2Detector>(),
        )
            .setOptions(listOf(ALLOW_LIST))
            .setEnabledByDefault(true)
    }
}

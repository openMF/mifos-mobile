/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIView
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKNavigationTypeLinkActivated
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@Composable
actual fun MyWebView(
    htmlContent: String,
    isLoading: (isLoading: Boolean) -> Unit,
    onUrlClicked: (url: String) -> Unit,
    modifier: Modifier,
) {
    val content = remember { htmlContent }
    val webView = remember { WKWebView() }

    // Define the WKNavigationDelegate
    val navigationDelegate = rememberWebViewDelegate(onUrlClicked)

    // Set WKNavigationDelegate to the webView
    webView.navigationDelegate = navigationDelegate
    UIKitView(
        modifier = modifier.padding(top = 12.dp),
        factory = {
            val container = UIView()
            container.addSubview(webView)

            // Enable Auto Layout
            webView.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activateConstraints(
                listOf(
                    webView.topAnchor.constraintEqualToAnchor(container.topAnchor),
                    webView.bottomAnchor.constraintEqualToAnchor(container.bottomAnchor),
                    webView.leadingAnchor.constraintEqualToAnchor(container.leadingAnchor),
                    webView.trailingAnchor.constraintEqualToAnchor(container.trailingAnchor),
                ),
            )
            webView.apply {
                WKWebViewConfiguration().apply {
                    allowsInlineMediaPlayback = true
                    allowsAirPlayForMediaPlayback = true
                    allowsPictureInPictureMediaPlayback = true
                }
                loadHTMLString(content, baseURL = null)
            }
            container
        },
    )
}

@Composable
private fun rememberWebViewDelegate(onUrlClicked: (String) -> Unit): WKNavigationDelegateProtocol {
    return object : NSObject(), WKNavigationDelegateProtocol {
        override fun webView(
            webView: WKWebView,
            decidePolicyForNavigationAction: WKNavigationAction,
            decisionHandler: (WKNavigationActionPolicy) -> Unit,
        ) {
            val navigationType = decidePolicyForNavigationAction.navigationType
            val request = decidePolicyForNavigationAction.request

            when (navigationType) {
                WKNavigationTypeLinkActivated -> {
                    // Handle link clicks
                    if (decidePolicyForNavigationAction.targetFrame == null) {
                        // Load the link in the same WKWebView
                        webView.loadRequest(request)
                    }
                    onUrlClicked(request.URL?.absoluteString ?: "")
                    println(request.URL?.absoluteString ?: "")
                    decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
                }

                else -> decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
            }
        }
    }
}

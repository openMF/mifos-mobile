package org.mifos.mobilebanking.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
public class PrivacyPolicyActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String showOrHideWebViewInitialUse = "show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.privacy_policy));
        showBackButton();

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // REMOTE RESOURCE
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.loadUrl(getString(R.string.privacy_policy_host_url));
        webView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost()
                    .endsWith(getString(R.string.privacy_policy_host))) {
                return false;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // only make it invisible the FIRST time the app is run
            if (showOrHideWebViewInitialUse.equals("show")) {
                webView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            showOrHideWebViewInitialUse = "hide";
            progressBar.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }
}

package cs4330.cs.utep.pricewatcher.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cs4330.cs.utep.pricewatcher.R;

public class BrowserActivity extends AppCompatActivity {

    /**
     * Handles the intent of when the user wants to open the browser to look at the current product.
     *
     * @param savedInstanceState
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView webView = findViewById(R.id.browserAndroid);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}

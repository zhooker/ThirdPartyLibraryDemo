package com.example.thirdparty.hybrid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.R;
import com.example.thirdparty.hybrid.jsbridge.RainbowBridge;
import com.example.thirdparty.hybrid.jsbridge.core.JsBridgeWebChromeClient;

public class JSBridgeActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsbridge);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        RainbowBridge.getInstance()
                .clazz(JsInvokeJavaScope.class)
                .inject();
        webView.setWebChromeClient(new JsBridgeWebChromeClient());
        webView.loadUrl("file:///android_asset/hybrid_jsbridge.html");
    }
}

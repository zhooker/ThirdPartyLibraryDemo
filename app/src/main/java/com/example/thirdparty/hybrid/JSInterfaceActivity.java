package com.example.thirdparty.hybrid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.MainActivity;
import com.example.thirdparty.R;

public class JSInterfaceActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsinterface);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterface(), "control");
        webView.loadUrl("file:///android_asset/hybrid_jsinterface.html");
    }

    public class JsInterface {

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(JSInterfaceActivity.this, toast, Toast.LENGTH_SHORT).show();
            log("show toast success");
        }

        public void log(final String msg) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript: log(" + "'" + msg + "'" + ")");
                }
            });
        }
    }
}

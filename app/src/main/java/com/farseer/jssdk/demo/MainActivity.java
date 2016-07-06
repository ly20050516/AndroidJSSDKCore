package com.farseer.jssdk.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.farseer.jssdk.JSSDK;
import com.farseer.jssdk.JSWebView;

public class MainActivity extends AppCompatActivity {

    JSSDK jssdk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSWebView jsWebView = (JSWebView) findViewById(R.id.jsWebView);

        jssdk = new JSSDK(this, jsWebView);
        jssdk.register();

//        String html = AssetsTool.getText(this, moduleName + ".html");
//        jssdk.loadData(html, "text/html", "utf-8");
        jssdk.loadUrl("file:///android_asset/common.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jssdk.unregister();
    }
}

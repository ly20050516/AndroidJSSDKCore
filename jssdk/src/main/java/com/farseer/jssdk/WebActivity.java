/*
 *    Copyright 2016 ifarseer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.farseer.jssdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WebActivity extends Activity {

    public final static String EXTRA_URL = "extra_url";
    JSSDK jssdk = null;

    public static void openWebActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_activity_web);

        JSWebView jsWebView = (JSWebView) findViewById(R.id.jsWebView);

        jssdk = new JSSDK(this, jsWebView);
        jssdk.register();

//        String html = AssetsTool.getText(this, moduleName + ".html");
//        jssdk.loadData(html, "text/html", "utf-8");
        jssdk.loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }

    @Override
    protected void onDestroy() {
        jssdk.unregister();
        super.onDestroy();
    }
}

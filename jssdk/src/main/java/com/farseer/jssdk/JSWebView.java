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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.*;
import com.farseer.jssdk.internal.JSInvoker;
import com.farseer.tool.LogTool;

/**
 * JSSDK提供外部使用的自定义WebView
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public class JSWebView extends WebView implements JSInvoker, View.OnLongClickListener {

    public static final String TAG = JSWebView.class.getSimpleName();

    private Handler handler = new Handler(Looper.getMainLooper());
    private OnJSClientListener jsClientListener;

    public void setJsClientListener(OnJSClientListener jsClientListener) {
        this.jsClientListener = jsClientListener;
    }

    @Override
    public void onJsInvoke(final String content) {

        LogTool.debug("执行javascript = " + content);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    evaluateJavascript(content, null);
                } else {
                    loadUrl(content);
                }
            }
        });
    }


    public JSWebView(Context context) {
        super(context);
        init(context);
    }

    public JSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setWebViewClient(new JSWebViewClient());
        setWebChromeClient(new JSWebChromeClient());

        setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    private class JSWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (jsClientListener != null) {
                jsClientListener.onPageStarted(view, url, favicon);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (jsClientListener != null) {
                jsClientListener.onPageFinished(view, url);
            }
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (jsClientListener != null) {
                return jsClientListener.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    private class JSWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (jsClientListener != null) {
                jsClientListener.onProgressChanged(view, newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (jsClientListener != null) {
                jsClientListener.onReceivedTitle(view, title);
            }
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (jsClientListener != null) {
                return jsClientListener.onJsPrompt(view, url, message, defaultValue, result);
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            if (jsClientListener != null) {
                return jsClientListener.onJsAlert(view, url, message, result);
            }
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            if (jsClientListener != null) {
                return jsClientListener.onJsConfirm(view, url, message, result);
            }
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            if (jsClientListener != null) {
                return jsClientListener.onJsBeforeUnload(view, url, message, result);
            }
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (jsClientListener != null) {
                return jsClientListener.onConsoleMessage(consoleMessage);
            }
            return super.onConsoleMessage(consoleMessage);
        }

    }
}

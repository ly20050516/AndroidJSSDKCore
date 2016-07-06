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

import android.graphics.Bitmap;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

/**
 * WebView的WebViewClient和WebChromeClient功能接口
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public interface OnJSClientListener {
    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    void onReceivedTitle(WebView view, String title);

    boolean shouldOverrideUrlLoading(WebView view, String url);

    void onProgressChanged(WebView webView, int progress);

    boolean onConsoleMessage(ConsoleMessage consoleMessage);

    boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result);

    boolean onJsAlert(WebView view, String url, String message, JsResult result);

    boolean onJsConfirm(WebView view, String url, String message, JsResult result);

    boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result);
}

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
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;
import com.farseer.jssdk.internal.*;
import com.farseer.tool.LogTool;

import java.util.List;
import java.util.Map;

/**
 * 对外开发的JSSDK
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public class JSSDK implements OnJSClientListener {

    private Context context = null;
    private JSWebView jsWebView = null;
    private JSInvoker jsInvoker = null;
    private JSInjector jsInjector = new JSInjector();
    private Dispatcher jsDispatcher = new JSDispatcher();

    private List<JSProcessor> processorList = null;
    private List<JSModule> moduleList = null;
    private static String namespace = null;

    public JSSDK(Context context, JSWebView jsWebView) {
        this.context = context;
        this.jsInvoker = jsWebView;
        this.jsWebView = jsWebView;
        init();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public static synchronized void init(Context context, String applicationId, String namespace, String... packageNameList) {
        JSSDK.namespace = namespace;
        SDKCacheManager.getInstance().initCache(context, applicationId, packageNameList);
    }

    public void loadData(String data, String mimeType, String encoding) {
        jsWebView.loadData(data, mimeType, encoding);
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        jsWebView.loadUrl(url, additionalHttpHeaders);
    }

    public void loadUrl(String url) {
        jsWebView.loadUrl(url);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        jsWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void register() {
        if (processorList != null) {
            for (JSProcessor jsProcessor : processorList) {
                if (jsProcessor != null) {
                    jsDispatcher.register(jsProcessor);
                }
            }
        }
    }

    public void unregister() {
        if (processorList != null) {
            for (JSProcessor jsProcessor : processorList) {
                if (jsProcessor != null) {
                    jsDispatcher.unregister(jsProcessor);
                }
            }
        }
    }

    public void callJsMethod(String content) {
        if (jsInvoker != null) {
            jsInvoker.onJsInvoke(content);
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private void init() {

        moduleList = SDKCacheManager.getInstance().getModuleList(context, jsDispatcher);
        processorList = SDKCacheManager.getInstance().getProcessorList(context, jsInvoker);

        SDKCacheManager.getInstance().checkMatch(moduleList, processorList);

        if (jsWebView != null) {
            jsWebView.setJsClientListener(this);

            // 注入生成命名空间的javascript代码
            jsInjector.addJsContent(JsFormator.getNamespaceJavascript());

            for (JSModule jsModule : moduleList) {
                jsWebView.addJavascriptInterface(jsModule, jsModule.getName());

                // 生成命名空间
                jsInjector.addJsContent(JsFormator.getRegisterNamespaceJavascript(getNamespace(jsModule.getNamespace()), jsModule.getName()));

                if (jsModule.getJsFunctionNameList() != null) {
                    for(String jsFunctionName : jsModule.getJsFunctionNameList()) {
                        jsInjector.addJsContent(JsFormator.getJsFunctionInitJavascript(getNamespace(jsModule.getNamespace()), jsModule.getName(), jsFunctionName));
                    }
                }
            }
            jsInjector.setInvoker(jsWebView);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogTool.error("onPageStarted url = " + url);
        if (!isJavascript(url)) {
            jsInjector.inject();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogTool.error("onPageFinished: url = " + url);
//        if (!isJavascript(url)) {
//            jsInjector.inject();
//        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogTool.error("onReceivedTitle");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogTool.error("shouldOverrideUrlLoading");
        return false;
    }

    @Override
    public void onProgressChanged(WebView webView, int progress) {
//        LogTool.error("onProgressChanged");
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LogTool.error("onConsoleMessage");
        return false;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        LogTool.error("onJsPrompt");
        return false;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogTool.error("onJsAlert");
        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        LogTool.error("onJsConfirm");
        return false;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        LogTool.error("onJsBeforeUnload");
        return false;
    }

    private String getNamespace(String namespaceOfModule){
        if (!TextUtils.isEmpty(JSSDK.namespace)) {
            return JSSDK.namespace;
        }
        return namespaceOfModule;
    }


    private boolean isJavascript(String url){

        if (url.startsWith("http://")) {
            return false;
        }

        if (url.startsWith("https://")) {
            return false;
        }
        if (url.startsWith("file")) {
            return false;
        }

        if (url.contains("<html>")) {
            return false;
        }

        return true;
    }
}

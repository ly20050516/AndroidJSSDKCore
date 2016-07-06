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

package com.farseer.jssdk.common;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.farseer.jssdk.JSEvent;
import com.farseer.jssdk.JSModule;
import com.farseer.jssdk.Module;
import com.farseer.jssdk.common.event.ClosePageEvent;
import com.farseer.jssdk.common.event.GoBackEvent;
import com.farseer.jssdk.common.event.OpenPageEvent;
import com.farseer.jssdk.common.event.ToastEvent;
import com.farseer.jssdk.internal.Dispatcher;

/**
 * 通用的javascript功能模块
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */

@Module(name = "Common", namespace = "cn.com.itomix")
public class CommonJsModule extends JSModule {

    public CommonJsModule(Context context, Dispatcher dispatcher, String moduleName, String namespace) {
        super(context, dispatcher, moduleName, namespace);
    }

    public void init() {
        for (String function : Constants.FUNCTION_LIST) {
            addJSFunction(function);
        }
    }

    @JavascriptInterface
    public void toast(String json) {
        JSEvent event = new ToastEvent(getName(), Constants.TOAST);
        event.processData(json);
        postEvent(event);
    }

    @JavascriptInterface
    public void openPage(String json) {
        JSEvent event = new OpenPageEvent(getName(), Constants.OPEN_PAGE);
        event.processData(json);
        postEvent(event);
    }

    @JavascriptInterface
    public void closePage(String json) {
        JSEvent event = new ClosePageEvent(getName(), Constants.CLOSE_PAGE);
        event.processData(json);
        postEvent(event);
    }

    @JavascriptInterface
    public void goBack(String json) {
        JSEvent event = new GoBackEvent(getName(), Constants.GO_BACK);
        event.processData(json);
        postEvent(event);
    }
}

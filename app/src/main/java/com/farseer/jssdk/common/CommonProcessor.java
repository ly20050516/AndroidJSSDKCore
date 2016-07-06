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

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.farseer.jssdk.JSProcessor;
import com.farseer.jssdk.JSWebView;
import com.farseer.jssdk.Processor;
import com.farseer.jssdk.WebActivity;
import com.farseer.jssdk.common.event.ClosePageEvent;
import com.farseer.jssdk.common.event.GoBackEvent;
import com.farseer.jssdk.common.event.OpenPageEvent;
import com.farseer.jssdk.common.event.ToastEvent;
import com.farseer.jssdk.internal.JSInvoker;
import com.squareup.otto.Subscribe;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/27
 */

@Processor(name = "Common")
public class CommonProcessor extends JSProcessor {

    public CommonProcessor(Context context, JSInvoker jsInvoker, String name) {
        super(context, jsInvoker, name);
    }

    @Subscribe
    public void processToastEvent(ToastEvent event) {
        ToastEvent.Data data = event.getData();
        if (data != null && data.check()) {
            Toast.makeText(getContext(), data.getContent(), Toast.LENGTH_LONG).show();
            getJsInvoker().onJsInvoke(event.getCallback(""));
        }
    }

    @Subscribe
    public void processClosePageEvent(ClosePageEvent event) {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    @Subscribe
    public void processGoBackEvent(GoBackEvent event) {
        JSWebView jsWebView = (JSWebView) getJsInvoker();
        jsWebView.goBack();
    }

    @Subscribe
    public void processOpenPageEvent(OpenPageEvent event) {
        OpenPageEvent.Data data = event.getData();
        if (data != null && data.check()) {
            WebActivity.openWebActivity(getContext(), event.getData().getUrl());
            getJsInvoker().onJsInvoke(event.getCallback(""));
        }
    }
}

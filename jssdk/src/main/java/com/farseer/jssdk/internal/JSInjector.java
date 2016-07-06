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

package com.farseer.jssdk.internal;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 向javascript注入方法类
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public class JSInjector {


    private JSInvoker invoker;

    private List<String> jsContentList = new ArrayList<>();

    public void setInvoker(JSInvoker invoker) {
        this.invoker = invoker;
    }

    public void addJsContent(String jsContent) {
        if (!TextUtils.isEmpty(jsContent)) {
            this.jsContentList.add(JsFormator.getEvalJavascript(jsContent));
        }
    }

    public void inject() {
        if (invoker != null) {
            for (String jsContent : jsContentList) {
                invoker.onJsInvoke(jsContent);
            }
        }
    }
}

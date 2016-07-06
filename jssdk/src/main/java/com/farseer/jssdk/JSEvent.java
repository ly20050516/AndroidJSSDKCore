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

import com.farseer.jssdk.internal.JsFormator;
import com.farseer.tool.LogTool;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/27
 */
public abstract class JSEvent {

    private String module;
    private String function;

    public JSEvent(String module, String function) {
        this.module = module;
        this.function = function;
    }

    public String getModule() {
        return module;
    }

    public String getFunction() {
        return function;
    }

    public String getCallback(String json){
        return JsFormator.getEvalJavascript(JsFormator.getCallbackJavascript(module, function, json));
    }

    public abstract void processData(String data);

    public void log(String data) {
        String text =  "JSEvent{" +
                "module='" + module + '\'' +
                ", function='" + function + '\'' +
                ", data='" + data + '\'' +
                '}';
        LogTool.debug(text);
    }
}

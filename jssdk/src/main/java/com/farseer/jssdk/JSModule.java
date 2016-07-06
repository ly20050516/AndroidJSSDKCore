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

import android.content.Context;
import com.farseer.jssdk.internal.Dispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的javascript功能模块
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public abstract class JSModule {


    private Context context;
    private String moduleName;
    private String namespace;
    private List<String> jsFunctionNameList = new ArrayList<>();
    private Dispatcher dispatcher;


    public abstract void init();

    public JSModule(Context context, Dispatcher dispatcher, String moduleName, String namespace) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.moduleName = moduleName;
        this.namespace = namespace;
        init();
    }


    public void postEvent(JSEvent event){
        if (dispatcher != null) {
            dispatcher.post(event);
        }
    }

    public String getName() {
        return moduleName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void addJSFunction(String jsFunctionName) {
        if (jsFunctionName != null) {
            jsFunctionNameList.add(jsFunctionName);
        }
    }

    public List<String> getJsFunctionNameList() {
        return jsFunctionNameList;
    }
}

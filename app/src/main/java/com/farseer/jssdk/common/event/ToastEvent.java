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

package com.farseer.jssdk.common.event;

import android.text.TextUtils;
import com.farseer.jssdk.JSEvent;
import com.farseer.tool.JsonTool;
import com.farseer.tool.LogTool;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/27
 */
public class ToastEvent extends JSEvent {

    private Data data;

    public ToastEvent(String module, String function) {
        super(module, function);
    }

    @Override
    public void processData(String json) {
        log(json);
        data = JsonTool.fromJsonString(json, new TypeToken<Data>() {}.getType());
        if (data == null || !data.check()) {
            LogTool.error(String.format("toast 's params of the module are not support", getModule()));
        }
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("content")
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean check() {
            if (!TextUtils.isEmpty(content)) {
                return true;
            }
            return false;
        }
    }
}

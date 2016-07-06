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

package com.farseer.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/28
 */
public class JsonTool {


    private static final Gson mGson = new GsonBuilder().create();

    //JSONUtils.fromJsonString(json, new TypeToken<T>() {}.getType());
    public static <T> T fromJsonString(String json, Type typeOfT) {
        try {
            return mGson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonString(Object object) {
        try {
            return mGson.toJson(object);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
}

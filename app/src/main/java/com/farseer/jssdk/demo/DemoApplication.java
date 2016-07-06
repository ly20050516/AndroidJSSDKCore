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

package com.farseer.jssdk.demo;

import android.app.Application;
import com.farseer.jssdk.JSSDK;
import com.farseer.tool.ProcessTool;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/25
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String processName = ProcessTool.getProcessName(getApplicationContext());
        if (!BuildConfig.APPLICATION_ID.equals(processName)) {
            return;
        }

        initApplication();
    }

    private void initApplication() {

        JSSDK.init(this, BuildConfig.APPLICATION_ID, "ITOMIX", "com.farseer.jssdk");
    }
}

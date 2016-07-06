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

import android.os.Handler;
import android.os.Looper;
import com.farseer.jssdk.JSEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/27
 */
public class JSDispatcher implements Dispatcher {

    private Bus bus = new Bus(ThreadEnforcer.ANY);
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void register(Object object) {
        bus.register(object);
    }

    @Override
    public void unregister(Object object) {
        bus.unregister(object);
    }

    @Override
    public void post(final JSEvent event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bus.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(event);
                }
            });
        }
    }
}

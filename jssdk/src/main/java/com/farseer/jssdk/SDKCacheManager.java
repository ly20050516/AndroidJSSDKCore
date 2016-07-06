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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.farseer.jssdk.internal.Dispatcher;
import com.farseer.jssdk.internal.JSInvoker;
import com.farseer.tool.LogTool;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/25
 */
public class SDKCacheManager {

    private static List<Class> moduleClassList = new ArrayList<>();
    private static List<Class> processorClassList = new ArrayList<>();


    private static SDKCacheManager ourInstance = new SDKCacheManager();

    public static SDKCacheManager getInstance() {
        return ourInstance;
    }

    private SDKCacheManager() {

    }

    public void initCache(Context context, String applicationId, String... packageNameList) {
        moduleClassList = scanAllClass(context, Module.class, applicationId, packageNameList);
        processorClassList = scanAllClass(context, Processor.class, applicationId, packageNameList);
    }

    public void clearCache() {
        moduleClassList.clear();
        processorClassList.clear();
    }

    public List<JSModule> getModuleList(Context context, Dispatcher dispatcher) {
        List<JSModule> moduleList = constructorModuleList(context, dispatcher, moduleClassList);
        return moduleList;
    }

    public List<JSProcessor> getProcessorList(Context context, JSInvoker jsInvoker) {
        List<JSProcessor> processorList = constructorProcessorList(context, jsInvoker, processorClassList);
        return processorList;
    }

    public boolean checkMatch(List<JSModule> moduleList, List<JSProcessor> processorList) {

        if (moduleList == null) {
            return false;
        }

        if (processorList == null) {
            return false;
        }

        Set<String> moduleNameSet = new HashSet<>();

        for (JSModule module : moduleList) {
            moduleNameSet.add(module.getName());
        }

        StringBuilder builder = new StringBuilder();
        for (JSProcessor processor : processorList) {
            if (moduleNameSet.contains(processor.getName())) {
                moduleNameSet.remove(processor.getName());
            } else {
                builder.append(String.format("\nthe processor named %s not match any module", processor.getName()));
            }
        }

        for (String name : moduleNameSet) {
            builder.append(String.format("\nthe module named %s not match any processor", name));
        }

        String result = builder.toString();
        if (TextUtils.isEmpty(result)) {
            return true;
        }

        LogTool.error(result);
        return false;
    }


    private List<Class> scanAllClass(Context context, Class clazz, String applicationId, String... packageNameList) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(applicationId, 0);
            String apkName = applicationInfo.sourceDir;
            DexFile dexFile = new DexFile(apkName);
            PathClassLoader classLoader = new PathClassLoader(apkName, Thread.currentThread().getContextClassLoader());
            List<Class> classList = new ArrayList<>();
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String entry = entries.nextElement();
                if (isNeedClass(entry, packageNameList)) {
                    Class<?> entryClass = classLoader.loadClass(entry);
                    if (entryClass != null) {
                        Annotation[] annotations = entryClass.getAnnotations();
                        for (Annotation annotation : annotations) {
                            if (annotation.annotationType().equals(clazz)) {
                                classList.add(entryClass);
                            }
                        }
                    }
                }
            }
            return classList;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isNeedClass(String entry,  String... packageNameList){

        for (String packageName : packageNameList) {
            if (entry.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    private List<JSModule> constructorModuleList(Context context, Dispatcher dispatcher, List<Class> classList) {
        List<JSModule> moduleList = new ArrayList<>();

        for (Class clazz : classList) {
            try {
                Module module = (Module) clazz.getAnnotation(Module.class);
                String moduleName = module.name();
                String namespace = module.namespace();
                Constructor<JSModule> constructor = clazz.getConstructor(Context.class, Dispatcher.class, String.class, String.class);
                JSModule jsModule = constructor.newInstance(context, dispatcher, moduleName, namespace);
                moduleList.add(jsModule);
                LogTool.error("successful inject module: " + moduleName + ", namespace = " + namespace);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return moduleList;
    }

    private List<JSProcessor> constructorProcessorList(Context context, JSInvoker jsInvoker, List<Class> classList) {
        List<JSProcessor> processorList = new ArrayList<>();

        for (Class clazz : classList) {
            try {
                Processor processor = (Processor) clazz.getAnnotation(Processor.class);
                String name = processor.name();
                Constructor<JSProcessor> constructor = clazz.getConstructor(Context.class, JSInvoker.class, String.class);
                JSProcessor jsProcessor = constructor.newInstance(context, jsInvoker, name);
                processorList.add(jsProcessor);
                LogTool.error("successful inject processor: " + name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return processorList;
    }


}

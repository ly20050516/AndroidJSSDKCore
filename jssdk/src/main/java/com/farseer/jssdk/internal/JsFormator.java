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

/**
 /**
 * class description
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/30
 */
public class JsFormator {

    public static String getEvalJavascript(String javascriptContent) {
        return String.format("javascript:%s;", javascriptContent);
    }

    /**
     * 获得生成命名空间方法的javascript.
     *
     * if (typeof Namespace == "undefined") {
     *     var Namespace = new Object();
     * }
     *
     * if (typeof Namespace.register == "undefined") {
     *     Namespace.register = function(path) {
     *         var arr = path.split(".");
     *         var ns = "";
     *         for (var i = 0; i < arr.length; i++) {
     *             if (i > 0) {
     *                 ns += ".";
     *             }
     *             ns += arr[i];
     *             eval("if(typeof(" + ns + ") == 'undefined') " + ns + " = new Object();");
     *         }
     *     }
     * }
     *
     * Namespace.register("com.farseer");
     *
     * @return 生成命名空间方法的javascript
     */
    public static String getNamespaceJavascript() {

        return "if (typeof Namespace == \"undefined\") {\n" +
                "\tvar Namespace = new Object();\n" +
                "}\n" +
                "if (typeof Namespace.register == \"undefined\") {\n" +
                "\tNamespace.register = function(path) {\n" +
                "\t    var arr = path.split(\".\");\n" +
                "\t    var ns = \"\";\n" +
                "\t    for (var i = 0; i < arr.length; i++) {\n" +
                "\t        if (i > 0) {\n" +
                "\t            ns += \".\";\n" +
                "\t        }\n" +
                "\t        ns += arr[i];\n" +
                "\t        eval(\"if(typeof(\" + ns + \") == \\\"undefined\\\") \" + ns + \" = new Object();\");\n" +
                "\t    }\n" +
                "\t}\t\n" +
                "}\n";
    }

    /**
     * 获得执行注册命名空间的javascript.
     *
     * 例如:
     * Namespace.register('ITOMIX.Dialog');
     * if (typeof DialogCallbackCache == 'undefined') {
     *     var DialogCallbackCache = new Object();
     * }
     *
     * @param namespace     命名空间
     * @param moduleName    模块名称
     *
     * @return 注册命名空间的javascript
     */
    public static String getRegisterNamespaceJavascript(String namespace, String moduleName) {
        // CommonCallbackCache
        String jsCallbackCacheName = getJsCallbackCacheName(moduleName);
        return String.format("Namespace.register('%s.%s');\n" +
                "if (typeof %s == 'undefined') {\n" +
                "        var %s = new Object();\n" +
                "}", namespace, moduleName, jsCallbackCacheName, jsCallbackCacheName);
    }

    /**
     * 获得注入js方法的javascript.
     *
     * 例如:
     * if (typeof cn.farseer.Common.toast == 'undefined') {
     *     var cn.farseer.Common.toast  = function(params, callback) {
     *         console.log('Common cn.farseer.Common.toast  inject begin');
     *         Common_callbacks['cn.farseer.Common.toast'] = callback;
     *         Common.toast(params);
     *         console.log('Common cn.farseer.Common.toast  inject end');
     *     };
     * }
     *
     * @param namespace         命名空间
     * @param moduleName        模块名称
     * @param functionName      js方法名称
     *
     * @return 注入js方法的javascript
     */
    public static String getJsFunctionInitJavascript(String namespace, String moduleName, String functionName) {

        // com.farseer.Common.toast
        String jsVariableName = getJsVariableName(namespace, moduleName, functionName);
        // CommonCallbackCache
        String jsCallbackCacheName = getJsCallbackCacheName(moduleName);
        // Common.toast
        String nativeFunctionName = getNativeFunctionName(moduleName, functionName);

        return String.format("if (typeof %s == 'undefined') {\n" +
                        "\t%s = function(params, callback) \n" +
                        "\t{\n" +
                        "\t\tconsole.log('%s inject begin'); \n" +
                        "\t\t%s['%s'] = callback;\n" +
                        "\t\t%s(params); \n" +
                        "\t\tconsole.log('%s inject end');\n" +
                        "\t};\n" +
                        "}", jsVariableName, jsVariableName, jsVariableName, jsCallbackCacheName, functionName, nativeFunctionName, jsVariableName);
    }

    /**
     * 获得执行callback方法的javascript.
     *
     * 例如:
     * if (typeof Common_callbacks['toast'] != "undefined") {
     *     Common_callbacks['toast'](params);
     *     delete Common_callbacks['%s'];
     * }
     *
     * @param moduleName        模块名称
     * @param functionName      js方法名称
     * @param  json             回调参数,数据格式为json
     *
     * @return callback方法的javascript
     */
    public static String getCallbackJavascript(String moduleName, String functionName, String json) {
        // CommonCallbackCache
        String jsCallbackCacheName = getJsCallbackCacheName(moduleName);
        return String.format("if (typeof %s['%s'] != 'undefined') {\n" +
                "\t%s['%s'](%s);\n" +
                "\tdelete %s['%s'];\n" +
                "}\n", jsCallbackCacheName, functionName, jsCallbackCacheName, functionName, json, jsCallbackCacheName, functionName);
    }

    /**
     * 获得js方法的完整名称.
     *
     * 例如:com.farseer.Common.toast
     *
     * @param namespace         命名空间
     * @param moduleName        模块名称
     * @param functionName      js方法名称
     *
     * @return js方法的完整名称.
     */
    private static String getJsVariableName(String namespace, String moduleName, String functionName) {
        StringBuilder builder = new StringBuilder();
        builder.append(namespace);
        builder.append(".");
        builder.append(moduleName);
        builder.append(".");
        builder.append(functionName);
        return builder.toString();
    }

    /**
     * 获得js callback变量名称.
     *
     * 例如: CommonCallbackCache
     *
     * @param moduleName 模块名称
     *
     * @return js callback变量名称
     */
    private static String getJsCallbackCacheName(String moduleName) {
        StringBuilder builder = new StringBuilder();
        builder.append(moduleName);
        builder.append("CallbackCache");
        return builder.toString();

    }

    /**
     * 获得本地方法完整名称.
     *
     * 例如:Common.toast
     *
     * @param moduleName        模块名称
     * @param functionName      js方法名称\
     *
     * @return 本地方法完整名称
     */
    private static String getNativeFunctionName(String moduleName, String functionName) {
        StringBuilder builder = new StringBuilder();
        builder.append(moduleName);
        builder.append(".");
        builder.append(functionName);
        return builder.toString();
    }
}

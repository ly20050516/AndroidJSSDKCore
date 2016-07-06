# AndroidJSSDKCore

AndroidJSSDKCore是在Android平台上native与js交互的通讯协议库。


在每个模块中会预定义通信js方法,在WebView加载页面完成后, native向WebView中动态注册通信js方法的实现。
通信js方法的实现内部完成js对native的调用,以及相关方法的回调处理。

通信js方法格式统一定义为:
    namespace.module.func(params, callback)
 
* namespace是接入客户端自定义的js方法命名空间,例如:com.farseer;
* module是基于AndroidJSSDKCore实现Module的名称,例如:Common,Dialog等;
* func是js方法名称,例如:toast,normalDialog等;
* params是js向native传递参数,数据格式为json,具体内容根据每个模块通信js方法定义(具体见每个模块的README.md);
* callback是native回调js方法,支持匿名js方法,方法原型为function(params){}, 参数params是native向js传递的数据,数据格式为json。

例如:
* com.farseer.Common.toast(JSON.stringify({'content':'toast提示'}), function(params){});
* com.farseer.Dialog.normalDialog(JSON.stringify({'title': 'title','content': 'content','positiveText': 'Sure','negativeText': 'Cancel'}), function(params){com.farseer.Common.toast(JSON.stringify({'content':params}), function(){});});


 
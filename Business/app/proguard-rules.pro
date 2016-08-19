
-optimizationpasses 5 #指定的代码压缩级别
-dontusemixedcaseclassnames #是否使用大小写混合
-dontskipnonpubliclibraryclasses#忽略非公共的类库

-dontpreverify #混淆时时候是否做校验

-verbose #混淆日志
#忽略警告
-ignorewarning

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keepattributes *Annotation*#保护注解

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keep  public class java.util.HashMap {
	public <methods>;
}
-keep  public class java.lang.String {
	public <methods>;
}
-keep  public class java.util.List {
	public <methods>;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class android.webkit.WebView{*;}
-keep public class android.webkit.SslErrorHandler{*;}
-keep public class android.net.http.SslError{*;}
-keepnames class * implements java.io.Serializable

#百度
-keep class com.baidu.**{*;}
-keep class com.google.gson.** {*;}
-keep class com.google.zxing.** {*;}
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.alibaba.fastjson.**{*;}
-keep class okio.**{*;}
-keep class com.squareup.okhttp.**{*;}
-keep class com.android.support.**{*;}
-keep public class com.xhl.bqlh.business.Model.** { *; }

# EventBus
-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}

################### region for xUtils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#################### end region

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature
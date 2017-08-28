# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\andriod_studio\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 ##################################################################################################
 #########以下通用{
    #指定代码的压缩级别
    -optimizationpasses 5
    -dontskipnonpubliclibraryclassmembers
    #包名不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
#    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     # 不做预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
#    -keepattributes *Annotation*
#    -keepattributes InnerClasses
    #保持泛型
    -keepattributes Signature
    #保留行号
    -keepattributes SourceFile,LineNumberTable
    #保留反射
    -keepattributes EnclosingMethod
     # 保持哪些类不被混淆
#2017/05/31    -keep public class * extends android.app.Fragment
#2017/05/31    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
#2017/05/31    -keep public class * extends android.preference.Preference
#2017/05/31    -keep public class * extends android.support.v4.app.FragmentActivity
#2017/05/31    -keep public class * extends android.support.multidex.MultiDexApplication
#2017/05/31    -keep public class * extends android.app.Application
#2017/05/31    -keep public class * extends android.app.backup.BackupAgentHelper
#2017/05/31    -keep class android.support.** {*;}
    #如果有引用v4包可以添加下面这行
#2017/05/31    -keep public class * extends android.support.v4.app.Fragment
    #忽略警告
    -ignorewarning

    #-------------记录生成的日志数据,gradle build时在本项目根目录输出##
    #apk 包内所有 class 的内部结构
#    -dump class_files.txt
#    #未混淆的类和成员
#    -printseeds seeds.txt
#    #列出从 apk 中删除的代码
#    -printusage unused.txt
#    #混淆前后的映射
#    -printmapping mapping.txt
    #-------------记录生成的日志数据，gradle build时 在本项目根目录输出-end######

     #保持 native 方法不被混淆
        -keepclasseswithmembernames class * {
            native <methods>;
        }

        #保持自定义控件类不被混淆
#2017/05/31        -keepclasseswithmembers class * {
#2017/05/31            public <init>(android.content.Context, android.util.AttributeSet);
#2017/05/31            public <init>(android.content.Context, android.util.AttributeSet, int);
#2017/05/31        }

        #保持自定义控件类不被混淆
#2017/05/31        -keepclassmembers class * extends android.app.Activity {
#2017/05/31           public void *(android.view.View);
#2017/05/31        }

        #保持 Parcelable 不被混淆
#2017/05/31        -keep class * implements android.os.Parcelable {
#2017/05/31          public static final android.os.Parcelable$Creator *;
#2017/05/31        }

        #保持 Serializable 不被混淆
#2017/05/31        -keepnames class * implements java.io.Serializable

        #保持 Serializable 不被混淆并且enum 类也不被混淆
#2017/05/31        -keepclassmembers class * implements java.io.Serializable {
#2017/05/31            static final long serialVersionUID;
#2017/05/31            private static final java.io.ObjectStreamField[] serialPersistentFields;
#2017/05/31            !static !transient <fields>;
#2017/05/31           !private <fields>;
#2017/05/31            !private <methods>;
#2017/05/31            private void writeObject(java.io.ObjectOutputStream);
#2017/05/31            private void readObject(java.io.ObjectInputStream);
#2017/05/31            java.lang.Object writeReplace();
#2017/05/31            java.lang.Object readResolve();
#2017/05/31        }

        #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#2017/05/31        -keepclassmembers enum * {
#2017/05/31          public static **[] values();
#2017/05/31          public static ** valueOf(java.lang.String);
#2017/05/31        }

        #这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
#2017/05/31        -keepclassmembers class * extends android.app.Activity {
#2017/05/31           public void *(android.view.View);
#2017/05/31        }
        #不混淆资源类
#2017/05/31        -keepclassmembers class **.R$* {
#2017/05/31            public static <fields>;
#2017/05/31        }
#2017/05/31        -keep public class * extends android.view.View {
#2017/05/31               public <init>(android.content.Context);
#2017/05/31               public <init>(android.content.Context, android.util.AttributeSet);
#2017/05/31               public <init>(android.content.Context, android.util.AttributeSet, int);
#2017/05/31               public void set*(...);
#2017/05/31               *** get*();
#2017/05/31           }
#2017/05/31           -keep class **.R$* {
#2017/05/31            *;
#2017/05/31           }
#2017/05/31           #---------------------------------webview------------------------------------
#2017/05/31           -keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#2017/05/31             public *;
#2017/05/31           }
#2017/05/31           -keepclassmembers class * extends android.webkit.WebViewClient {
#2017/05/31               public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#2017/05/31               public boolean *(android.webkit.WebView, java.lang.String);
#2017/05/31           }
#2017/05/31           -keepclassmembers class * extends android.webkit.WebViewClient {
#2017/05/31               public void *(android.webkit.WebView, jav.lang.String);
#2017/05/31            }
    #忽略警告/如果引用了v4或者v7包
#2017/05/31    -dontwarn android.support.**


#把混淆类中的方法名也混淆了
-useuniqueclassmembernames
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#########以上通用}
##################################################################################################



    #如果不想混淆 keep 掉

    -keep class com.google.android.**{*;}
    -dontwarn com.google.android.**

    #keep 银联
    -keep class com.unionpay.**{*;}
    -dontwarn com.unionpay.**

     #keep P2000
     -keep class com.basewin.** {*;}
    -dontwarn com.basewin.**

     #keep gson
     -keep class com.google.gson.** {*;}
     -dontwarn com.google.gson.**

    #keep ksoap2
    -keep class org.** {*;}
    -dontwarn org.**

    #keep ormlite
     -keep class com.j256.ormlite.** {*;}
    -dontwarn com.j256.ormlite.**

    #keep wanliansdk
     -keep class com.wanlianapp.wanliansdk.** {*;}
    -dontwarn com.wanlianapp.wanliansdk.**

     #keep 友盟
     -keep class com.umeng.**{*;}
     -dontwarn com.umeng.**
     #keep 注解
     -keep class butterknife.**{*;}
     -dontwarn butterknife.**
     -dontwarn butterknife.internal.**
     -keep class **$$ViewBinder { *; }
     -keepclasseswithmembernames class * {
         @butterknife.* <fields>;
     }
     -keepclasseswithmembernames class * {
         @butterknife.* <methods>;
     }
     #keep 银联
     -keep class com.unionpay.**{*;}
     -dontwarn com.unionpay.**
     #keep tencent
     -keep class com.tencent.**{*;}
     -dontwarn com.tencent.**
     #keep baidu
     -keep class com.baidu.** {*;}
     -keep class vi.com.** {*;}
     -dontwarn com.baidu.**

     #keep 长连接
      -keep class io.netty.** {*;}
      -dontwarn io.netty.**
     -keepattributes Signature,InnerClasses
     -keepclasseswithmembers class io.netty.** {*;}
     -keepnames class io.netty.** {*;}

     #keep okio   okhttp3
      -keep class okio.** {*;}
      -dontwarn okio.**

     -keep class org.apache.** {*;}
     -dontwarn org.apache.**
     #keep nineoldandroids
     -keep class com.nineoldandroids.** {*;}
     -dontwarn com.nineoldandroids.**
     #keep volley
     -keep class com.android.volley.** {*;}
     -dontwarn com.android.volley.**
     #keep 二维码
     -keep class com.google.zxing.** {*;}
     -dontwarn com.google.zxing.**
     #keep imageloader
     -keep class com.nostra13.** {*;}
     -dontwarn com.nostra13.**
     -dontwarn java.lang.invoke**

      #keep gaodeMap
     -keep class com.amap.** {*;}
     -dontwarn com.amap.**
     -dontwarn com.amap.api.**
     -keep class com.a.a.**  {*;}
     -dontwarn com.a.a.**
    -keep class com.autonavi.** {*;}
     -dontwarn com.autonavi.**

    #keep支付宝
     -keep class com.alipay.**{ *;}
     -dontwarn com.alipay.**
     -keep class com.ta.**{ *;}
     -dontwarn com.ta.**
     -keep class com.ut.**{ *;}
     -dontwarn com.ut.**

    #keep tencent
     -keep class com.tencent.**{ *;}
     -dontwarn com.tencent.**

     #keep eventbus
    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }
    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
        <init>(Java.lang.Throwable);
    }
    -keepclasseswithmembernames class * {
         @Subcribe.* <fields>;
     }
    -keepclasseswithmembernames class * {
         @Subcribe.* <methods>;
     }

    #淆规则表明不混淆所有的GlideModule
    -keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
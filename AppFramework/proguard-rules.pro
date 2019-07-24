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
    -ignorewarnings
     #优化  不优化输入的类文件
    -dontoptimize

     # 不做预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*
    -keepattributes InnerClasses
    #保持泛型
    -keepattributes Signature
    #保留行号
    -keepattributes SourceFile,LineNumberTable
    #保留反射
    -keepattributes EnclosingMethod
     # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment

    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.preference.Preference
    -keep public class * extends android.support.v4.app.FragmentActivity
    -keep public class * extends android.support.multidex.MultiDexApplication
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep class android.support.** {*;}
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment
    #忽略警告
    -ignorewarnings

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
    -keepclasseswithmembers class * {
         public <init>(android.content.Context, android.util.AttributeSet);
         public <init>(android.content.Context, android.util.AttributeSet, int);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
         public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
         private static final java.io.ObjectStreamField[] serialPersistentFields;
         !static !transient <fields>;
          !private <fields>;
         !private <methods>;
         private void writeObject(java.io.ObjectOutputStream);
         private void readObject(java.io.ObjectInputStream);
          java.lang.Object writeReplace();
         java.lang.Object readResolve();
    }

    #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
    -keepclassmembers enum * {
      public static **[] values();
       public static ** valueOf(java.lang.String);
     }

        #这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆

   -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
   }

   #不混淆资源类
   -keepclassmembers class **.R$* {
        public static <fields>;
   }

   -keep public class * extends android.view.View {
            public <init>(android.content.Context);
           public <init>(android.content.Context, android.util.AttributeSet);
           public <init>(android.content.Context, android.util.AttributeSet, int);
           public void set*(...);
            *** get*();
   }

   -keep class **.R$* {
    *;
    }

   #---------------------------------webview------------------------------------
    -keepclassmembers class fqcn.of.javascript.interface.for.Webview {
        public *;
    }

   -keepclassmembers class * extends android.webkit.WebViewClient {
       public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
       public boolean *(android.webkit.WebView, java.lang.String);
   }

   -keepclassmembers class * extends android.webkit.WebViewClient {
       public void *(android.webkit.WebView, jav.lang.String);
   }

   #忽略警告/如果引用了v4或者v7包
   -dontwarn android.support.**


    #把混淆类中的方法名也混淆了
    -useuniqueclassmembernames
    #优化时允许访问并修改有修饰符的类和类的成员
    -allowaccessmodification
    #将文件来源重命名为“SourceFile”字符串
    -renamesourcefileattribute SourceFile
    #########以上通用}
##################################################################################################




    #如果不想混淆 keep 掉
    #keep tencent
    -keep class com.tencent.**{*;}
    -dontwarn com.tencent.**

    -keep class com.google.android.**{*;}
    -dontwarn com.google.android.**

    #keep 银联
    -keep class com.unionpay.**{*;}
    -dontwarn com.unionpay.**

     #keep gson
     -keep class com.google.gson.** {*;}
     -dontwarn com.google.gson.**

    #keep ksoap2
    -keep class org.** {*;}
    -dontwarn org.**

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

    -keep class com.tencent.mm.opensdk.** {*;}
    -keep class com.tencent.wxop.** {*; }
    -keep class com.tencent.mm.sdk.** {*;}

     #keep eventbus
    -keepclassmembers class ** {
        @Subscribe <methods>;
    }
    -keep enum ThreadMode { *; }
    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends ThrowableFailureEvent {
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

    # greenDao混淆 3.0及以上Scan
    -keep class org.greenrobot.dao.** {*;}
    -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
    }
    -keep class **$Properties
    #滤镜
    -keep class com.muzhi.camerasdk.**{*;}
    -keep class com.mdroid.**{*;}
    -keep class com.library.camerafilter.**{*;}
    -keep class com.muzhi.camerasdk.library.filter.OpenGlUtils.**{*;}
    #防止这个类混淆出错 com.asiainfo.andcampus.util.TabLayoutIndicatorUtil(android.support.design.widget.TabLayout用了反射
    -keep public class  android.support.design.**{*;}
    -keep public class com.framework2.tinker.app.**{*;}#用了反射





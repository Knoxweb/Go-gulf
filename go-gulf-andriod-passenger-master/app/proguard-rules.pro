# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 -dontwarn org.intellij.lang.annotations.Language
 -dontwarn org.jetbrains.annotations.VisibleForTesting

 # R8 full mode strips generic signatures from return types if not kept.
 -if interface * { @retrofit2.http.* public *** *(...); }
 -keep,allowoptimization,allowshrinking,allowobfuscation class <3>

 # With R8 full mode generic signatures are stripped for classes that are not kept.
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

-dontwarn org.jetbrains.annotations.NotNull
-dontwarn org.jetbrains.annotations.Nullable

-keep class com.gogulf.passenger.app.utils.** {*;}
-keep class com.gogulf.passenger.app.utils.others.** {*;}
-keep class com.gogulf.passenger.app.utils.objects.** {*;}

-keep class com.gogulf.passenger.app.data.model.MainWithAuthentications
-keep class com.gogulf.passenger.app.data.model.auths.Authentications

-keep class com.gogulf.passenger.app.data.apidata.DefaultRequestModel
-keep class com.gogulf.passenger.app.data.api.** {*;}
-keep class com.gogulf.passenger.app.data.model.** {*;}
-keep class com.gogulf.passenger.app.data.model.auths.** {*;}
-keep class com.gogulf.passenger.app.data.model.base.** {*;}
-keep class com.gogulf.passenger.app.data.model.dashboards.** {*;}
-keep class com.gogulf.passenger.app.data.model.firestore.** {*;}
-keep class com.gogulf.passenger.app.data.model.others.** {*;}
-keep class com.gogulf.passenger.app.data.model.request.** {*;}
-keep class com.gogulf.passenger.app.data.model.response.** {*;}
-keep class com.gogulf.passenger.app.data.model.response.bookings.** {*;}
-keep class com.gogulf.passenger.app.data.model.response.currentride.** {*;}
-keep class com.gogulf.passenger.app.data.model.response.mycards.** {*;}
-keep class com.gogulf.passenger.app.data.model.response.notification.** {*;}
-keep class com.gogulf.passenger.app.data.apidata.** {*;}

-keep class com.gogulf.passenger.app.ui.getaride.** {*;}
-keep class com.gogulf.passenger.app.ui.support.supports.**{*;}


-keepclassmembers class com.gogulf.passenger.app.data.model.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.auths.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.base.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.dashboards.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.firestore.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.others.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.request.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.response.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.response.bookings.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.response.currentride.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.response.mycards.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.model.response.notification.** {*;}
-keepclassmembers class com.gogulf.passenger.app.data.apidata.** {*;}


-keep class com.gogulf.passenger.app.ui.menu.MenuModels


# Preserve the class names in the io.reactivex.rxjava3 package
-keepclassmembers class io.reactivex.rxjava3.** { *; }

# Don't obfuscate the package
-keepnames class io.reactivex.rxjava3.**

# RxJava uses reflection in some operators, so keep all method names
-keepclassmembers class * {
    @io.reactivex.rxjava3.annotations.** *;
}

# Needed for generic types used in RxJava
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keepattributes Signature


-keep class com.shockwave.**

-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

-dontwarn org.slf4j.impl.StaticLoggerBinder

-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.* { *; }
-keep class com.google.maps.android.** { *; }

# NEW ADDED
# Keep all classes from google-maps-services library
-keep class com.google.maps.** { *; }

# Keep all classes from android-maps-utils library
-keep class com.google.maps.android.** { *; }

-keep class com.google.maps.model.** {*;}
-keepclassmembers class com.google.maps.model.** {*;}

-keep class com.google.android.gms.maps.model.** {*;}
-keepclassmembers class com.google.android.gms.maps.model.** {*;}
# Optional: If the libraries use annotations, you might want to keep them too
-keepattributes *Annotation*

# Optional: If the libraries use any reflection, keep all class members
-keepclassmembers class com.google.maps.** { *; }
-keepclassmembers class com.google.maps.android.** { *; }
-keep class com.google.firebase.** {*;}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keep class com.gogulf.passenger.app.** {*;}


-dontwarn com.google.android.gms.auth.api.credentials.Credential$Builder
-dontwarn com.google.android.gms.auth.api.credentials.Credential
-dontwarn com.google.android.gms.auth.api.credentials.CredentialRequest$Builder
-dontwarn com.google.android.gms.auth.api.credentials.CredentialRequest
-dontwarn com.google.android.gms.auth.api.credentials.CredentialRequestResponse
-dontwarn com.google.android.gms.auth.api.credentials.Credentials
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsClient
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsOptions$Builder
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsOptions
-dontwarn com.google.android.gms.auth.api.credentials.HintRequest$Builder
-dontwarn com.google.android.gms.auth.api.credentials.HintRequest

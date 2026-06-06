# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /sdk/tools/proguard/proguard-android.txt

# Keep WebView classes
-keep class android.webkit.** { *; }

# Keep application classes
-keep class com.openhands.app.** { *; }

# Keep custom views
-keep public class * extends android.view.View

# Keep Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Allow obfuscation
-renamesourcefileattribute SourceFile
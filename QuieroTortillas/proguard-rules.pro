-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-dontoptimize
-dontobfuscate

-dontwarn com.squareup.okhttp.**

-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

# Eventbus
-keepattributes *Annotation* -keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**


# ******* GSON ******

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
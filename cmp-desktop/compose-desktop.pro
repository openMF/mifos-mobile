-ignorewarnings
-dontwarn kotlinx.coroutines.debug.*

-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }
-keep class com.arkivanov.essenty.** { *; }
-keep class org.sqlite.** { *; }

# Webcam
-keep class com.github.sarxos.webcam.** { *; }
-keep class org.bridj.** { *; }

# Windows folders
-keep class com.sun.jna.* { *; }
-keepclassmembers class * extends com.sun.jna.* { public *; }

# Keep Ktor classes
-keep class io.ktor.** { *; }
-dontnote io.ktor.**

# Apollo workarounds
-dontnote okio.**
-keep class com.apollographql.apollo.** { *; }
-dontnote com.apollographql.**
-keep class okhttp3.** { *; }
-keep class org.bouncycastle.** { *; }
-dontnote okhttp3.internal.platform.**
-dontwarn okhttp3.internal.platform.**

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keeppackagenames okhttp3.internal.publicsuffix.*
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Keep the class and fields of kotlinx.datetime.Instant
-keep class kotlinx.datetime.Instant { *; }
-dontnote kotlinx.datetime.Instant

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**
-keepnames class kotlinx.serialization.internal.** { *; }

# Do not warn about missing annotations and metadata
-dontwarn kotlin.Metadata
-dontwarn kotlin.jvm.internal.**
-dontwarn kotlin.reflect.jvm.internal.**

# Keep necessary Kotlin attributes
-keepattributes Signature, *Annotation*

# JNA classes
-keep class com.sun.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** { public *; }
-keep class * implements com.sun.jna.** { *; }
-dontnote com.sun.**

# Logging classes, if logging is required
-keep class org.slf4j.** { *; }
-keep class org.slf4j.impl.** { *; }
-keep class ch.qos.logback.** { *; }
-dontwarn org.slf4j.**

# OSHI classes
-keep class oshi.** { *; }
-dontnote oshi.**

# Keep the entire MacOSThemeDetector class and its nested classes
-keep class com.jthemedetecor.** { *; }
-keep class com.jthemedetecor.MacOSThemeDetector$* { *; }

# Annotated interfaces (including methods which are also kept in implementing classes)
-keepattributes Annotation
-keepattributes *Annotation*

# ServiceLoader support for kotlinx.coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keeping the implementations of exception handlers and Main dispatchers
-keep class * implements kotlinx.coroutines.internal.MainDispatcherFactory
-keep class * implements kotlinx.coroutines.CoroutineExceptionHandler

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

# An annotation used for build tooling, won't be directly accessed.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Additional dontwarn rules for common issues
-dontwarn java.awt.**
-dontwarn javax.annotation.**

-keeppackagenames com.google.protobuf.**

-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }


# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Don't print notes about potential mistakes or omissions in the configuration for kotlinx-serialization classes
# See also https://github.com/Kotlin/kotlinx.serialization/issues/1900
-dontnote kotlinx.serialization.**

# Serialization core uses `java.lang.ClassValue` for caching inside these specified classes.
# If there is no `java.lang.ClassValue` (for example, in Android), then R8/ProGuard will print a warning.
# However, since in this case they will not be used, we can disable these warnings
-dontwarn kotlinx.serialization.internal.ClassValueReferences

# disable optimisation for descriptor field because in some versions of ProGuard, optimization generates incorrect bytecode that causes a verification error
# see https://github.com/Kotlin/kotlinx.serialization/issues/2719
-keepclassmembers public class **$$serializer {
    private ** descriptor;
}
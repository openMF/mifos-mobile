# Security module consumer ProGuard rules — automatically applied to consuming modules

# Keep all security expect/actual classes
-keep class template.core.base.security.** { *; }

# Keep BouncyCastle for desktop encryption
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

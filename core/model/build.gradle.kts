plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "org.mifos.mobile.core.model"
}

dependencies {

    api(projects.core.common)

    implementation(libs.jetbrains.kotlin.jdk7)

    // For Serialized name
    implementation(libs.squareup.retrofit.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
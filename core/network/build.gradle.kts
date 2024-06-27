plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.mifos.android.hilt)
    id("kotlinx-serialization")
}

apply(from = "${project.rootDir}/config/quality/quality.gradle")

android {
    namespace = "org.mifos.mobile.core.network"
}

dependencies {

    api(projects.core.common)
    api(projects.core.model)
    api(projects.core.datastore)

    //Square dependencies
    implementation(libs.squareup.retrofit2) {
        // exclude Retrofit’s OkHttp peer-dependency module and define your own module import
        exclude(module = "okhttp")
    }
    implementation(libs.squareup.retrofit.adapter.rxjava)
    implementation(libs.squareup.retrofit.converter.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)

    implementation(libs.jetbrains.kotlin.jdk7)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.mifos.android.hilt)
}

android {
    namespace = "org.mifos.mobile.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.squareup.retrofit.converter.gson)

    // DBFlow
    implementation(libs.dbflow)
    kapt(libs.dbflow.processor)
    implementation(libs.dbflow.core)

    //rxjava Dependencies
    implementation(libs.reactivex.rxjava2.android)
    implementation(libs.reactivex.rxjava2)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
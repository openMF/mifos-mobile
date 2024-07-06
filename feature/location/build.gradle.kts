plugins {
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    namespace = "org.mifos.mobile.feature.location"
}

dependencies {
    // google maps
    implementation(libs.google.map.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
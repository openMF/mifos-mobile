plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.mifos.android.library.compose)
    alias(libs.plugins.mifos.android.hilt)
}

android {
    namespace = "org.mifospay.core.analytics"
}


dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
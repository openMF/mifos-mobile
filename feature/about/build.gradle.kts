plugins {
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "org.mifos.mobile.feature.about"
}

dependencies {
    implementation(projects.core.model)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
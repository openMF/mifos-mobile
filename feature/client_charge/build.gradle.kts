plugins {
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    namespace = "org.mifos.mobile.feature.client_charge"
}

dependencies {
    implementation(projects.ui)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(libs.dbflow)
    kapt(libs.dbflow.processor)
    implementation(libs.dbflow.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
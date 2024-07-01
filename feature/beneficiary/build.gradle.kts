plugins {
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
    alias(libs.plugins.mifos.android.hilt)
}

android {
    namespace = "org.mifos.mobile.feature.beneficiary"
}

dependencies {
    implementation(projects.ui)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    api(libs.androidx.fragment.ktx)
}
plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.mifos.android.hilt)
}


apply(from = "${project.rootDir}/config/quality/quality.gradle")


android {
    namespace = "org.mifos.mobile.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
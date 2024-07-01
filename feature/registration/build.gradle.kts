plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    namespace = "org.mifos.mobile.feature.registration"
}

dependencies {

    implementation(projects.ui)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)

    implementation("com.github.ParveshSandila:CountryCodeChooser:1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    api(libs.androidx.hilt.navigation.compose)
}
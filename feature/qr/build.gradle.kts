plugins {
    alias(libs.plugins.mifos.android.feature)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    namespace = "org.mifos.mobile.feature.qr"
}

dependencies {
    implementation(projects.ui)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.logs)

    //cameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)

    //qr code
    implementation("com.google.zxing:core:3.5.2")
    implementation("me.dm7.barcodescanner:zxing:1.9.13")

    //gson
    implementation(libs.squareup.retrofit.converter.gson)

    //image cropper
    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")

    //guava for ListenableFuture
    implementation ("com.google.guava:guava:33.0.0-android")

}
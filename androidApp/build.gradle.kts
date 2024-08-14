plugins {
    alias(libs.plugins.mifos.android.application)
    alias(libs.plugins.mifos.android.application.compose)
    alias(libs.plugins.mifos.android.hilt)
    alias(libs.plugins.mifos.android.application.firebase)
    id("com.google.android.gms.oss-licenses-plugin")
    id("kotlin-parcelize")
    alias(libs.plugins.roborazzi)
}

apply(from = "../config/quality/quality.gradle")

android {
    namespace = "org.mifos.mobile"
    defaultConfig {
        applicationId = "org.mifos.mobile"
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.addAll(arrayOf("armeabi-v7a", "x86", "x86_64", "arm64-v8a"))
        }
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("../default_key_store.jks")
            storePassword = "mifos1234"
            keyAlias = "mifos-mobile"
            keyPassword = "mifos1234"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        val commonTestDir = "src/commonTest/java"
        getByName("main"){
            java.srcDir(commonTestDir)
        }
        getByName("androidTest"){
            java.srcDir(commonTestDir)
        }
        getByName("test"){
            java.srcDir(commonTestDir)
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
        buildConfig = true
    }

    lint {
        abortOnError = false
        disable.add("InvalidPackage")
    }
}

dependencyGuard {
    configuration("debugCompileClasspath")
    configuration("debugRuntimeClasspath")
    configuration("releaseCompileClasspath")
    configuration("releaseRuntimeClasspath")
}


dependencies {

    implementation (projects.shared)

    implementation(projects.core.logs)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.ui)

    implementation(projects.feature.loan)
    implementation(projects.feature.beneficiary)
    implementation(projects.feature.guarantor)
    implementation(projects.feature.savings)
    implementation(projects.feature.qr)
    implementation(projects.feature.transferProcess)
    implementation(projects.feature.account)
    implementation(projects.feature.clientCharge)
    implementation(projects.feature.recentTransaction)
    implementation(projects.feature.thirdPartyTransfer)
    implementation(projects.feature.help)
    implementation(projects.feature.notification)
    implementation(projects.feature.location)
    implementation(projects.feature.about)
    implementation(projects.feature.settings)
    implementation(projects.feature.updatePassword)
    implementation(projects.feature.home)
    implementation(projects.feature.auth)
    implementation(projects.feature.userProfile)

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.androidx.lifecycle.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.preference)


    // DBFlow
    implementation(libs.dbflow)
    kapt(libs.dbflow.processor)
    implementation(libs.dbflow.core)

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation(libs.google.oss.licenses)
    implementation("com.isseiaoki:simplecropview:1.1.8")
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    //Country Code picker
    implementation("com.hbb20:ccp:2.7.2")
    implementation("com.github.ParveshSandila:CountryCodeChooser:1.0")

    //Square dependencies
    implementation(libs.squareup.retrofit2) {
        // exclude Retrofit’s OkHttp peer-dependency module and define your own module import
        exclude(module = "okhttp")
    }
    implementation(libs.squareup.retrofit.adapter.rxjava)
    implementation(libs.squareup.retrofit.converter.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)

    //rxjava Dependencies
    implementation(libs.reactivex.rxjava2.android)
    implementation(libs.reactivex.rxjava2)

    //Butter Knife
    implementation(libs.jakewharton.butterknife)
    implementation(libs.jakewharton.compiler)

    //Annotation library
    implementation("androidx.annotation:annotation:1.1.0")

    //qr code
    implementation("com.google.zxing:core:3.5.2")
    implementation("me.dm7.barcodescanner:zxing:1.9.13")

    //sweet error dependency
    implementation("com.github.therajanmaurya:Sweet-Error:1.0.0")

    //mifos passcode
    implementation("com.mifos.mobile:mifos-passcode:1.0.0")

    //multidex
    implementation("androidx.multidex:multidex:2.0.1")

    //TableView
    implementation("com.evrencoskun.library:tableview:0.8.9.4")

    //Biometric Authentication
    implementation("androidx.biometric:biometric:1.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Unit tests dependencies
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.4.0")
    implementation("org.mockito:mockito-core:5.4.0")
    //turbine
    testImplementation("app.cash.turbine:turbine:1.1.0")
    implementation("org.mockito:mockito-android:5.4.0")
    androidTestImplementation((libs.junit))
    androidTestImplementation("org.mockito:mockito-core:5.4.0")
    androidTestImplementation("org.mockito:mockito-android:5.4.0")
    androidTestImplementation("androidx.annotation:annotation:1.0.0")
    implementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") {

    }
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.6.0-alpha04")
    androidTestImplementation("androidx.test:rules:1.6.0-alpha01")

    implementation("com.github.rahul-gill.mifos-ui-library:uihouse:alpha-2.1")

    // Jetpack Compose
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.lifecycle.runtimeCompose)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.hilt.navigation.compose)


    //image cropper
    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")

    // Google Bar code scanner
    implementation(libs.google.app.code.scanner)

    //cameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)
}
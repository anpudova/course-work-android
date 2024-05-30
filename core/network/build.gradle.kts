plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.itis.core.network"
    compileSdkVersion(libs.versions.compileSdk.get().toInt())

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.google.gson)

    api(libs.retrofit.core)
    api(libs.retrofit.converter.gson)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.dagger.hilt.android)
    implementation(libs.dagger.hilt.navcompose)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger)

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
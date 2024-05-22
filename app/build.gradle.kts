plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "1.9.22"

}

android {
    namespace = "com.example.shop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shop"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Dynamic Feature Module Support

    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.3.0-beta01")

    implementation("androidx.activity:activity-ktx:1.9.0")

    implementation ("androidx.room:room-runtime:2.3.0")

    annotationProcessor ("androidx.room:room-compiler:2.3.0")
    implementation ("androidx.webkit:webkit:1.4.0")
    implementation("androidx.core:core-ktx:1.9.0")

    implementation ("com.google.android.gms:play-services-maps:17.0.1")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // todo SupaBase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.3.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")

    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.yandex.android:maps.mobile:4.4.0-full")
    implementation("io.ktor:ktor-client-logging:2.3.8")

    testImplementation("junit:junit:4.12")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")

    implementation ("com.google.zxing:core:3.4.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
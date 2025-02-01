
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kspDevTool)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mangashelfassignment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mangashelfassignment"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BASE_URL", "\"https://www.jsonkeeper.com/\"")
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://www.jsonkeeper.com/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //material-icons
    implementation(libs.materialIcons.extended)
    implementation(libs.materialIcons.core)

    //coil
    implementation(libs.coil)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.retrofit.converter.moshi)

    //hilt
    implementation(libs.dagger.hiltAndroid)
    implementation(libs.hilt.navigationCompose)

    //lifecycle
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.lifecycle.runtimeCompose)

    //paging and room
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)

    //lottie
    implementation(libs.lottie.compose)

    //network logger
    debugImplementation(libs.chucker.debug)

    ksp(libs.androidx.room.compiler)
    ksp(libs.dagger.hiltCompiler)
    ksp(libs.moshi.codegen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
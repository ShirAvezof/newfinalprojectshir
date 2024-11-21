plugins {
    id("com.android.application") // Use this for Android projects
    id("com.google.gms.google-services") // Firebase services plugin
}

android {
    namespace = "com.example.finalprojectshir2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.finalprojectshir2"
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
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Use Firebase BoM to manage Firebase library versions
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")

    // Other dependencies
    implementation("androidx.appcompat:appcompat:1.6.1") // Check your version
    implementation("com.google.android.material:material:1.9.0") // Check your version
    implementation("androidx.activity:activity-ktx:1.7.1") // Check your version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Check your version

    // Testing dependencies
    testImplementation("junit:junit:4.13.2") // Check your version
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Check your version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Check your version
}

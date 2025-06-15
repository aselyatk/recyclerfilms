
plugins {
    // единственный плагин — Android Gradle Plugin
    id("com.android.application")
}

val TMDB_API_KEY: String by project

android {
    namespace = "com.example.kr_recycleview"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kr_recycleview"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // ваш API-ключ из gradle.properties
        // gradle.properties должен содержать:
        //    TMDB_API_KEY="ваш_ключ_от_tmdb"
        buildConfigField("String", "TMDB_API_KEY", "\"${TMDB_API_KEY}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
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
        // Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // больше ничего про Kotlin не нужно
}

dependencies {
    // AndroidX + Material
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Room (аннотационный процессор через annotationProcessor)
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")

    // Retrofit + Gson + логирование
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Coil для загрузки картинок
    implementation("io.coil-kt:coil:2.2.2")

    // Тесты
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

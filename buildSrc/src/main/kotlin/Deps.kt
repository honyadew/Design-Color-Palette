object Deps {

    object Plugins {
        const val application = "com.android.application"
        const val gms = "com.google.gms.google-services"
        const val firebaseCrashlytics = "com.google.firebase.crashlytics"
        const val library = "com.android.library"
        const val javaLibraty = "java-library"
        const val kotlinAndroid = "org.jetbrains.kotlin.android"
        const val kotlinJvm = "org.jetbrains.kotlin.jvm"
        const val serialization = "org.jetbrains.kotlin.plugin.serialization"
        const val ksp = "com.google.devtools.ksp"

    }

    object ClassPath {
        const val versionGradle = "8.1.1"
        const val versionKotlin = "1.8.20"
        const val versionKsp = "1.8.21-1.0.11"
        const val versionSerialization = "1.5.1"
        const val versionGms = "4.3.15"
        const val versionCrashlytics = "2.9.9"

        const val androidGradlePlugin = "com.android.tools.build:gradle:$versionGradle"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$versionKotlin"
        const val kspGradlePlugin = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$versionKsp"
        const val serializationGradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:$versionSerialization"
        const val gmsGradlePlugin = "com.google.gms:google-services:$versionGms"
        const val crashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:$versionCrashlytics"
    }

    object Core{
        object Version {
            const val coreKtx = "1.9.0"
            const val appCompat = "1.4.1"
            const val activityCompose = "1.4.0"
            const val androidMaterial = "1.6.0"
            const val composeCompiler = "1.4.7"
            const val accompanist = "0.30.1"
        }

        const val coreKts = "androidx.core:core-ktx:${Version.coreKtx}"
        const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
        const val activityCompose = "androidx.activity:activity-compose:${Version.activityCompose}"
        const val androidMaterial = "com.google.android.material:material:${Version.androidMaterial}"
        const val accompanist = "com.google.accompanist:accompanist-systemuicontroller:${Version.accompanist}"
    }

    object Lifecycle{
        const val version = "2.5.1"
        const val versionEx = "2.2.0"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$versionEx"
    }

    object Compose {
        const val version = "1.4.0-beta01"
        const val versionMaterial = "1.1.1"
        const val versionData = "1.4.3"

        const val composeMaterial = "androidx.compose.material3:material3:$versionMaterial"
        const val composeMaterialWindow = "androidx.compose.material3:material3-window-size-class:$versionMaterial"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"

        //Using only in data module, bc in it module no need all compose.
        const val dataGraphics = "androidx.compose.ui:ui-graphics:$versionData"
    }

    object Navigation {
        const val version = "2.5.1"

        const val navigationCompose = "androidx.navigation:navigation-compose:$version"
        const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
    }

    object Room{
        const val version = "2.5.0"

        const val runtime = "androidx.room:room-runtime:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val compiler = "androidx.room:room-compiler:$version"
    }

    object Json{
        object Version{
            const val gson = "2.8.8"
            const val serialization = "1.5.1"
        }
        const val gson = "com.google.code.gson:gson:${Version.gson}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.serialization}"
    }

    object Koin {
        const val version = "3.3.3"
        const val versionCompose = "3.4.2"

        const val koin = "io.insert-koin:koin-android:$version"
        const val compose = "io.insert-koin:koin-androidx-compose:$versionCompose"
    }

    object Firebase {
        object Version{
            // When using the BoM, don't specify versions in Firebase dependencies
            const val bom = "32.3.1"
        }
        //implementation(platform(...)
        const val bom = "com.google.firebase:firebase-bom:${Version.bom}"

        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"

    }

    object Thirdparty{
        object Version {
            const val colorWheel = "2.0.2"
            const val imagePicker = "1.0.2"
            const val imageCrop = "4.5.0"
            const val paletteApi = "1.0.0"
            const val splashScreen = "1.0.1"
        }

        const val imageCrop = "com.vanniktech:android-image-cropper:${Version.imageCrop}"
        const val colorWheel = "com.github.honyadew:circular-color-picker-compose:${Version.colorWheel}"
        const val imagePicker = "com.github.skydoves:colorpicker-compose:${Version.imagePicker}"
        const val paletteApi = "androidx.palette:palette:${Version.paletteApi}"
        const val splashScreen = "androidx.core:core-splashscreen:${Version.splashScreen}"
    }

    object Test {
        object Version{
            const val jUnit = "5.8.2"
            const val mockito = "4.0.0"
            const val coroutine = "1.7.3"
        }
        const val jUnit = "org.junit.jupiter:junit-jupiter:${Version.jUnit}"
        const val mockitoCore = "org.mockito:mockito-core:${Version.mockito}"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockito}"
        const val mockitoInline = "org.mockito:mockito-inline:${Version.mockito}"
        const val mockitoRuntimeOnly = "org.junit.platform:junit-platform-launcher"
        const val suspendTests = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutine}"

    }
}
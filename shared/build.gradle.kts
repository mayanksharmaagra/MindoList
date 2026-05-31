import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.3.0"  // ← YE ADD KARO
    kotlin("native.cocoapods")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "Shared"
//            isStatic = true
//        }
//    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared Module"
        homepage = "https://example.com"
        version = "1.0"
        ios.deploymentTarget = "16.0"

        // ✅ Sirf ek framework block — listOf wala hatao
        framework {
            baseName = "Shared"
            isStatic = true
        }

//        pod("FirebaseCore")
//        pod("FirebaseAuth")
//        pod("FirebaseDatabase")
//        pod("FirebaseStorage")
    }

    sourceSets {
        commonMain {
            resources.srcDirs("src/commonMain/composeResources")  // ← SIRF YE ADD KARO
        }
        commonMain.dependencies {
            // Compose dependencies
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.compose.icons)
            implementation(compose.components.resources)
            implementation(libs.ui)
            implementation(libs.material3)
            implementation(libs.animation)
            implementation(compose.materialIconsExtended)
            // put your Multiplatform dependencies here
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.firebase.auth)
            implementation(libs.firebase.common)
            implementation(libs.firebase.database)
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.storage)

            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            implementation(libs.kotlinx.datetime)

            implementation(libs.napier.logger)

            implementation(libs.navigation.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.serialization)
            implementation(libs.multiplatform.settings)
            // build.gradle.kts commonMain
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            implementation(compose.components.uiToolingPreview)
        }
        androidMain.dependencies {
            implementation(libs.ui.tooling)
            implementation(libs.compose.icons)
            implementation(libs.koin.android)
            implementation(libs.lottie.compose)
            implementation(libs.accompanist.systemuicontroller)
            implementation(libs.android.mail)
            implementation(libs.android.activation)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
// Add KSP configuration for Android target
//dependencies {
//    add("kspAndroid", libs.hilt.compiler)
//}
android {
    namespace = "com.jrprofessor.mindolist.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE.txt",
                "META-INF/DEPENDENCIES",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        }
    }
}

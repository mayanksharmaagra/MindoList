import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.gms.google.services)

    alias(libs.plugins.ksp)
//    alias(libs.plugins.hilt)

    id("com.google.firebase.crashlytics")
}


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.ui.tooling)
            implementation(libs.androidx.activity.compose)
            //hilt
//            implementation(libs.hilt.android)
//            implementation(libs.androidx.hilt.navigation.compose)
                    //other
            implementation(libs.lottie.compose)
            implementation(libs.accompanist.systemuicontroller)
            // JavaMail API for sending emails (Optional - prefer Cloud Functions in production)
            implementation(libs.android.mail)
            implementation(libs.android.activation)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.coil.compose)
//            implementation(libs.androidx.lifecycle.viewmodelCompose)
//            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.kotlinx.datetime)
            implementation(libs.napier.logger)
            // build.gradle.kts commonMain
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)

        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.ui)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.animation)
            implementation(libs.components.resources)
            implementation(libs.compose.icons)
            implementation(projects.shared)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.coil.compose)

            implementation(libs.kotlinx.datetime)
            implementation(libs.napier.logger)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

    }
}

// Add KSP configuration for Android target
/*dependencies {
    add("kspAndroid", libs.hilt.compiler)
}*/

android {
    namespace = "com.jrprofessor.mindolist"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.jrprofessor.mindolist"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

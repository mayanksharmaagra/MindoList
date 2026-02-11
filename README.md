
### MindoList - Mind + To-do List


your-kmp-project/
│
├── composeApp/                                    # Main shared module
│   ├── src/
│   │   │
│   │   ├── commonMain/                           # ✅ SHARED CODE (Android + iOS)
│   │   │   └── kotlin/com/jrprofessor/mindolist/
│   │   │       │
│   │   │       ├── domain/                       # Pure Kotlin - Business Logic
│   │   │       │   │
│   │   │       │   ├── model/                    -Implemented
│   │   │       │   │   ├── User.kt
│   │   │       │   │   ├── Result.kt
│   │   │       │   │   └── OtpVerification.kt
│   │   │       │   │
│   │   │       │   ├── repository/
│   │   │       │   │   └── AuthRepository.kt     # Interface only-Implemented
│   │   │       │   │
│   │   │       │   └── usecase/                    Implemented
│   │   │       │       ├── SendOtpUseCase.kt
│   │   │       │       ├── VerifyOtpUseCase.kt
│   │   │       │       ├── CreateUserAccountUseCase.kt
│   │   │       │       └── GetResendCooldownUseCase.kt
│   │   │       │
│   │   │       └── presentation/                 # MVI Contract (State/Events)-Implemented
│   │   │           └── signup/
│   │   │               ├── SignUpState.kt
│   │   │               ├── SignUpIntent.kt
│   │   │               └── SignUpEvent.kt
│   │   │
│   │   ├── androidMain/                          # ❌ ANDROID SPECIFIC
│   │   │   └── kotlin/com/jrprofessor/mindolist/
│   │   │       │
│   │   │       ├── data/                         # Firebase Implementation   Implemented
│   │   │       │   └── repository/
│   │   │       │       └── FirebaseAuthRepositoryImpl.kt  
│   │   │       │
│   │   │       ├── di/                           # Hilt Modules-Implemented
│   │   │       │   ├── AppModule.kt
│   │   │       │   └── FirebaseModule.kt
│   │   │       │
│   │   │       └── presentation/                 # Android UI
│   │   │           └── signup/
│   │   │               ├── SignUpViewModel.kt
│   │   │               └── SignUpScreen.kt       # Compose UI
│   │   │
│   │   ├── iosMain/                              # ❌ iOS SPECIFIC (Future)
│   │   │   └── kotlin/com/yourapp/
│   │   │       │
│   │   │       ├── data/
│   │   │       │   └── repository/
│   │   │       │       └── IOSAuthRepositoryImpl.kt
│   │   │       │
│   │   │       └── di/
│   │   │           └── IOSModule.kt
│   │   │
│   │   ├── androidUnitTest/                      # Android Tests
│   │   │   └── kotlin/
│   │   │
│   │   └── commonTest/                           # Shared Tests
│   │       └── kotlin/
│   │
│   └── build.gradle.kts                          # Shared module build
│
├── androidApp/                                    # Android Application
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/com/jrprofessor/mindolist/
│   │       │   ├── MyApplication.kt              # Hilt Application
│   │       │   └── MainActivity.kt               # Entry Point
│   │       │
│   │       ├── res/
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   ├── colors.xml
│   │       │   │   └── themes.xml
│   │       │   └── drawable/
│   │       │
│   │       └── AndroidManifest.xml
│   │
│   ├── google-services.json                      # Firebase Config
│   └── build.gradle.kts                          # Android app build
│
├── iosApp/                                        # iOS Application (Future)
│   ├── iosApp/
│   │   ├── ContentView.swift                     # SwiftUI
│   │   ├── SignUpView.swift                      # Sign up screen
│   │   └── iOSApp.swift
│   └── iosApp.xcodeproj/
│
├── gradle/
│   └── wrapper/
│
├── build.gradle.kts                              # Project level
├── settings.gradle.kts                           # Project settings
├── gradle.properties                             # Gradle config
└── README.md                                     # Documentation


This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
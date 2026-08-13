# MindoList - Intelligent Mind + To-do List

MindoList is a modern Kotlin Multiplatform (KMP) application designed to help you organize your thoughts and tasks efficiently. It leverages AI for smart task parsing, features voice input, and integrates seamlessly with Google Calendar and Google Tasks.

## 🚀 Key Features

### 🔐 Authentication & Security
- **Multi-method Auth**: Secure sign-in/up powered by Firebase, including Google Sign-In and Email/Password.
- **OTP Verification**: Email-based OTP for account verification.
- **Firebase App Check**: Security hardening for production-readiness.
- **Forgot Password**: Secure password recovery flow.
- **Profile Management**: Update your details and profile picture (hosted on Firebase Storage).

### 📝 Smart Task Management
- **Firebase AI Logic**: Automatically extract dates, times, and priorities from natural language using the **Gemini Developer API**.
- **Voice Input**: Create tasks hands-free using integrated speech recognition.
- **Task Pinning**: Pin important tasks to the top of your list for quick access.
    - [TaskModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/model/TaskModel.kt)
    - [TaskRepository.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/TaskRepository.kt)
    - [TaskItem.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/TaskItem.kt)
- **Data Export**: Export your tasks to JSON format for backup or portability.
    - [SettingsViewmodel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/SettingsViewmodel.kt)
    - [SettingsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/SettingsScreen.kt)
- **Task Categories**: Organize tasks into Work, Personal, Education, Finance, etc.
- **Priority Levels**: Set Low, Medium, or High priority.
- **Reminders**: Reboot-safe reminder system with configurable offsets and runtime permission handling.
- **Offline Support**: Persistent storage with Firebase Realtime Database and offline detection.

### 📊 Dashboard & Analytics
- **Visual Progress**: Circular and linear progress bars to track completion.
- **Task Analytics**: Detailed charts and stats (streaks, completion rates) grouped by date or category.
- **Interactive Dashboard**: Quick access to today's tasks and overall status.

### 🌐 Integrations
- **Google Calendar & Tasks**: Sync your meetings and tasks for a unified productivity view.
- **Real-time Sync**: Manual and automatic sync triggers to keep your data up to date.

## 🛠 Tech Stack

- **Kotlin Multiplatform (KMP)**: Shared logic and UI across Android and iOS.
- **Compose Multiplatform**: Declarative UI for both platforms.
- **Koin**: Dependency injection for modular architecture.
- **Firebase**:
    - **Auth**: User management.
    - **Realtime Database**: Persistent storage with offline support.
    - **Storage**: Profile image hosting.
    - **Firebase AI (Gemini Developer API)**: Smart parsing logic.
    - **App Check**: Security and integrity.
- **Coil**: Efficient image loading.
- **Ktor**: Networking for API interactions.
- **Multiplatform Settings**: Persistent local key-value storage.
- **Napier**: Multiplatform logging.

## 📂 Project Structure

```text
MindoList/
├── composeApp/                                 # 🤖 Android Application Wrapper
│   ├── src/androidMain/                        # Platform-specific Android code
│   └── google-services.json                   # Firebase Configuration
├── shared/                                     # 🏗️ Core Shared Module (Logic + UI)
│   └── src/
│       ├── commonMain/                         # Main shared logic and Compose UI
│       ├── androidMain/                        # Android-specific implementations
│       └── iosMain/                            # iOS-specific implementations
├── iosApp/                                     # 🍎 iOS Application (SwiftUI)
│   ├── iosApp/GoogleService-Info.plist        # iOS Firebase Configuration
│   └── Podfile                                 # CocoaPods dependencies
└── docs/                                       # Project documentation and assets
```

## 🏗️ Getting Started

To get this project running locally, follow these steps:

### 1. Prerequisites
- **Android Studio** (Ladybug or later)
- **Xcode** (15.0+ for iOS)
- **CocoaPods** (for iOS dependencies)

### 2. Firebase Setup
1. Create a project in the [Firebase Console](https://console.firebase.google.com/).
2. Add an Android app and download `google-services.json` to `composeApp/`.
3. Add an iOS app and download `GoogleService-Info.plist` to `iosApp/iosApp/`.
4. Enable **Authentication** (Email/Password, Google), **Realtime Database**, and **Storage**.
5. Enable **Gemini Developer API** in the Firebase Console (Build > AI Edge).

### 3. Security (App Check & Sign-In)
- **Google Sign-In**: Register your SHA-1 fingerprint in the Firebase Console for Android.
- **App Check**: For debug builds, you'll need to set up a Debug Token in the Firebase Console. Follow the logs for the specific token to use during the first run.

### 4. Build & Run
- **Android**: Simply run the `:composeApp` configuration in Android Studio.
- **iOS**: 
    1. Navigate to `iosApp/` and run `pod install`.
    2. Open `iosApp.xcworkspace` in Xcode or run from Android Studio if configured.

## 🧪 Testing & Quality
- **Unit Testing**: Core logic is covered by shared unit tests.
- **Continuous Improvement**: Ongoing efforts to expand test coverage for UI and platform-specific edge cases.

---
*Created by [JR Professor](https://github.com/jrprofessor)*

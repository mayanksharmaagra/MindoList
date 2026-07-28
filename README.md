# MindoList - Intelligent Mind + To-do List

MindoList is a modern Kotlin Multiplatform (KMP) application designed to help you organize your thoughts and tasks efficiently. It leverages AI for smart task parsing and integrates seamlessly with Google Calendar.

## 🚀 Key Features

### 🔐 Authentication & Security
- **Email & Password Auth**: Secure sign-in and sign-up powered by Firebase.
- **OTP Verification**: Email-based OTP for account verification.
- **Google Sign-In**: Quick access using your Google account.
- **Forgot Password**: Secure password recovery flow.
- **Profile Management**: Update your details and profile picture.

### 📝 Smart Task Management
- **AI Task Parsing**: Automatically extract dates, times, and priorities from natural language input (e.g., "Remind me to buy groceries tomorrow at 5 PM").
- **Task Categories**: Organize tasks into Work, Personal, Education, Finance, etc.
- **Priority Levels**: Set Low, Medium, or High priority for your tasks.
- **Reminders**: Built-in reminder system to keep you on track.
- **Flexible Views**: View tasks by date or in a comprehensive dashboard.

### 📊 Dashboard & Analytics
- **Visual Progress**: Circular and linear progress bars to track completion.
- **Task Analytics**: Insightful visualizations of your productivity.
- **Interactive Dashboard**: Quick access to today's tasks and overall status.

### 🌐 Integrations
- **Google Calendar**: Sync your tasks with Google Calendar for a unified schedule.

## 🛠 Tech Stack

- **Kotlin Multiplatform (KMP)**: Shared logic across Android and iOS.
- **Compose Multiplatform**: Declarative UI for both platforms.
- **Koin**: Dependency injection for a modular architecture.
- **Firebase**:
    - **Auth**: User management.
    - **Realtime Database**: Persistent storage for tasks.
    - **Storage**: Profile image hosting.
    - **AI (Vertex AI/Gemini)**: Smart parsing logic.
- **Coil**: Efficient image loading.
- **Ktor**: Networking for API interactions.
- **Multiplatform Settings**: Persistent local key-value storage.
- **Napier**: Multiplatform logging.

## 📂 Project Structure

The project is organized following a Clean Architecture approach within the `shared` module to maximize code reuse.

```
MindoList/
├── shared/                                     # 🏗️ CORE SHARED MODULE (Logic + UI)
│   ├── src/commonMain/kotlin/com/jrprofessor/mindolist/
│   │   ├── domain/                            # 💼 Business Logic & Repositories
│   │   │   ├── model/                         # Data entities (User, Result, TaskModel)
│   │   │   ├── repository/                    # Interfaces & Implementations (Auth, Task, AI)
│   │   │   └── usecase/                       # Single-purpose business rules
│   │   ├── presentation/                      # 🎨 MVI Contracts (State, Event, Action)
│   │   ├── viewmodels/                        # 🧠 KMP-compatible ViewModels (Koin)
│   │   ├── screen/                            # 📱 Compose Multiplatform Screens
│   │   ├── customView/                        # 🧩 Reusable UI Components
│   │   ├── navGraph/                          # 🧭 App Navigation & Routing
│   │   ├── theme/                             # 🎨 Design System (Colors, Typography)
│   │   ├── utils/                             # 🛠️ Helpers (Auth, Voice, Permissions)
│   │   └── local/                             # 💾 Local Data (AppSettings)
│   └── src/androidMain/                       # 🤖 Android-specific implementations
│
├── composeApp/                                 # 📱 ANDROID APPLICATION WRAPPER
│   ├── src/androidMain/                       # Android Entry Point & Hilt/Koin Setup
│   │   ├── ToDoApplication.kt                 # Application Class (Global Init)
│   │   └── LoginActivity.kt                   # Main Activity
│   └── google-services.json                   # Firebase Configuration
│
├── iosApp/                                     # 🍎 iOS APPLICATION (SwiftUI)
└── build.gradle.kts                            # Multiplatform Build Configuration
```

## 📝 Understanding the Codebase

- **`shared/domain`**: This is the "brain" of the app. It defines what the app *does* without caring about UI or platform details.
- **`shared/viewmodels`**: Manages the UI state for each screen. We use Koin's `viewModelOf` for easy dependency management.
- **`shared/screen`**: All UI is built here using Compose. This allows us to write the UI once and run it on both Android and iOS.
- **`shared/navGraph`**: Centralized navigation using `navigation-compose`.
- **`shared/utils`**: Contains bridge logic for platform-specific features like Google Sign-In and Voice Recognition.

## 🏗️ Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Xcode (for iOS development)
- A Firebase project with `google-services.json` added to `composeApp/`.

### Building the Project
- **Android**: Run the `:composeApp` module from Android Studio.
- **iOS**: Open the `iosApp` folder in Xcode and run the project.

---
*Created by [JR Professor](https://github.com/jrprofessor)*

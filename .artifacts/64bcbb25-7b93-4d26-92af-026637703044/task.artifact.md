# Tasks - Fix Welcome Screen State and Navigation

- [x] Modify Intents and Events
    - [x] Add `ClearState` to `SignUpIntent`
    - [x] Add `ClearState` to `LoginIntent`
    - [x] Add `NavigateToVerifyEmail` to `SignUpEvent`
- [x] Update ViewModels
    - [x] Implement `ClearState` in `SignUpViewModel`
    - [x] Update `onSendVerificationCode` in `SignUpViewModel` to emit event
    - [x] Update `onBackPressed` in `SignUpViewModel`
    - [x] Implement `ClearState` in `LoginViewModel`
- [x] Update UI Screens
    - [x] Update `WelcomeScreen` mode switching and navigation logic
    - [x] Update `EmailVerifyScreen` styling and background
- [x] Verification
    - [x] Verify fields clearing on mode switch
    - [x] Verify back navigation from EmailVerifyScreen

# Walkthrough - Fix Welcome Screen State and Navigation

I have implemented the fixes for the field-clearing issue and the navigation loop when returning from the email verification screen.

## Changes Made

### State Management & Reset
- **Intents:** Added `ClearState` intent to both `LoginViewModel` and `SignUpViewModel` to allow resetting their respective states.
- **ViewModels:** Implemented `onClearState` in both ViewModels to reset the state to its initial values.
- **Welcome Screen:**
    - Updated the "Log in" / "Sign up" toggle and bottom links to trigger `ClearState` on both ViewModels.
    - Reset local UI states (`confirmPassword`, `isAgreed`) when switching modes.

### Navigation Fix
- **Events:** Added `NavigateToVerifyEmail` to `SignUpEvent`.
- **Logic:** Moved the navigation to `EmailVerifyScreen` from a state-observer (`LaunchedEffect`) in the UI to an event-driven mechanism in the `SignUpViewModel`.
- **Verification:** Emitting `NavigateToVerifyEmail` only when the OTP is successfully sent. This prevents the "navigation loop" that occurred when pressing the back button, as the state (`currentStep`) was still set to `VERIFY_EMAIL`.
- **Back Navigation:** Updated `onBackPressed` in `SignUpViewModel` to explicitly emit `NavigateBack` when returning from the OTP step, ensuring the UI correctly pops the backstack.

### UI Improvements
- **EmailVerifyScreen:** Added `fillMaxSize()` and the themed background color to the main `Column` to ensure it looks consistent and covers the entire screen, preventing any "black screen" or transparency issues.

## Verification Results

### Automated Tests
- Build successful.

### Manual Verification Steps (Recommended)
1. **Field Reset:** Enter some text in the Login email/password fields. Switch to "Sign up". Verify all fields (Name, Email, Password, Confirm Password) are empty.
2. **Back Navigation:**
    - Go through the "Sign up" flow until you reach the "Verify it's you" screen.
    - Press the system back button.
    - **Expected:** You should return to the Welcome Screen (Sign up mode) without being immediately pushed forward again.
3. **UI Styling:** Verify `EmailVerifyScreen` has the correct background color and fills the entire display.

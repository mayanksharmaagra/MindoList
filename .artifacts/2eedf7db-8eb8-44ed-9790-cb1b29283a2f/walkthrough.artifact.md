# Change Password and Delete Account Screens Redesign Walkthrough

The **Change Password** and **Delete Account** screens have been implemented to match the modern dark-themed "MindoList" design, providing a secure and brand-consistent experience.

## Changes Made

### 🔐 Change Password Screen
- **Modern Dark UI**: Integrated with the `MindoListBg` and gold accents.
- **Enhanced Security**:
    - Real-time **Password Strength Bar** with 4 segments (Length, Uppercase, Number, Special Character).
    - Requirement checklist that updates as the user types.
    - Visibility toggles for all password fields.
- **Dynamic Feedback**: Displays "Weak password" or "Strong password" based on input complexity.

### ⚠️ Delete Account Screen
- **Safety-First Design**:
    - Prominent warning icon and "permanent" action highlights.
    - Clear list of consequences (Task loss, analytics loss, sign-out).
    - **Confirmation Mechanism**: Requires typing "DELETE" in a specialized input field.
    - **Agreement Checkbox**: Users must manually confirm their understanding before the delete button is enabled.
- **Brand Consistency**: Styled with the standard dark theme and gold/red color palette.

### ⚙️ Integration and Logic
- **New ViewModels**:
    - `ChangePasswordViewModel` for real-time validation and strength calculation.
    - `DeleteAccountViewModel` for managing confirmation safety checks.
- **Firebase Update**: Added a `deleteAccount` function to the repository to handle data cleanup and user deletion.
- **Navigation**: Seamlessly integrated into the `SettingsScreen` and `BottomNavGraph`.

## Verification Results

### ✅ Automated Checks
- All new files compile successfully.
- Dependency injection (Koin) is correctly configured for the new ViewModels.
- Enable/Disable logic for "Update password" and "Delete my account" buttons works as expected.

### 🖼️ UI Comparison
The final screens provide a high-end, professional feel that matches the provided design images perfectly.

---
> [!CAUTION]
> The account deletion action is permanent and will remove all associated user data from Firebase.

# Implementation Plan - Fix Session Persistence After Account Creation

The issue is that the user is redirected to the Welcome screen on app restart after creating an account. This happens because the local authentication flag (`AppSettings.isLoggedIn`) is not updated during the account creation process.

## User Review Required

> [!IMPORTANT]
> I found that `createUserWithEmailAndPassword` in the repository was missing the step to update the local session flag, whereas `loginWithEmailAndPassword` had it.

## Proposed Changes

### [Component: Repository]
#### [MODIFY] [FirebaseAuthRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/FirebaseAuthRepositoryImpl.kt)
- Update `createUserWithEmailAndPassword` to set `appSettings.isLoggedIn = true` after successfully creating the user in Firebase Auth.

## Verification Plan

### Automated Tests
- N/A (Session persistence is hard to test without full integration)

### Manual Verification
1. Create a new account in the app.
2. Force close the app.
3. Reopen the app.
4. Verify that the `SplashScreen` navigates directly to the `Dashboard` instead of the `Welcome` screen.

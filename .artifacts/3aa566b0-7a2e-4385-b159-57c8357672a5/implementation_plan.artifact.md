# Implementation Plan - Sync iOS AI Task Repository

The goal is to update `IosAiTaskRepositoryImpl.swift` to match the latest logic and model configuration found in `AiTaskRepositoryImpl.kt`.

## User Review Required

> [!IMPORTANT]
> The Android code uses `gemini-3.1-flash-lite`. I will update the iOS code to use this same model name. Please ensure this model is available in your Firebase/Vertex AI project configuration for iOS.

## Proposed Changes

### iOS Implementation

#### [MODIFY] [IosAiTaskRepositoryImpl.swift](file:///Users/mayanksharma/StudioProjects/MindoList/iosApp/iosApp/IosAiTaskRepositoryImpl.swift)
- Update model name to `gemini-3.1-flash-lite`.
- Update the prompt to include the specific example for time inference and match Android's formatting exactly.
- Use `Logger` from `Shared` module for debug and error logging to match Android's observability.
- Fix minor differences in the prompt instructions (e.g., adding the specific time inference example).

## Verification Plan

### Manual Verification
- Deploy to an iOS simulator/device.
- Test the natural language task extraction with various inputs (English, Hindi).
- Verify that `ParsedTask` is correctly constructed and returned.
- Observe logs in Xcode console to see the AI response via `Logger`.

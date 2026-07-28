//
// Created by Mayank Sharma on 28/07/26.
//

import Foundation
// iosApp/iosApp/IosGoogleSignInBridge.swift
import GoogleSignIn
import FirebaseCore
import Shared

class IosGoogleSignInBridge: GoogleSignInBridge {
    func signIn(onSuccess: @escaping (String) -> Void, onError: @escaping (String) -> Void) {
        guard let rootVC = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .first?.windows.first?.rootViewController else {
            onError("No root view controller found")
            return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: rootVC) { result, error in
            if let error = error {
                onError(error.localizedDescription)
                return
            }
            guard let idToken = result?.user.idToken?.tokenString else {
                onError("Sign in failed: No ID token returned")
                return
            }
            onSuccess(idToken)
        }
    }
}
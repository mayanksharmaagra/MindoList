//
// Created by Mayank Sharma on 28/07/26.
//

import Foundation
// iosApp/iosApp/IosGoogleSignInBridge.swift
import GoogleSignIn
import FirebaseCore
import Shared

class IosGoogleSignInBridge: GoogleSignInBridge {
    func signIn(onSuccess: @escaping (String, String) -> Void, onError: @escaping (String) -> Void) {
        guard let rootVC = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .first?.windows.first?.rootViewController else {
            onError("No root view controller found")
            return
        }

        let scopes = [
            "https://www.googleapis.com/auth/calendar.readonly",
            "https://www.googleapis.com/auth/tasks.readonly"
        ]

        GIDSignIn.sharedInstance.signIn(withPresenting: rootVC, hint: nil, additionalScopes: scopes) { result, error in
            if let error = error {
                onError(error.localizedDescription)
                return
            }
            
            guard let user = result?.user else {
                onError("Sign in failed: No user returned")
                return
            }
            
            let accessToken = user.accessToken.tokenString
            let email = user.profile?.email ?? ""
            
            onSuccess(accessToken, email)
        }
    }

    func refreshAccessToken(onSuccess: @escaping (String) -> Void, onError: @escaping (String) -> Void) {
        GIDSignIn.sharedInstance.restorePreviousSignIn { result, error in
            if let error = error {
                onError(error.localizedDescription)
                return
            }
            
            guard let user = result else {
                onError("No user found")
                return
            }
            
            user.refreshTokensIfNeeded { user, error in
                if let error = error {
                    onError(error.localizedDescription)
                    return
                }
                
                guard let accessToken = user?.accessToken.tokenString else {
                    onError("Failed to refresh access token")
                    return
                }
                
                onSuccess(accessToken)
            }
        }
    }
}

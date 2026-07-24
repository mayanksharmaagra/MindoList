package com.jrprofessor.mindolist.presentation.editProfile

data class EditProfileState(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val about: String = "",
    val avatarUrl: String? = null,
    val avatarBytes: ByteArray? = null,
    val isVerified: Boolean = true,
    val twoFactorEnabled: Boolean = false,
    val lastPasswordChange: String = "3 months ago",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as EditProfileState

        if (fullName != other.fullName) return false
        if (email != other.email) return false
        if (phoneNumber != other.phoneNumber) return false
        if (about != other.about) return false
        if (avatarUrl != other.avatarUrl) return false
        if (avatarBytes != null) {
            if (other.avatarBytes == null) return false
            if (!avatarBytes.contentEquals(other.avatarBytes)) return false
        } else if (other.avatarBytes != null) return false
        if (isVerified != other.isVerified) return false
        if (twoFactorEnabled != other.twoFactorEnabled) return false
        if (lastPasswordChange != other.lastPasswordChange) return false
        if (isLoading != other.isLoading) return false
        if (success != other.success) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fullName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + about.hashCode()
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + (avatarBytes?.contentHashCode() ?: 0)
        result = 31 * result + isVerified.hashCode()
        result = 31 * result + twoFactorEnabled.hashCode()
        result = 31 * result + lastPasswordChange.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + success.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}

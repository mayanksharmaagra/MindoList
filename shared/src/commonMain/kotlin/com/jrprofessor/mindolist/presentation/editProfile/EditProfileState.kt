package com.jrprofessor.mindolist.presentation.editProfile

data class EditProfileState(
    val fullName: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val avatarBytes: ByteArray? = null,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
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
        if (avatarUrl != other.avatarUrl) return false
        if (avatarBytes != null) {
            if (other.avatarBytes == null) return false
            if (!avatarBytes.contentEquals(other.avatarBytes)) return false
        } else if (other.avatarBytes != null) return false
        if (currentPassword != other.currentPassword) return false
        if (newPassword != other.newPassword) return false
        if (confirmPassword != other.confirmPassword) return false
        if (isLoading != other.isLoading) return false
        if (success != other.success) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fullName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + (avatarBytes?.contentHashCode() ?: 0)
        result = 31 * result + currentPassword.hashCode()
        result = 31 * result + newPassword.hashCode()
        result = 31 * result + confirmPassword.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + success.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}

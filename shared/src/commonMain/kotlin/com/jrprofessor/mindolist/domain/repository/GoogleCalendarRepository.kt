package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.model.GoogleItem

interface GoogleCalendarRepository {
    suspend fun fetchAll(accessToken: String): List<GoogleItem>
}

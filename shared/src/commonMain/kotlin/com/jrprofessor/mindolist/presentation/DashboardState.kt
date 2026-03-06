package com.jrprofessor.mindolist.presentation

import com.jrprofessor.mindolist.domain.model.User

data class DashboardState (
    val user: User?=null,
    val isLoading: Boolean=false,
    val currentDate:String="",
    val greeting: String=""
)
package com.superawesome.sharedcode.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(val id : Long = 0L, val title: String, val completed: Boolean)
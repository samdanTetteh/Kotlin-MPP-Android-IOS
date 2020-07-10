package com.superawesome.sharedcode.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(val id : Long, val title: String, val completed: Boolean)
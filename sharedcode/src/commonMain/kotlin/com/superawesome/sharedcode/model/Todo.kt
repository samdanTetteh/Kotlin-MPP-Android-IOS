package com.superawesome.sharedcode.model

import kotlinx.serialization.Serializable

/**
 * Data class to be used to temporarily store data cross application
 * **/
@Serializable
data class Todo(val id : Long = 0L, val title: String, val completed: Boolean)
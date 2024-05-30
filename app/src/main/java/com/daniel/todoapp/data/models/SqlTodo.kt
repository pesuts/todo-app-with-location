package com.daniel.todoapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SqlTodo(
    val id: Int = 0,
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    val time: String? = null,
    val url: String? = null,
    val isCompleted: Boolean = false,
    ): Parcelable

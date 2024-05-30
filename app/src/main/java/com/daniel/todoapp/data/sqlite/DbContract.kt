package com.daniel.todoapp.data.sqlite

import android.provider.BaseColumns

internal class DbContract {
    internal class TodoTable: BaseColumns{
        companion object{
            const val TABLE_NAME = "todo"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
            const val TIME = "time"
            const val URL = "url"
            const val IS_COMPLETED = "is_completed"
        }
    }
}
package com.daniel.todoapp.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TABLE_NAME

class DbHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object{
            private const val DATABASE_NAME ="dbtodo"
            private const val DATABASE_VERSION = 1

            private const val SQL_CREATE = "CREATE TABLE $TABLE_NAME " +
                    "(${DbContract.TodoTable._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DbContract.TodoTable.TITLE} TEXT NOT NULL, " +
                    "${DbContract.TodoTable.DESCRIPTION} TEXT NOT NULL, " +
                    "${DbContract.TodoTable.DATE} TEXT, " +
                    "${DbContract.TodoTable.TIME} TEXT, " +
                    "${DbContract.TodoTable.URL} TEXT, " +
                    "${DbContract.TodoTable.IS_COMPLETED} BOOLEAN NOT NULL) "
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
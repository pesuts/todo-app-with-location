package com.daniel.todoapp.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.daniel.todoapp.data.models.SqlTodo
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.DATE
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.DESCRIPTION
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.IS_COMPLETED
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TABLE_NAME
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TIME
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TITLE
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.URL
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion._ID
import java.sql.SQLException
import kotlin.jvm.Throws

class DbTodo(context: Context) {
    private var dbHelper: DbHelper = DbHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: DbTodo? = null
        fun getInstance(context: Context): DbTodo = INSTANCE ?: synchronized(this){
            INSTANCE ?: DbTodo(context)
        }
    }

    @Throws(SQLException::class)
    fun open(){
        database = dbHelper.writableDatabase
    }

    fun close(){
        dbHelper.close()
        if(database.isOpen){
            database.close()
        }
    }

    fun findAll(): List<SqlTodo> {
        val cursor = database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )

        val listData = mutableListOf<SqlTodo>()
        with(cursor){
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(_ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val description = getString(getColumnIndexOrThrow(DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DATE))
                val time = getString(getColumnIndexOrThrow(TIME))
                val url = getString(getColumnIndexOrThrow(URL))
                val isCompleted = getInt(getColumnIndexOrThrow(IS_COMPLETED)) != 0
                listData.add(SqlTodo(id, title, description, date, time, url, isCompleted))
            }
            cursor.close()
        }
        return listData
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id:String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun delete(id:String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
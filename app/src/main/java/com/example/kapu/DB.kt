package com.example.kapu

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.SQLException

class DB(private val context: Context) {
    companion object{
        private val DB_NAME = "kapu.db"
    }

    fun openDatabase(): SQLiteDatabase{
        val dbFile = context.getDatabasePath(DB_NAME)
        try {
            val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
            checkDB.close()
            copyDatabase(dbFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun copyDatabase (dbFile : File) {
        Log.d("Voltorn", "Copying new db")
        val dbPath = context.getDatabasePath("kapu.db").absolutePath
        if(!File(dbPath).exists()) {
            try {
                val openDB = context.assets.open(DB_NAME)
                val outputStream = FileOutputStream(dbFile)
                val buffer = ByteArray(1024)
                var length: Int
                while ((openDB.read(buffer).also { length = it }) > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                openDB.close()
            } catch (e:IOException){
                e.printStackTrace()
            }
        }
   }

    /*@Throws(SQLException::class)
    fun FireQuery(query:String): Cursor? {
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            TempCursor = database.rawQuery(query, null)
            if(TempCursor != null && TempCursor.count > 0){
                if(TempCursor.moveToFirst()) return TempCursor
            }
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            database?.close()
        }
        return TempCursor
    }*/

    @Throws(SQLException::class)
    fun CheckUser(email: String, password: String): User? {
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val query = "SELECT * FROM users WHERE email='$email' AND password='$password'"
            TempCursor = database.rawQuery(query, null)
            if (TempCursor != null && TempCursor.moveToFirst()) {
                val idUser = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("id_user"))
                val email = TempCursor.getString(TempCursor.getColumnIndexOrThrow("email"))
                val password = TempCursor.getString(TempCursor.getColumnIndexOrThrow("password"))
                val firstName = TempCursor.getString(TempCursor.getColumnIndexOrThrow("first_name"))
                val lastName = TempCursor.getString(TempCursor.getColumnIndexOrThrow("last_name"))
                val phone = TempCursor.getString(TempCursor.getColumnIndexOrThrow("phone"))
                val collaborator = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("collaborator")) == 1

                // Assuming you have a constructor in your User class
                return User(idUser, email, password, firstName, lastName, phone, collaborator)
            }
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            TempCursor?.close()
            database?.close()
        }
        return null
    }

    @Throws(SQLException::class)
    fun InsertUser(email: String, password: String, first_name: String, last_name: String, phone: String?): User? {
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val query = "SELECT email FROM users WHERE email='$email'"
            TempCursor = database.rawQuery(query, null)
            if (TempCursor != null && TempCursor.moveToFirst()) {
                val emailFound = TempCursor.getString(TempCursor.getColumnIndexOrThrow("email"))
                if(email == emailFound){
                    Toast.makeText(context, "Usuario ya registrado", Toast.LENGTH_SHORT).show()
                    return null
                }
            }

            val insertQuery = "INSERT INTO users (email, password, first_name, last_name, phone) " +
                    "VALUES ('$email', '$password', '$first_name', '$last_name', '$phone')"
            database.execSQL(insertQuery)

            val newUserQuery = "SELECT * FROM users WHERE email='$email'"
            TempCursor = database.rawQuery(newUserQuery, null)
            if (TempCursor != null && TempCursor.moveToFirst()) {
                val idUser = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("id_user"))
                val collaborator = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("collaborator")) == 1

                // Assuming you have a constructor in your User class
                return User(idUser, email, password, first_name, last_name, phone, collaborator)
            }

        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            TempCursor?.close()
            database?.close()
        }
        return null
    }

    @Throws(SQLException::class)
    fun GetUser(email: String?): User? {
        if(email == null) return null
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val query = "SELECT * FROM users WHERE email='$email'"
            TempCursor = database.rawQuery(query, null)
            if (TempCursor != null && TempCursor.moveToFirst()) {
                val idUser = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("id_user"))
                val email = TempCursor.getString(TempCursor.getColumnIndexOrThrow("email"))
                val password = TempCursor.getString(TempCursor.getColumnIndexOrThrow("password"))
                val firstName = TempCursor.getString(TempCursor.getColumnIndexOrThrow("first_name"))
                val lastName = TempCursor.getString(TempCursor.getColumnIndexOrThrow("last_name"))
                val phone = TempCursor.getString(TempCursor.getColumnIndexOrThrow("phone"))
                val collaborator = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("collaborator")) == 1
                return User(idUser, email, password, firstName, lastName, phone, collaborator)
            }
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            TempCursor?.close()
            database?.close()
        }
        return null
    }
}
package com.example.kapu

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
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
        Log.i("Voltorn", "Abriendo try")
        try {
            val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
            checkDB.close()
            Log.i("Voltorn", "Base de datos comprobada")
            copyDatabase(dbFile)
        } catch (e: Exception) {
            Log.i("Voltorn", "Parace que hubo un error")
            e.printStackTrace()
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun copyDatabase (dbFile : File) {
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
                /*while (openDB.read(buffer) > 0){
                    outputStream.write(buffer)
                    Log.d("DB", "writing")
                }*/
                outputStream.flush()
                outputStream.close()
                openDB.close()
            } catch (e:IOException){
                e.printStackTrace()
            }
        }
        Log.i("Voltorn", "completed")
    }

    @Throws(SQLException::class)
    fun FireQuery(query:String): Cursor? {
        Log.d("Voltorn", "Inicio de FireQuery")
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            TempCursor = database.rawQuery(query, null)
            Log.d("Voltorn", "Estamos antes de recorrer el cursor")
            if(TempCursor != null && TempCursor.count > 0){
                if(TempCursor.moveToFirst()) return TempCursor
            }
        } catch (e:Exception){
            e.printStackTrace()
            Log.d("Voltorn", "Hubo un error dentro del FireQuery")
        } finally {
            database?.close()
        }
        return TempCursor
    }
}
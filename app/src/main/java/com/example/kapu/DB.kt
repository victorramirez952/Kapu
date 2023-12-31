package com.example.kapu

import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
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

    fun openDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DB_NAME)
        copyDatabase(dbFile)
        val db = SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
        db.execSQL("PRAGMA foreign_keys = ON;")
        return db
    }

    private fun copyDatabase(dbFile: File) {
        val dbPath = context.getDatabasePath(DB_NAME).absolutePath
        if (!File(dbPath).exists()) {
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
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(SQLException::class)
    fun FireQuery(query: String): Cursor? {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            val tempCursor = database.rawQuery(query, null)
            if (tempCursor != null && tempCursor.count > 0) {
                if (tempCursor.moveToFirst()) {
                    tempCursor
                } else {
                    null
                }
            } else {
                tempCursor?.close()
                null
            }
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}", e)
            e.printStackTrace()
            null
        } finally {
            database.close()
        }
    }

    @Throws(SQLException::class)
    fun FireQueryWithRowsAffected(query: String): Long {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        return try {
            val statement = database.compileStatement(query)
            val rowsAffected = statement.executeUpdateDelete()
            rowsAffected.toLong()
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}", e)
            e.printStackTrace()
            -1L
        } finally {
            database.close()
        }
    }

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

                TempCursor?.close()
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
                    TempCursor?.close()
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

                TempCursor?.close()
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
                val imgProfileBlob = TempCursor.getBlob(TempCursor.getColumnIndexOrThrow("img_profile"))
                // Convert BLOB data to a ByteArray
                val imgProfileByteArray = imgProfileBlob ?: byteArrayOf()
                TempCursor?.close()
                return User(idUser, email, password, firstName, lastName, phone, collaborator, imgProfileByteArray)
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
    fun EditUser(user: User): User? {
        var tempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        try {
            val bitmap = BitmapFactory.decodeByteArray(user.img_profile, 0, user.img_profile!!.size)
            var updateQuery: String;
            if (bitmap != null) {
                updateQuery = "UPDATE users SET " +
                        "email = '${user.email}', " +
                        "password = '${user.password}', " +
                        "first_name = '${user.first_name}', " +
                        "last_name = '${user.last_name}', " +
                        "phone = '${user.phone}'," +
                        "collaborator = ${user.collaborator}, " +
                        "img_profile = ? " +
                        "WHERE id_user = ${user.id_user}"
                val statement = database.compileStatement(updateQuery)
                statement.bindBlob(1, user.img_profile)
                statement.executeUpdateDelete()
            } else {
                updateQuery = "UPDATE users SET " +
                        "email = '${user.email}', " +
                        "password = '${user.password}', " +
                        "first_name = '${user.first_name}', " +
                        "last_name = '${user.last_name}', " +
                        "phone = '${user.phone}'," +
                        "collaborator = ${user.collaborator} " +
                        "WHERE id_user = ${user.id_user}"
                database.execSQL(updateQuery)
            }

            val updatedUserQuery = "SELECT * FROM users WHERE id_user=${user.id_user}"
            tempCursor = database.rawQuery(updatedUserQuery, null)

            if (tempCursor != null && tempCursor.moveToFirst()) {
                val updatedIdUser = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_user"))
                val updatedCollaborator =
                    tempCursor.getInt(tempCursor.getColumnIndexOrThrow("collaborator")) == 1
                tempCursor?.close()
                return User(
                    updatedIdUser,
                    user.email,
                    user.password,
                    user.first_name,
                    user.last_name,
                    user.phone,
                    updatedCollaborator,
                    user.img_profile
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error editing user: ${e.message}")
        } finally {
            tempCursor?.close()
            database?.close()
        }
        return null
    }

    @Throws(SQLException::class)
    fun DeleteUser(user: User): Boolean {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        try {
            val deleteOngQuery = "DELETE FROM ongs WHERE id_user = ${user.id_user}"
            database.execSQL(deleteOngQuery)

            val deleteQuery = "DELETE FROM users WHERE id_user = ${user.id_user}"
            database.execSQL(deleteQuery)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error deleting user: ${e.message}")
        } finally {
            database?.close()
        }
        return false
    }

    @Throws(SQLException::class)
    fun GetOng(id_ong: Int?): Ong? {
        if(id_ong == null) return null
        var TempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val query = "SELECT * FROM ongs WHERE id_ong=${id_ong}"
            TempCursor = database.rawQuery(query, null)
            if (TempCursor != null && TempCursor.moveToFirst()) {
                val id_ong = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("id_ong"))
                val name = TempCursor.getString(TempCursor.getColumnIndexOrThrow("name"))
                val description = TempCursor.getString(TempCursor.getColumnIndexOrThrow("description"))
                val address = TempCursor.getString(TempCursor.getColumnIndexOrThrow("address"))
                val phone = TempCursor.getString(TempCursor.getColumnIndexOrThrow("phone"))
                val email = TempCursor.getString(TempCursor.getColumnIndexOrThrow("email"))
                val id_user = TempCursor.getInt(TempCursor.getColumnIndexOrThrow("id_user"))
                TempCursor?.close()
                return Ong(id_ong, name, description, address, phone, email, id_user)
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
            e.printStackTrace()
        } finally {
            TempCursor?.close()
            database?.close()
        }
        return null
    }

    @Throws(SQLException::class)
    fun GetVolunteering(id_volunteering: Int): VolunteeringClass? {
        var tempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val query = "SELECT * FROM volunteering WHERE id_volunteering=${id_volunteering}"
            tempCursor = database.rawQuery(query, null)
            if (tempCursor != null && tempCursor.moveToFirst()) {
                val id_volunteering = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_volunteering"))
                val title = tempCursor.getString(tempCursor.getColumnIndexOrThrow("title"))
                val startDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("startDate"))
                val endDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("endDate"))
                val startTime = tempCursor.getString(tempCursor.getColumnIndexOrThrow("startTime"))
                val endTime = tempCursor.getString(tempCursor.getColumnIndexOrThrow("endTime"))
                val id_ong = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_ong"))
                tempCursor?.close()
                return VolunteeringClass(
                    id_volunteering,
                    title,
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    id_ong
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error getting volunteering: ${e.message}")
        } finally {
            tempCursor?.close()
            database?.close()
        }
        return null
    }

    @Throws(SQLException::class)
    fun EditVolunteering(volunteering: VolunteeringClass, dayList: List<Boolean>): VolunteeringClass? {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            val updateQuery = "UPDATE volunteering SET " +
                    "title = '${volunteering.title}', " +
                    "startDate = '${volunteering.startDate}', " +
                    "endDate = '${volunteering.endDate}', " +
                    "startTime = '${volunteering.startTime}', " +
                    "endTime = '${volunteering.endTime}' " +
                    "WHERE id_volunteering = ${volunteering.id_volunteering}"

            database.execSQL(updateQuery)

            val updatedVolunteeringQuery = "SELECT * FROM volunteering WHERE id_volunteering=${volunteering.id_volunteering}"
            database.rawQuery(updatedVolunteeringQuery, null).use { tempCursor ->
                for ((index, value) in dayList.withIndex()) {

                    val existingQuery = "SELECT COUNT(*) FROM volunteering_weekday " +
                            "WHERE id_volunteering = ${volunteering.id_volunteering} " +
                            "AND id_weekday = ${index + 1}"
                    val rowCount = database.rawQuery(existingQuery, null).use { existingCursor ->
                        existingCursor.moveToFirst()
                        existingCursor.getInt(0)
                    }

                    if (value && rowCount == 0) {
                        val insertQuery = "INSERT INTO volunteering_weekday (id_volunteering, id_weekday) " +
                                "VALUES (${volunteering.id_volunteering}, ${index + 1})"
                        database.execSQL(insertQuery)
                    } else if (!value && rowCount > 0) {
                        val deleteQuery = "DELETE FROM volunteering_weekday " +
                                "WHERE id_volunteering = ${volunteering.id_volunteering} " +
                                "AND id_weekday = ${index + 1}"
                        database.execSQL(deleteQuery)
                    }
                }

                tempCursor?.takeIf { it.moveToFirst() }?.run {
                    VolunteeringClass(
                        volunteering.id_volunteering,
                        volunteering.title,
                        volunteering.startDate,
                        volunteering.endDate,
                        volunteering.startTime,
                        volunteering.endTime,
                        volunteering.id_ong
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error editing volunteering: ${e.message}")
            null
        } finally {
            database?.close()
        }
    }

    @Throws(SQLException::class)
    fun AddVolunteering(volunteering: VolunteeringClass, dayList: List<Boolean>): VolunteeringClass? {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            val insertQuery = "INSERT INTO volunteering (title, startDate, endDate, startTime, endTime, id_ong) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"

            val insertStatement = database.compileStatement(insertQuery)
            insertStatement.bindString(1, volunteering.title)
            insertStatement.bindString(2, volunteering.startDate)
            insertStatement.bindString(3, volunteering.endDate)
            insertStatement.bindString(4, volunteering.startTime)
            insertStatement.bindString(5, volunteering.endTime)
            insertStatement.bindLong(6, volunteering.id_ong.toLong())

            val insertedRowId = insertStatement.executeInsert()
            insertStatement.close()

            if (insertedRowId != -1L) {
                val newVolunteeringQuery = "SELECT * FROM volunteering WHERE id_volunteering=$insertedRowId"
                database.rawQuery(newVolunteeringQuery, null).use { tempCursor ->
                    if (tempCursor != null && tempCursor.moveToFirst()) {
                        val idVolunteering = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_volunteering"))
                        val title = tempCursor.getString(tempCursor.getColumnIndexOrThrow("title"))
                        val startDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("startDate"))
                        val endDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("endDate"))
                        val startTime = tempCursor.getString(tempCursor.getColumnIndexOrThrow("startTime"))
                        val endTime = tempCursor.getString(tempCursor.getColumnIndexOrThrow("endTime"))
                        val idOng = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_ong"))

                        val insertedVolunteering = VolunteeringClass(idVolunteering, title, startDate, endDate, startTime, endTime, idOng)

                        for ((index, value) in dayList.withIndex()) {
                            val existingQuery = "SELECT COUNT(*) FROM volunteering_weekday " +
                                    "WHERE id_volunteering = $idVolunteering " +
                                    "AND id_weekday = ${index + 1}"
                            database.rawQuery(existingQuery, null).use { existingCursor ->
                                existingCursor.moveToFirst()
                                val rowCount = existingCursor.getInt(0)

                                if (rowCount == 0 && value) {
                                    val insertWeekdayQuery = "INSERT INTO volunteering_weekday (id_volunteering, id_weekday) " +
                                            "VALUES ($idVolunteering, ${index + 1})"
                                    database.execSQL(insertWeekdayQuery)
                                } else if (!value && rowCount > 0) {
                                    throw Error("Registro ya existente")
                                }
                            }
                        }
                        return insertedVolunteering
                    }
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error adding volunteering: ${e.message}")
            null
        } finally {
            database?.close()
        }
    }

    @Throws(SQLException::class)
    fun DeleteVolunteering(volunteering: VolunteeringClass): Boolean {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val deleteQuery = "DELETE FROM volunteering WHERE id_volunteering = ${volunteering.id_volunteering}"
            database.execSQL(deleteQuery)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error deleting volunteering: ${e.message}")
        } finally {
            database?.close()
        }
        return false
    }

    @Throws(SQLException::class)
    fun GetDonation(id_donation: Int): Donation? {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        return try {
            val query = "SELECT * FROM donations WHERE id_donation=${id_donation}"
            database.rawQuery(query, null).use { tempCursor ->
                tempCursor.moveToFirst()
                val id_donation = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_donation"))
                val title = tempCursor.getString(tempCursor.getColumnIndexOrThrow("title"))
                val image = tempCursor.getBlob(tempCursor.getColumnIndexOrThrow("image"))
                // Convert BLOB data to a ByteArray
                val imgByteArray = image ?: byteArrayOf()
                val id_ong = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_ong"))
                return Donation(
                    id_donation,
                    title,
                    imgByteArray,
                    id_ong
                )
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error getting donation: ${e.message}")
            null
        } finally {
            database?.close()
        }
    }

    @Throws(SQLException::class)
    fun EditDonation(donation: Donation): Donation? {
        var tempCursor: Cursor? = null
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            val blobData = donation.image

            val updateQuery = if (blobData != null) {
                "UPDATE donations SET " +
                        "title = '${donation.title}', " +
                        "id_ong = '${donation.id_ong}', " +
                        "image = ? " +
                        "WHERE id_donation = ${donation.id_donation}"

            } else {
                "UPDATE donations SET " +
                        "title = '${donation.title}', " +
                        "id_ong = '${donation.id_ong}' " +
                        "WHERE id_donation = ${donation.id_donation}"
            }

            val statement = database.compileStatement(updateQuery)

            if (blobData != null) {
                statement.bindBlob(1, blobData)
            }

            statement.executeUpdateDelete()

            val updatedDonationQuery = "SELECT * FROM donations WHERE id_donation=${donation.id_donation}"
            tempCursor = database.rawQuery(updatedDonationQuery, null)

            tempCursor?.takeIf { it.moveToFirst() }?.run {
                Donation(
                    donation.id_donation,
                    donation.title,
                    getBlob(getColumnIndexOrThrow("image")),
                    donation.id_ong
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error editing donation: ${e.message}")
            null
        } finally {
            tempCursor?.close()
            database?.close()
        }
    }

    @Throws(SQLException::class)
    fun AddDonation(donation: Donation): Donation? {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            val insertQuery = "INSERT INTO donations (title, image, id_ong) VALUES (?, ?, ?)"

            val insertStatement = database.compileStatement(insertQuery)
            insertStatement.bindString(1, donation.title)

            // If img_profile is not null, bind the byte array to the statement
            donation.image?.let {
                insertStatement.bindBlob(2, it)
            }

            insertStatement.bindLong(3, donation.id_ong.toLong())

            val insertedRowId = insertStatement.executeInsert()
            insertStatement.close()

            if (insertedRowId != -1L) {
                val newDonationQuery = "SELECT * FROM donations WHERE id_donation=$insertedRowId"
                database.rawQuery(newDonationQuery, null).use { tempCursor ->
                    if (tempCursor != null && tempCursor.moveToFirst()) {
                        val idDonation = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_donation"))
                        val title = tempCursor.getString(tempCursor.getColumnIndexOrThrow("title"))
                        val image = tempCursor.getBlob(tempCursor.getColumnIndexOrThrow("image"))
                        val idOng = tempCursor.getInt(tempCursor.getColumnIndexOrThrow("id_ong"))
                        val insertedDonation = Donation(idDonation, title, image, idOng)
                        return insertedDonation
                    }
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error adding donation: ${e.message}")
            null
        } finally {
            database?.close()
        }
    }


    @Throws(SQLException::class)
    fun DeleteDonation(donation: Donation): Boolean {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        try {
            val deleteQuery = "DELETE FROM donations WHERE id_donation = ${donation.id_donation}"
            database.execSQL(deleteQuery)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Voltorn", "Error deleting donation: ${e.message}")
        } finally {
            database?.close()
        }
        return false
    }

    // Inside the DB class
    @Throws(SQLException::class)
    fun insertUserVolunteering(userId: Int, volunteeringId: Int): Long {
        val database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        return try {
            // Insert into user_volunteering table
            val insertQuery = "INSERT INTO user_volunteering (id_user, id_volunteering) VALUES ($userId, $volunteeringId)"
            database.execSQL(insertQuery)

            // Retrieve the last inserted row ID
            val maxQuery = "SELECT last_insert_rowid()"
            val cursor = database.rawQuery(maxQuery, null)

            val lastInsertId = if (cursor.moveToFirst()) {
                cursor.getLong(0)
            } else {
                -1
            }

            cursor.close()
            lastInsertId
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}", e)
            e.printStackTrace()
            -1
        } finally {
            database.close()
        }
    }
}
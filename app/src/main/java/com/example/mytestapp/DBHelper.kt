package com.example.mytestapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context):SQLiteOpenHelper(context,dbname,factory,version) {
    companion object{
        internal val dbname = "SqlDB"
        internal val factory = null
        internal val version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE users(email nvarchar(50) primary key," +
                         "password nvarchar(50)," +
                         "name nvarchar(50)," +
                         "lastname nvarchar(50)," +
                         "address nvarchar(50)," +
                         "phone char(10))")

        Log.d("ded", "db")

        db?.execSQL("CREATE TABLE userLoggedIn(email nvarchar(50) primary key," +
                        "password nvarchar(50))")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun insertDB(email:String,password:String,name:String,lastname:String,address:String,phone:String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        values.put("email",email)
        values.put("password",password)
        values.put("name",name)
        values.put("lastname",lastname)
        values.put("address",address)
        values.put("phone",phone)

        db.insert("users",null,values)
        db.close()
    }

    fun insertDBuserLoggedIn(email: String, password: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        values.put("email",email)
        values.put("password",password)

        db.insert("userLoggedIn", null, values)
        db.close()
    }

    fun getUserLoggedInCount(): Int {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT COUNT(*) FROM userLoggedIn"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun deleteAllUserLoggedIn() {
        val db: SQLiteDatabase = writableDatabase
        db.delete("userLoggedIn", null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun selectUserLoggedIn(): UserCredentials? {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM userLoggedIn"

        val userCredentials: UserCredentials

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            cursor.close()
            userCredentials = UserCredentials(email, password)
            return userCredentials
        } else {
            cursor.close()
            return null
        }
    }

    fun selectDB(email:String,password:String):Boolean{
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM users where email ='${email}' and password = '${password}'"
        val cursor:Cursor = db.rawQuery(query,null)

        if(cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun selectLoginCredentials(email: String, password: String): UserCredentials {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM users where email ='${email}' and password = '${password}'"
        return UserCredentials(email, password)
    }

    @SuppressLint("Range")
    fun selectUserData(email: String, password: String): User? {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, name, lastname, address, phone FROM users where email ='${email}' and password = '${password}'"
        val user1: User

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val lastname = cursor.getString(cursor.getColumnIndex("lastname"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            val phone = cursor.getString(cursor.getColumnIndex("phone"))
            cursor.close()
            user1 = User(email, name, lastname, address, phone)
            return user1
        } else {
            cursor.close()
            return null
        }

    }

    fun updateUserData(email: String, name: String, lastname: String, address: String, phone: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("lastname", lastname)
        contentValues.put("address", address)
        contentValues.put("phone", phone)
        Log.d("name", name)

        db.update("users", contentValues, "email = '${email}'", null)

    }
}
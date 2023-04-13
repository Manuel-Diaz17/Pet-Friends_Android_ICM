package com.example.mytestapp

class UserCredentials(
    val email:String,
    val password:String
): java.io.Serializable {
    override fun toString(): String {
        return "${email}, ${password}"
    }
}

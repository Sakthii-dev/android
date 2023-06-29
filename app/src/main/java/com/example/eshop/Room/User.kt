package com.example.eshop

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(


    @ColumnInfo(name = "fname") val firstName: String,
    @ColumnInfo(name = "lname") val lastName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "code") val code: String,

    @PrimaryKey
    @ColumnInfo(name = "phone") val phone: String,

    @ColumnInfo(name = "password") val password: String,



)

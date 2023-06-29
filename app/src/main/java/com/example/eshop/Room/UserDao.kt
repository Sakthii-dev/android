package com.example.eshop

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE phone LIKE :roll LIMIT 1")
    fun findByRoll(roll: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)


    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE phone = :userId)")
    fun isRecordExistsUserId(userId: String?): Boolean

    @Query("SELECT password FROM user_table WHERE phone = :phone")
    fun isCorrectPassword(phone: String?): String


    @Query("SELECT * FROM user_table WHERE phone == :phone")
    fun getUser(phone: String): User

    @Query("SELECT phone FROM user_table")
    fun rnoList(): List<String>


}


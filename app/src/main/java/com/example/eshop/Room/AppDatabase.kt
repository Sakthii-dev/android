package com.example.eshop

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    abstract fun UserDao() : UserDao


    companion object {

        private var INSTANCE :AppDatabase? = null


         fun getDataBase(context: Context): AppDatabase {
            if(INSTANCE == null){
                synchronized(AppDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase :: class.java,
                        "user_table"
                    ).build()
                }
            }
            return INSTANCE!!

        }
    }
}
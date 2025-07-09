package com.gsm.quotesmvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gsm.quotesmvvm.models.Result
import kotlin.concurrent.Volatile

@Database(entities = [Result::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        fun getDatabase(context: Context): QuoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quotes.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}
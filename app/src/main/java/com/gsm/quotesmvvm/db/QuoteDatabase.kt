package com.gsm.quotesmvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gsm.quotesmvvm.models.Result
import com.gsm.quotesmvvm.utils.Converters
import kotlin.concurrent.Volatile

@Database(entities = [Result::class], version = 2)
@TypeConverters(Converters::class)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE quote ADD COLUMN tags TEXT NOT NULL DEFAULT '[]'")
            }
        }

        fun getDatabase(context: Context): QuoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quotes.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE!!
        }
    }
}
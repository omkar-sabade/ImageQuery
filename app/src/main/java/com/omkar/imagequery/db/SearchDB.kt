package com.omkar.imagequery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Item::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class SearchDB : RoomDatabase() {

    companion object {

        @Volatile
        private var dbInstance: SearchDB? = null

        fun getInstance(context: Context): SearchDB {
            return dbInstance ?: synchronized(this) {
                dbInstance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SearchDB::class.java,
                    "item_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }.also { dbInstance = it }
        }

    }

    abstract fun itemDAO(): ItemDAO

}

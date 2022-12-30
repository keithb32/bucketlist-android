package edu.virginia.cs.cs4720.kab7em.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.virginia.cs.cs4720.kab7em.converter.DateConverter
import edu.virginia.cs.cs4720.kab7em.model.BucketItem

/***************************************************************************************
 *  REFERENCES
 *  Title: AppRoomDatabase.kt
 *  Author: Mark Sherriff
 *  Date: 9/9/2022
 *  URL: https://github.com/uva-cs4720-f22/storing-data-with-room/blob/main/app/src/main/java/edu/virginia/cs4720/roomexample/AppRoomDatabase.kt
 *
 ***************************************************************************************/

@Database(entities = [BucketItem::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bucketItemDao(): BucketItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .allowMainThreadQueries()
                    .addTypeConverter(DateConverter())
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
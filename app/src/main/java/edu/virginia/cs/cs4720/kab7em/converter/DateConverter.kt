package edu.virginia.cs.cs4720.kab7em.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

/***************************************************************************************
 *  REFERENCES
 *  Title: Referencing complex data using Room
 *  Date: 10/27/2021
 *  URL: https://developer.android.com/training/data-storage/room/referencing-data
 *  Software License: Apache 2.0
 *
 ***************************************************************************************/

@ProvidedTypeConverter
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
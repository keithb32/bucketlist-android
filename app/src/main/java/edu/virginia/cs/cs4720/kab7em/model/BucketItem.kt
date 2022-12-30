package edu.virginia.cs.cs4720.kab7em.model

import androidx.room.*
import java.util.*

/***************************************************************************************
 *  REFERENCES
 *  Title: Defining data using Room entities
 *  Date: 10/27/2021
 *  URL: https://developer.android.com/training/data-storage/room/defining-data
 *  Software License: Apache 2.0
 *
 *  Title: How to make primary key as autoincrement for Room Persistence lib
 *  Author: Renetik
 *  Date: 8/17/2020
 *  URL: https://stackoverflow.com/questions/44109700/how-to-make-primary-key-as-autoincrement-for-room-persistence-lib
 *
 ***************************************************************************************/

@Entity(tableName="bucket_items")
@TypeConverters
data class BucketItem (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name= "uid") val uid: Int,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "due_date") val dueDate: Date,
    @ColumnInfo(name = "completed") val isComplete: Boolean,
    @ColumnInfo(name = "completion_date") val completionDate: Date?
){
    constructor(itemName: String, dueDate : Date, isComplete: Boolean, completionDate: Date?) :
            this(0,itemName, dueDate, isComplete, completionDate)
}
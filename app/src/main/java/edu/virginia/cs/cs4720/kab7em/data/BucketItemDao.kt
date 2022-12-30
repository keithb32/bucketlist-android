package edu.virginia.cs.cs4720.kab7em.data

import androidx.room.*
import edu.virginia.cs.cs4720.kab7em.model.BucketItem
import java.util.*

/***************************************************************************************
 *  REFERENCES
 *  Title: Accessing data using Room DAOs
 *  Date: 10/27/2021
 *  URL: https://developer.android.com/training/data-storage/room/accessing-data
 *  Software License: Apache 2.0
 *
 ***************************************************************************************/

@Dao
interface BucketItemDao {
    @Query("SELECT * FROM bucket_items")
    fun getAll(): List<BucketItem>

    @Query("SELECT * FROM bucket_items WHERE uid = :id")
    fun get(id : Int): BucketItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items : List<BucketItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: BucketItem)

    @Query("DELETE FROM bucket_items")
    fun deleteAll()

    @Query("UPDATE bucket_items SET item_name = :itemName, due_date = :dueDate, completed = :isComplete, completion_date = :completionDate WHERE uid = :id")
    fun update(id: Int, itemName: String?, dueDate: Date?, isComplete: Boolean, completionDate: Date?)
}
package edu.virginia.cs.cs4720.kab7em

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.virginia.cs.cs4720.kab7em.adapter.ItemAdapter
import edu.virginia.cs.cs4720.kab7em.data.AppDatabase
import edu.virginia.cs.cs4720.kab7em.helper.DateHelper
import edu.virginia.cs.cs4720.kab7em.model.BucketItem
import java.time.LocalDate
import java.util.*

const val LAUNCH_ADD_ITEM_ACTIVITY = 1
const val LAUNCH_EDIT_ITEM_ACTIVITY = 2

/***************************************************************************************
 *  REFERENCES
 *  Title: Recyclerview - cannot call this method in a scroll callback
 *  Author: Paresh P.
 *  Date: 3/22/2017
 *  URL: https://stackoverflow.com/questions/42944005/recyclerview-cannot-call-this-method-in-a-scroll-callback
 *
 *  Title: How to manage startActivityForResult on Android
 *  Author: Nishant
 *  Date: 5/2/2012
 *  URL: https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
 *
 ***************************************************************************************/

class MainActivity : AppCompatActivity() {

    private val appDatabase by lazy { AppDatabase.getDatabase(this).bucketItemDao() }
    lateinit var bucketItems: List<BucketItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Place bucket items in recycler view
        bucketItems = sortBucketItems(appDatabase.getAll())
        adapter = ItemAdapter(this, bucketItems)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter

        // Configure the adapter so that when an item's text is clicked
        // an EditBucketItemActivity is launched for that item
        adapter.setOnItemClickListener(object: ItemAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item = bucketItems[position]
                val intent = Intent(this@MainActivity, EditBucketItemActivity::class.java)
                intent.putExtra("ID", item.uid)
                intent.putExtra("ITEM_NAME_TO_EDIT", item.itemName)
                intent.putExtra("DUE_DATE_TO_EDIT", DateHelper.getStringFromDate(item.dueDate))
                intent.putExtra("IS_COMPLETE_TO_EDIT", item.isComplete)
                intent.putExtra("COMPLETION_DATE_TO_EDIT", DateHelper.getStringFromDate(item.completionDate))
                startActivityForResult(intent, LAUNCH_EDIT_ITEM_ACTIVITY)
            }
        })

        // Configure the adapter so that when an item's checkbox is clicked
        // its state is updated in the database
        // and the list is resorted accordingly
        adapter.setOnBoxClickListener(object: ItemAdapter.OnCheckBoxClickListener{
            override fun onBoxClick(position: Int, checkbox: CheckBox){
                val item = bucketItems[position]
                if (checkbox.isChecked){
                    appDatabase.update(item.uid, item.itemName, item.dueDate, checkbox.isChecked, DateHelper.getTodaysDate())
                }
                else{
                    appDatabase.update(item.uid, item.itemName, item.dueDate, checkbox.isChecked, null)
                }
                bucketItems = sortBucketItems(appDatabase.getAll())
                adapter = recyclerView.adapter as ItemAdapter
                recyclerView.post {
                    adapter.updateDataset(bucketItems)
                }

            }
        })

        // Configure floating action button so that it launches the AddBucketItem activity
        val addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddBucketItemActivity::class.java)
            startActivityForResult(intent, LAUNCH_ADD_ITEM_ACTIVITY)
        }
    }

    // Handler for result intents from other activities
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle result from AddBucketItemActivity
        if (requestCode == LAUNCH_ADD_ITEM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val itemName = data!!.getStringExtra("ADDED_ITEM_NAME")
                val dueDate = DateHelper.getDateFromString(data.getStringExtra("ADDED_DUE_DATE"))
                val isComplete = data.getBooleanExtra("ADDED_IS_COMPLETE", false)
                val completionDate = DateHelper.getDateFromString(data.getStringExtra("ADDED_COMPLETION_DATE"))

                appDatabase.insert(BucketItem(itemName!!, dueDate!!, isComplete, completionDate))
                bucketItems = sortBucketItems(appDatabase.getAll())
                adapter = recyclerView.adapter as ItemAdapter
                adapter.updateDataset(bucketItems)
            }
        }

        // Handle result from EditBucketItemActivity
        else if (requestCode == LAUNCH_EDIT_ITEM_ACTIVITY){
            if (resultCode == RESULT_OK){
                val id = data!!.getIntExtra("ID", -1)
                val itemName = data.getStringExtra("EDITED_ITEM_NAME")
                val dueDate = DateHelper.getDateFromString(data.getStringExtra("EDITED_DUE_DATE"))
                val isComplete = data.getBooleanExtra("EDITED_IS_COMPLETE", false)
                val completionDate = DateHelper.getDateFromString(data.getStringExtra("EDITED_COMPLETION_DATE"))

                appDatabase.update(id, itemName, dueDate, isComplete, completionDate)
                bucketItems = sortBucketItems(appDatabase.getAll())
                adapter = recyclerView.adapter as ItemAdapter
                adapter.updateDataset(bucketItems)
            }
        }
    }
}

// Helper function to sort bucket items before displaying in recycler view
fun sortBucketItems(items: List<BucketItem>): List<BucketItem> {
    if (items.isEmpty()) {
        return items
    }

    // Split list of bucket items into two sublists: a list of completed items and a list of incomplete items
    // Then sort each sublist
    val byCompletion = items.groupBy{it.isComplete}
    val notCompletedByDueDate = byCompletion[false]?.sortedWith(compareBy{it.dueDate})
    val completedByCompletionDate = byCompletion[true]?.sortedWith(compareBy{it.completionDate})

    // If all bucket items are completed, return the sorted sublist of completed items
    return if (notCompletedByDueDate == null){
        completedByCompletionDate!!
    }
    // If all bucket items are incomplete, return the sorted sublist of incomplete items
    else if (completedByCompletionDate == null){
        notCompletedByDueDate!!
    }
    // If some bucket items are incomplete and some are complete, concatenate the two sorted sublists
    else{
        notCompletedByDueDate!!.plus(completedByCompletionDate!!)
    }


}


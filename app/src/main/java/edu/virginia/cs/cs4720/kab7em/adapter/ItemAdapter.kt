package edu.virginia.cs.cs4720.kab7em.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.virginia.cs.cs4720.kab7em.R
import edu.virginia.cs.cs4720.kab7em.helper.DateHelper
import edu.virginia.cs.cs4720.kab7em.model.BucketItem

/***************************************************************************************
 *  REFERENCES
 *  Title: Adding a RecyclerView to your app
 *  URL: https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-2-pathway-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-recyclerview-scrollable-list#3
 *  Software License: Apache 2.0
 *
 *  Title: Item Click Listener - RecyclerView using Kotlin || kotlin Item Click Listener in RecyclerView
 *  Author: Foxandroid
 *  Date: 5/7/2021
 *  URL: https://www.youtube.com/watch?v=dB9JOsVx-yY
 *
 *  Title: How to update my Recyclerview using kotlin android?
 *  Author: Rozina
 *  Date: 10/26/2018
 *  URL: https://stackoverflow.com/questions/53005135/how-to-update-my-recyclerview-using-kotlin-android
 *
 ***************************************************************************************/

class ItemAdapter(private val context: Context, private var dataset: List<BucketItem>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    private lateinit var itemListener: OnItemClickListener
    private lateinit var checkBoxListener: OnCheckBoxClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    interface OnCheckBoxClickListener{
        fun onBoxClick(position: Int, checkbox : CheckBox)
    }

    class ItemViewHolder(private val view: View, itemListener: OnItemClickListener, checkboxListener: OnCheckBoxClickListener) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.findViewById(R.id.item_title)
        val checkboxView : CheckBox = view.findViewById(R.id.task_completed)

        // Set listeners for the views in the holder
        init {
            itemTextView.setOnClickListener {
                itemListener.onItemClick(adapterPosition)
            }
            checkboxView.setOnClickListener {
                checkboxListener.onBoxClick(adapterPosition, checkboxView)
            }
        }
    }

    // create new view and inflate so it can display the bucket items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout, itemListener, checkBoxListener)
    }

    // binds bucket item data to widgets
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        // Add text for each item in the recycler view to display
        holder.itemTextView.text = "${item.itemName}\n" + "due: ${DateHelper.getStringFromDate(item.dueDate)}"

        //if (item.isComplete){
        //    holder.itemTextView.text = holder.itemTextView.text as String + "\ncompleted: ${DateHelper.getStringFromDate(item.completionDate)}"
        // }
        
        holder.checkboxView.isChecked = item.isComplete
    }

    // return number of items in dataset (the BucketItem list)
    override fun getItemCount() = dataset.size

    fun setOnItemClickListener(listener: OnItemClickListener){
        itemListener = listener
    }

    fun setOnBoxClickListener(listener: OnCheckBoxClickListener){
        checkBoxListener = listener
    }

    fun updateDataset(bucketItems : List<BucketItem>){
        dataset = bucketItems
        notifyDataSetChanged()
    }
}
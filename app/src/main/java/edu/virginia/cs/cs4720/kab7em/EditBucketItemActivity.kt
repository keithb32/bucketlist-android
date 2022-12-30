package edu.virginia.cs.cs4720.kab7em

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import edu.virginia.cs.cs4720.kab7em.helper.DateHelper
import java.text.SimpleDateFormat
import java.util.*

/***************************************************************************************
 *  REFERENCES
 *  Title: How to Add and Customize Back Button of Action Bar in Android?
 *  Author: namanjha10
 *  Date: 2/23/2021
 *  URL: https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
 *
 *  Title: How to use DatePickerDialog in Kotlin?
 *  Author: Alexandr Kovalenko, Victor Zamanian
 *  Date: 8/23/2017
 *  URL: https://stackoverflow.com/questions/45842167/how-to-use-datepickerdialog-in-kotlin
 *
 *  Title: Date picker dialog hiding behind soft keyboard
 *  Author: Sam
 *  Date: 10/11/2015
 *  URL: https://stackoverflow.com/questions/33643952/date-picker-dialog-hiding-behind-soft-keyboard
 *
 *  Title: Implement Form Validation (Error to EditText) in Android
 *  Author: adityamshidlyali
 *  Date: 7/27/2022
 *  URL: https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
 *
 ***************************************************************************************/

class EditBucketItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bucket_item)

        // Show back arrow in action bar
        setupActionBar(supportActionBar)

        // Change instructionView text (since we are using same xml file as AddBucketItemActivity)
        val instructionView = findViewById<TextView>(R.id.textViewInstructions)
        instructionView.text = getString(R.string.edit_item_instructions)

        // Get view objects
        val itemNameView = findViewById<EditText>(R.id.editTextItemName)
        val dueDateView = findViewById<EditText>(R.id.editTextDueDate)
        val checkBoxView = findViewById<CheckBox>(R.id.checkBoxCompleted)
        val completionDateTextView = findViewById<TextView>(R.id.textView5)
        val completionDateView = findViewById<EditText>(R.id.editTextCompletionDate)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        // Attach date picker dialogs to date views
        attachDatePicker(dueDateView)
        attachDatePicker(completionDateView)

        // Populate views with info from the tapped item
        populateInfo(itemNameView, dueDateView, checkBoxView, completionDateView)

        // Handle visibility of completion date view based on state of checkbox on launch
        if (checkBoxView.isChecked){
            completionDateView.visibility = View.VISIBLE
            completionDateTextView.visibility = View.VISIBLE
        }
        else {
            completionDateView.visibility = View.GONE
            completionDateTextView.visibility = View.GONE
        }

        // Add listener to checkbox to change the completion date view's visibility
        // whenever the checkbox state is changed
        checkBoxView.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked){
                completionDateView.visibility = View.VISIBLE
                completionDateTextView.visibility = View.VISIBLE
            }
            else{
                completionDateView.text.clear()
                completionDateView.visibility = View.GONE
                completionDateTextView.visibility = View.GONE
            }
        }

        // Configure save button to send back user input in the result intent
        saveBtn.setOnClickListener {
            val itemName = itemNameView.text.toString()
            val dueDate = dueDateView.text.toString()
            val isComplete = checkBoxView.isChecked
            val completionDate = completionDateView.text.toString()

            // Validate input
            // Rules: itemName, dueDate, isComplete must ALWAYS be nonempty
            //        completionDate can be empty only if isComplete is false
            if (itemName == "" || dueDate == "" || (isComplete && completionDate == "")){
                if (itemName == "") {
                    itemNameView.error = "Please enter an item name."
                }
                if (dueDate == "") {
                    dueDateView.error = "Please enter a due date."
                }
                if (isComplete && completionDate == "") {
                    completionDateView.error = "Please enter a completion date."
                }
            }
            // Return data in intent if input is valid
            else{
                val intent = Intent(this, MainActivity::class.java)
                    .apply{
                        putExtra("ID", intent.extras?.getInt("ID"))
                        putExtra("EDITED_ITEM_NAME", itemName)
                        putExtra("EDITED_DUE_DATE", dueDate)
                        putExtra("EDITED_IS_COMPLETE", isComplete)
                        putExtra("EDITED_COMPLETION_DATE", completionDate)
                    }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }

    private fun setupActionBar(actionBar: ActionBar?){
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Edit Bucket Item"
    }

    private fun populateInfo(itemNameView: EditText, dueDateView: EditText, checkBoxView: CheckBox, completionDateView: EditText){
        val bundle = intent.extras
        var itemNameText = bundle?.getString("ITEM_NAME_TO_EDIT")
        var dueDateText = bundle?.getString("DUE_DATE_TO_EDIT")
        var checkBoxState = bundle?.getBoolean("IS_COMPLETE_TO_EDIT")
        var completionDateText = bundle?.getString("COMPLETION_DATE_TO_EDIT")

        itemNameView.setText(itemNameText)
        dueDateView.setText(dueDateText)
        checkBoxView.isChecked = checkBoxState!!
        completionDateView.setText(completionDateText)
    }

    private fun attachDatePicker(dateView : EditText){
        val cal = Calendar.getInstance()

        // Disable keyboard on focus for date view
        dateView.inputType = 0

        // Create listeners that set the text of the date views based on the date picked
        // in the date picker dialog
        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "MM/dd/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateView.setText(sdf.format(cal.time))
        }

        // Hide keyboard if it is already on the screen when switching to date views
        dateView.setOnFocusChangeListener{ view, hasFocus ->
            if (hasFocus) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        // Open date picker dialog when date views are clicked
        dateView.setOnClickListener {
            var dateViewText = dateView.text.toString()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            // Set date picker default date to the user's previous input, if there is any
            if (dateViewText != "") {
                year = DateHelper.getYearFromString(dateViewText)
                month = DateHelper.getMonthFromString(dateViewText)
                day = DateHelper.getDayFromString(dateViewText)
            }

            val dialog = DatePickerDialog(this, dateListener, year, month, day)
            dialog.show()
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
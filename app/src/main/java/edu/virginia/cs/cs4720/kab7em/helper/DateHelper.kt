package edu.virginia.cs.cs4720.kab7em.helper

import java.util.*

class DateHelper {

    companion object {

        // Return equivalent date object from string in mm/dd/YYYY format
        fun getDateFromString(str: String?): Date? {
            if (str == "") {
                return null
            }
            val dateStrings = str?.split("/")?.toTypedArray()
            val month = dateStrings?.get(0)?.toInt()?.minus(1)
            val day = dateStrings?.get(1)?.toInt()
            val year = dateStrings?.get(2)?.toInt()
            return Date(year!!, month!!, day!!)
        }

        // Return year from string in mm/dd/YYYY format
        fun getYearFromString(str: String?) : Int {
            val dateStrings = str?.split("/")?.toTypedArray()
            val year = dateStrings?.get(2)?.toInt()
            return year!!
        }

        // Return month-1 from string in mm/dd/YYYY format
        // We subtract 1 to follow the convention of Java Date class, which maps January to 0
        fun getMonthFromString(str: String?) : Int {
            val dateStrings = str?.split("/")?.toTypedArray()
            val month = dateStrings?.get(0)?.toInt()?.minus(1)
            return month!!
        }

        // Return day from string in mm/dd/YYYY format
        fun getDayFromString(str: String?) : Int {
            val dateStrings = str?.split("/")?.toTypedArray()
            val day = dateStrings?.get(1)?.toInt()
            return day!!
        }

        // Return string in mm/dd/YYYY format from date object
        // We add 1 to undo the convention of Java Date class, which maps January to 0
        fun getStringFromDate(d: Date?): String {
            if (d == null) {
                return ""
            }
            return "${d.month + 1}/${d.date}/${d.year}"
        }

        // Return today's date as a Java Date object
        fun getTodaysDate(): Date {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return Date(year, month, day)
        }
    }

}
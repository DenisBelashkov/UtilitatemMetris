package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

class CustomDateTimePicker(
    private val context: Context,
    private val onReadyCallback: (Calendar) -> Unit
) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val calendar: Calendar = Calendar.getInstance()
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var month = calendar.get(Calendar.MONTH)
    private var year = calendar.get(Calendar.YEAR)

    private var hour = calendar.get(Calendar.HOUR)
    private var minute = calendar.get(Calendar.MINUTE)

    private val datePicker = DatePickerDialog(context, this, year, month, day)

    private val timePicker = TimePickerDialog(context, this, hour, minute, true)

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.day = dayOfMonth
        timePicker.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute
        onReadyCallback.invoke(buildResultCalendar())
    }

    private fun buildResultCalendar(): Calendar {
        return Calendar.getInstance().apply {
            this.set(year, month, day, hour, minute)
        }
    }

    fun show() {
        datePicker.show()
    }

}
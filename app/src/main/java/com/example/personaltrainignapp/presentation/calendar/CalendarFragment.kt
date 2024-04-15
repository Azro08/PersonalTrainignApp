package com.example.personaltrainignapp.presentation.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.databinding.FragmentCalendarBinding
import com.example.personaltrainignapp.util.Constants
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private val binding by viewBinding(FragmentCalendarBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCalendarDate()
    }

    private fun setCalendarDate() {
        binding.compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY)
        val currentDate: Date = Calendar.getInstance().time
        val currentDateString = formatDateWithWeekday(currentDate)
        binding.textViewDate.text = currentDateString

        binding.compactCalendarView.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val formattedDate = formatDateWithWeekday(dateClicked)
                binding.textViewDate.text = formattedDate
                Log.d("TAG", "Day was clicked: $formattedDate")
                val fullDate = Constants.getFullDateString(dateClicked)
                navToHistoryDetails(fullDate)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                val formattedMonth = formatDateWithWeekday(firstDayOfNewMonth)
                binding.textViewDate.text = formattedMonth
                Log.d("TAG", "Month was scrolled to: $formattedMonth")
            }
        })

    }

    private fun navToHistoryDetails(fullDate: String) {
        val bundle = bundleOf(Pair(Constants.DATE_KEY, fullDate))
        findNavController().navigate(R.id.nav_history_details, bundle)
    }



    private fun formatDateWithWeekday(date: Date): String {
        val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

}
package com.daniel.todoapp.ui.fragment.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.findNavController
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.FragmentSqlTodoFormBinding
import com.daniel.todoapp.data.models.SqlTodo
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.DATE
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.DESCRIPTION
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.IS_COMPLETED
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TIME
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.TITLE
import com.daniel.todoapp.data.sqlite.DbContract.TodoTable.Companion.URL
import com.daniel.todoapp.data.sqlite.DbTodo
import com.daniel.todoapp.ui.fragment.HomeFragment
import java.util.Calendar
import java.util.Locale

class SqlTodoFormFragment : Fragment() {
    private lateinit var binding: FragmentSqlTodoFormBinding
    private lateinit var dbTodo: DbTodo
    private var data: SqlTodo? = null

    private lateinit var datePickerDialog: DatePickerDialog
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var hour: Int = 0
    private var minute: Int = 0

    private var todoUrl: String = ""
    private var isCompleted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSqlTodoFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()

        dbTodo = DbTodo(requireContext())
        dbTodo.open()

        binding.gmapBtnSection.visibility = View.GONE

        data = SqlTodoFormFragmentArgs.fromBundle(arguments as Bundle).data

        data?.let {
            isCompleted = data!!.isCompleted
            binding.editTitle.setText(data!!.title)
            binding.editDescription.setText(data!!.description)
            binding.btnDate.setText(data!!.date)
            binding.btnTime.setText(data!!.time)
            if(data!!.url != ""){
                binding.gmapBtnSection.visibility = View.VISIBLE
                todoUrl = data!!.url!!

                binding.btnGmap.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(todoUrl))
                    startActivity(intent)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val editTitle = binding.editTitle.text.toString()

            if(editTitle == ""){
                Toast.makeText(requireContext(), "Title tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editDescription = binding.editDescription.text.toString()
            val editDate = selectedDate
            val editTime = selectedTime
            saveData(view, editTitle, editDescription, editDate, editTime, todoUrl, isCompleted)
        }

        binding.btnBack.setOnClickListener {
            view.findNavController().navigate(
                SqlTodoFormFragmentDirections.actionSqlTodoFormFragmentToTodosFragment()
            )
        }

        binding.btnDate.setOnClickListener {
            openDatePicker(view)
        }

        binding.btnTime.setOnClickListener {
            popTimePicker(view)
        }
    }

    fun deleteData(view: View){
        val result = dbTodo.delete(data!!.id.toString())
        if(result > 0){
            view.findNavController().navigate(
                SqlTodoFormFragmentDirections.actionSqlTodoFormFragmentToTodosFragment()
            )
            Toast.makeText(requireContext(), "Berhasil Menghapus data", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveData(view: View, title : String, description: String, date: String, time: String, url: String, isCompleted: Boolean){
        val values = ContentValues()
        values.put(TITLE, title)
        values.put(DESCRIPTION, description)
        values.put(DATE, date)
        values.put(TIME, time)
        values.put(URL, url)
        values.put(IS_COMPLETED, if (isCompleted) 1 else 0)

        if(data?.id != null && data?.id != 0){
            val result = dbTodo.update(data!!.id.toString(), values)
            if(result > 0){
                view.findNavController().navigate(
                    SqlTodoFormFragmentDirections.actionSqlTodoFormFragmentToTodosFragment()
                )
                Toast.makeText(requireContext(), "Berhasil mengubah data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        } else {
            val result = dbTodo.insert(values)
            if(result > 0){
                view.findNavController().navigate(
//                    SqlTodoFormFragmentDirections.actionSqlTodoFormFragmentToSqlTodoFragment2()
                    SqlTodoFormFragmentDirections.actionSqlTodoFormFragmentToTodosFragment()

                )
                Toast.makeText(requireContext(), "Berhasil Menambahkan data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTodayDate(): String{
        var cal: Calendar = Calendar.getInstance()
        var year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month = month + 1
        var day: Int = cal.get(Calendar.DAY_OF_MONTH)

        return makeDateString(day, month, year)
    }

    private fun initDatePicker(){
        var dateSetListener: DatePickerDialog.OnDateSetListener = object: DatePickerDialog.OnDateSetListener{
            override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int){
                var updatedMonth = month + 1
                var date: String = makeDateString(day, updatedMonth, year)
                selectedDate = makeDateString(day, updatedMonth, year)
                binding.btnDate.setText(selectedDate)
            }
        }

        var cal: Calendar = Calendar.getInstance()
        var year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        var day: Int = cal.get(Calendar.DAY_OF_MONTH)

        val minDate = cal.timeInMillis

        datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = minDate

    }

    fun popTimePicker(view: View){
        var onTimeListener: TimePickerDialog.OnTimeSetListener = object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var hour = hourOfDay
                var minute = minute
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                binding.btnTime.setText(selectedTime)
            }
        }

        var timePickerDialog: TimePickerDialog = TimePickerDialog(requireContext(), onTimeListener, hour, minute, true)

        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }

    fun makeDateString(day: Int, month: Int, year: Int): String{
        return "$day ${getMonthFormat(month)} $year"
    }

    fun getMonthFormat(month: Int): String{
        if(month == 1) {
            return "Jan"
        }
        else if(month == 2) {
            return "Feb"
        }
        else if(month == 3) {
            return "Mar"
        }
        else if(month == 4) {
            return "Apr"
        }
        else if(month == 5) {
            return "Mei"
        }
        else if(month == 6) {
            return "Jun"
        }
        else if(month == 7) {
            return "Jul"
        }
        else if(month == 8) {
            return "Agu"
        }
        else if(month == 9) {
            return "Sep"
        }
        else if(month == 10) {
            return "Okt"
        }
        else if(month == 11) {
            return "Nov"
        }
        else if(month == 12) {
            return "Des"
        }

        return "Err"
    }

    fun openDatePicker(view: View){
        datePickerDialog.show()
    }

}
package com.daniel.todoapp.ui.adapter.todo

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.RowTodoBinding
import com.daniel.todoapp.data.models.SqlTodo

class SqlTodoAdapter(private val listData: ArrayList<SqlTodo>) : RecyclerView.Adapter<SqlTodoAdapter.DataViewHolder>() {
    private lateinit var OnClickCallBack: onClickCallBack
    private lateinit var context: Context

    fun setOnClickCallBack(data: onClickCallBack){
        this.OnClickCallBack = data
    }

    interface onClickCallBack{
        fun onItemClicked(data: SqlTodo)
        fun onClickedDelete(data: SqlTodo)
        fun onClickedCheck(data: SqlTodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        context = parent.context
        return DataViewHolder(RowTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val curTodo = listData[position]
        var title = curTodo.title.toString()
        var time = "-"

        holder.binding.txtTitle.text = title

        if(curTodo.time != "" && curTodo.date != ""){
            time = "${curTodo.date} - ${curTodo.time}"
            holder.binding.txtTime.text = time
        }

        if(time != "-"){
            holder.binding.iconLeft.setImageResource(R.drawable.clock)
        }
        else holder.binding.iconLeft.setImageResource(R.drawable.task)

        var drawable = ContextCompat.getDrawable(context, R.drawable.button_background_circle)
        holder.binding.iconLeft.background = drawable
        holder.binding.iconLeft.visibility = View.VISIBLE
        holder.binding.txtTitle.setPadding(50, 0,0,0)
        holder.binding.txtTime.setPadding(50, 0,0,0)

        if(curTodo.isCompleted) {
            holder.binding.txtTitle.paintFlags = holder.binding.txtTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.txtTime.paintFlags = holder.binding.txtTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.binding.txtTodo.setOnClickListener {
            OnClickCallBack.onItemClicked(listData[holder.adapterPosition])
        }

        holder.binding.btnDelete.setOnClickListener {
            OnClickCallBack.onClickedDelete(listData[holder.adapterPosition])
        }

        holder.binding.checkbox.setOnClickListener {
            OnClickCallBack.onClickedCheck(listData[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listData.count()
    }

    class DataViewHolder(val binding: RowTodoBinding): RecyclerView.ViewHolder(binding.root)
}
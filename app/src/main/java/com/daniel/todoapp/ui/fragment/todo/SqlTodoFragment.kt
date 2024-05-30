package com.daniel.todoapp.ui.fragment.todo

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.FragmentSqlTodoBinding
import com.daniel.todoapp.data.models.SqlTodo
import com.daniel.todoapp.data.sqlite.DbContract
import com.daniel.todoapp.data.sqlite.DbTodo
import com.daniel.todoapp.ui.activity.WelcomeActivity
import com.daniel.todoapp.ui.adapter.todo.SqlTodoAdapter
import com.daniel.todoapp.ui.fragment.HomeFragmentDirections
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SqlTodoFragment : Fragment() {
    private lateinit var binding: FragmentSqlTodoBinding
    private lateinit var dbTodo: DbTodo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSqlTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbTodo = DbTodo(requireContext())
        dbTodo.open()

        val layoutManager = LinearLayoutManager(requireContext())
        val layoutManager2 = LinearLayoutManager(requireContext())
        binding.rvTodosUncompleted.layoutManager = layoutManager
        binding.rvTodosCompleted.layoutManager = layoutManager2

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvTodosUncompleted.addItemDecoration(itemDecoration)
        binding.rvTodosCompleted.addItemDecoration(itemDecoration)

        binding.btnAdd.setOnClickListener{
            view.findNavController().navigate(
                SqlTodoFragmentDirections.actionSqlTodoFragment2ToSqlTodoFormFragment(null)
            )
        }

        binding.bottomAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    Firebase.auth.signOut()
                    val intent = Intent(requireContext(), WelcomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            true
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.place -> {
                    view.findNavController().navigate(
//                        SqlTodoFragmentDirections.actionTodosFragmentToPlacesFragment()
                        SqlTodoFragmentDirections.actionTodosFragmentToPlaceCategoryFragment()
                    )
                    true
                }
                else -> false
            }
        }

        loadDatas(view)
    }

    fun loadDatas(view: View){
        val datas = dbTodo.findAll()

        val (completedTodos, unCompletedTodos) = datas.partition { it.isCompleted }

        if(completedTodos.count() == 0){
            binding.completed.visibility = View.GONE
        }

        val todoUncompletedAdapterAdapter = SqlTodoAdapter(ArrayList(unCompletedTodos))
        val todoCompletedAdapterAdapter = SqlTodoAdapter(ArrayList(completedTodos))

        todoUncompletedAdapterAdapter.setOnClickCallBack(object: SqlTodoAdapter.onClickCallBack {
            override fun onClickedDelete(data: SqlTodo) {
                deleteData(view, data)
            }
            override fun onItemClicked(data: SqlTodo) {
                editData(view, data)
            }
            override fun onClickedCheck(data: SqlTodo) {
                checkData(view, data)
            }
        })

        todoCompletedAdapterAdapter.setOnClickCallBack(object: SqlTodoAdapter.onClickCallBack {
            override fun onClickedDelete(data: SqlTodo) {
                deleteData(view, data)
            }

            override fun onItemClicked(data: SqlTodo) {
                editData(view, data)
            }

            override fun onClickedCheck(data: SqlTodo) {
                checkData(view, data)
            }
        })

        binding.rvTodosUncompleted.adapter = todoUncompletedAdapterAdapter
        binding.rvTodosCompleted.adapter = todoCompletedAdapterAdapter
    }

    private fun editData(view: View, data: SqlTodo){
        view.findNavController().navigate(
            SqlTodoFragmentDirections.actionSqlTodoFragment2ToSqlTodoFormFragment(
                data
            )
        )
    }

    private fun deleteData(view: View, data: SqlTodo){
        val result = dbTodo.delete(data!!.id.toString())
        if(result > 0){
            loadDatas(view)
            Toast.makeText(requireContext(), "Berhasil Menghapus data", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkData(view: View, data: SqlTodo){
        val values = ContentValues()

        values.put(DbContract.TodoTable.IS_COMPLETED, if (data!!.isCompleted) 0 else 1)

        dbTodo.update(data!!.id.toString(), values)
        loadDatas(view)
    }

}
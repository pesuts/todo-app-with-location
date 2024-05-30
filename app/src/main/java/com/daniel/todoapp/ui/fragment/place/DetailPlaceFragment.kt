package com.daniel.todoapp.ui.fragment.place

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.daniel.todoapp.viewmodel.MainViewModel
import com.daniel.todoapp.ui.activity.MainActivity
import com.daniel.todoapp.databinding.FragmentDetailPlaceBinding
import com.daniel.todoapp.ui.adapter.place.ImageSliderAdapter
import com.daniel.todoapp.data.models.Place
import com.daniel.todoapp.data.models.SqlTodo


class DetailPlaceFragment : Fragment() {
    private lateinit var binding: FragmentDetailPlaceBinding
    private var data: Place? = null
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var parentActivity: MainActivity
    lateinit var urlGMap: String
    var title: String = ""
    var description: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity = requireActivity() as MainActivity
        val viewPager: ViewPager2 = binding.viewPager

//        viewModel.isLoading.observe(viewLifecycleOwner){
//            parentActivity.setLoading(it)
//        }

//        viewModel.isFinish.observe(viewLifecycleOwner){
//            setFinishAction(view, it)
//        }

        data = DetailPlaceFragmentArgs.fromBundle(arguments as Bundle).data
        viewModel.setTargetTodo(data)

        data?.let {
            val adapter = ImageSliderAdapter(data!!.photos!!)
            viewPager.adapter = adapter
            binding.detailName.setText(data!!.name)
            binding.detailAddress.setText(data!!.address)
            binding.detailRating.setText(data!!.rating.toString())
            val totalRatings = "(${data!!.totalRatings.toString()} Ratings)"
            binding.detailReviews.setText(totalRatings)
            binding.ratingBar.setRating(data!!.rating!!.toFloat())

            urlGMap = "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=${data!!.idPlace!!}"
            title = "Pergi ke ${data!!.name}"
            description = "Pergi ke ${data!!.name} di ${data!!.address}"

            viewModel.setIsLoading(false)
        }

        binding.btnGmap.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlGMap))
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            val newTodo = SqlTodo(
                id = 0,
                title = title,
                description = description,
                date = "",
                time = "",
                url = urlGMap
            )

            view.findNavController().navigate(
                DetailPlaceFragmentDirections.actionDetailPlaceFragmentToSqlTodoFormFragment(
                    newTodo
                )
            )
        }

        binding.btnBack.setOnClickListener {
//            view.findNavController().navigate(
//                DetailPlaceFragmentDirections.actionFormFragmentToTodosFragment()
//            )
            view.findNavController().navigateUp()
        }
    }

//    private fun setFinishAction(view: View, data: Boolean){
//        if(data){
//            view.findNavController().navigate(
//                DetailPlaceFragmentDirections.actionFormFragmentToTodosFragment()
//            )
//            viewModel.setIsFinish(false)
//        }
//
//    }



}
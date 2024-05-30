package com.daniel.todoapp.ui.fragment.place

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.todoapp.R
import com.daniel.todoapp.viewmodel.MainViewModel
import com.daniel.todoapp.ui.activity.MainActivity
import com.daniel.todoapp.databinding.FragmentPlacesBinding
import com.daniel.todoapp.ui.adapter.todo.TodoAdapter
import com.daniel.todoapp.data.models.Place
import com.daniel.todoapp.ui.fragment.todo.SqlTodoFragmentDirections
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class PlacesFragment : Fragment() {
    private lateinit var binding: FragmentPlacesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var parentActivity: MainActivity
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lat: String
    private lateinit var long: String
    private lateinit var category: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        parentActivity = requireActivity() as MainActivity

        category = PlacesFragmentArgs.fromBundle(arguments as Bundle).category.toString()

        binding.progressBar.visibility = View.VISIBLE


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,
                    false) || permissions.getOrDefault(Manifest.permission
                    .ACCESS_COARSE_LOCATION, false) -> {
                    Toast.makeText(requireContext(), "Location access granted", Toast
                        .LENGTH_SHORT).show()

                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (isLocationEnabled()) {
                            val result = fusedLocationClient.getCurrentLocation(
                                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                                CancellationTokenSource().token
                            )
                            result.addOnCompleteListener {
                                lat = it.result.latitude.toString()
                                long = it.result.longitude.toString()
                                viewModel.loadDatas(long, lat, category)
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(), "Please turn ON the location.",
                                Toast.LENGTH_SHORT
                            ).show()
                            createLocationRequest()
                        }
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            100
                        )
                    }
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "No Location Access",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        viewModel.listPlace.observe(viewLifecycleOwner){
            Log.d("DATA", it.toString())
            if(it != null){
                setRvTodo(it, view)
            }
            binding.progressBar.visibility = View.GONE
        }


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodos.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvTodos.addItemDecoration(itemDecoration)

        binding.btnBack.setOnClickListener {
            view.findNavController().navigateUp()
        }


    }

    private fun setRvTodo(data: List<Place>?, view: View){
        val todoAdapter = TodoAdapter(ArrayList(data))
        todoAdapter.setOnClickCallBack(object: TodoAdapter.onClickCallBack {
            override fun onItemClicked(data: Place) {
                editData(view, data)
            }
        })
        binding.rvTodos.adapter = todoAdapter
    }

    private fun editData(view: View, data: Place){
        view.findNavController().navigate(
            PlacesFragmentDirections.actionTodosFragmentToFormFragment(
                data
            )
        )
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try{
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun createLocationRequest(){
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).setMinUpdateIntervalMillis(5000).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

        }

        task.addOnFailureListener{ e->
            if (e is ResolvableApiException){
                try{
                    e.startResolutionForResult(requireContext() as Activity, 100)
                } catch (sendEx: java.lang.Exception){

                }
            }

        }
    }

}
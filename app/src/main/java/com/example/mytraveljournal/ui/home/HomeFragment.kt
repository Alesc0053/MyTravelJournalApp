package com.example.mytraveljournal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraveljournal.R
import com.example.mytraveljournal.TravelData.TravelViewModel
import com.example.mytraveljournal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mSettingsUserViewModel: SettingsUserViewModel
    private lateinit var mTravelViewModel: TravelViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isShowingFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mSettingsUserViewModel = ViewModelProvider(requireActivity()).get(SettingsUserViewModel::class.java)
        mTravelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)

        val adapter = HomeListAdapter { travel ->
            mTravelViewModel.updateTravel(travel)
        }


        val recyclerView = root.findViewById<RecyclerView>(R.id.rvTravel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter



        //Butonul pentru a adauga un element
        val addButton = root.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_addFragment)
        }


        //Butonul pentru filtrare la favorite
        val favoriteToggle = root.findViewById<Button>(R.id.favoriteToggle)
        favoriteToggle.setOnClickListener{
            isShowingFavorite = !isShowingFavorite
            updateRecyclerView(adapter)
        }

        //Crearea listei

        mSettingsUserViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            println("User ID in HomeFragment: $userId")
            mTravelViewModel.getUserTavel(userId).observe(viewLifecycleOwner, Observer { travel ->
                val filteredList = if (isShowingFavorite) {
                    travel.filter { it.favorite }
                } else {
                    travel
                }
                adapter.setData(filteredList)
            })
        })

        return root
    }

    private fun updateRecyclerView(adapter: HomeListAdapter) {

        mSettingsUserViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            println("User ID in HomeFragment: $userId")
            mTravelViewModel.getUserTavel(userId).observe(viewLifecycleOwner, Observer { travel ->
                val filteredList = if (isShowingFavorite) {
                    travel.filter { it.favorite }
                } else {
                    travel
                }
                adapter.setData(filteredList)
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.mytraveljournal.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.mytraveljournal.R
import com.example.mytraveljournal.TravelData.Travel
import com.example.mytraveljournal.TravelData.TravelViewModel
import java.util.Calendar

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var mSettingsUserViewModel: SettingsUserViewModel
    private lateinit var mTravelViewModel: TravelViewModel
    val travelTypes = arrayOf("Leisure", "Business", "Family", "Other")
    private var selectedDate: String = ""
    private val addLocation: EditText by lazy {
        requireView().findViewById(R.id.addLocation)
    }
    private val addName: EditText by lazy {
        requireView().findViewById(R.id.addName)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSettingsUserViewModel =
            ViewModelProvider(requireActivity())[SettingsUserViewModel::class.java]
        mTravelViewModel = ViewModelProvider(this)[TravelViewModel::class.java]

        //Required UI elements
        val addDateDialog = view.findViewById<EditText>(R.id.addTextDate)
        val seekBarTravelMood = view.findViewById<SeekBar>(R.id.addTravelMood)
        val spinner: Spinner = view.findViewById(R.id.addTravelType)
        val moodText = view.findViewById<TextView>(R.id.addMoodText)

        //Travel type spinner
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, travelTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Data (calendar)
        addDateDialog.setOnClickListener {
            showDatePickerDialog()
        }

        //Mood seekBar stuff
        var newMood = 0
        seekBarTravelMood.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                newMood = p0?.progress ?: 0
                val moodResource = when {
                    newMood < 20 -> R.string.sad_Mood
                    newMood < 51 -> R.string.normal_Mood
                    newMood < 76 -> R.string.happy_Mood
                    else -> R.string.exalted_Mood
                }
                moodText.text = getString(moodResource)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        //Map and autoComplete location
        var newLong: Double = 22.0
        var newLat: Double = 22.0
        var newLocation = ""
        val mapsView: ImageView = view.requireViewById(R.id.mapsContainer)
        addLocation.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                mTravelViewModel.decodeLocation(
                    query = addLocation.text.toString(),
                    onDecodeComplete = { name, fullAddress, long, lat ->
                        addLocation.setText("$name ($fullAddress)")
                        newLong = long.toDouble()
                        newLat = lat.toDouble()
                        newLocation = fullAddress
                        mapsView.isVisible = true
                        mapsView.load(
                            "https://api.mapbox.com/styles/v1/" +
                                    "mapbox/outdoors-v11/static/pin-s+555555(" +
                                    "$long,$lat/$long,$lat," +
                                    "11.21,0/128x128@2x" +
                                    "?access_token=${getString(R.string.mapbox_access_token)}"
                        )
                    },
                    onDecodeFailed = {
                        it.printStackTrace()
                        addLocation.setText("")
                        mapsView.isVisible = false
                    }
                )
            }
        }

        //Add the new Travel
        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val newName = view.findViewById<EditText>(R.id.addName).text.toString()
            val newDescription = view.findViewById<EditText>(R.id.addTravelNotes).text.toString()
            val newTravelType = spinner.selectedItem.toString()
            addDataToDataBase(newName, newDescription, newTravelType, newMood, newLong, newLat, newLocation)
        }

    }


    private fun addDataToDataBase(
        newName: String,
        newDescription: String,
        newTravelType: String,
        newMood: Int,
        newLong: Double,
        newLat: Double,
        newLocation: String
    ) {
        if (inputCheck(newName, newDescription)) {
            mSettingsUserViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
                val newTravel = Travel(
                    name = newName,
                    date = selectedDate,
                    favorite = false,
                    travelType = newTravelType,
                    travelMood = newMood,
                    travelNotes = newDescription,
                    userId = userId,
                    latitude = newLat,
                    longitude = newLong,
                    location = newLocation
                )
                mTravelViewModel.insertTravel(newTravel)
            })


            Toast.makeText(requireContext(), "Adaugat cu succes", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.nav_home)
        } else
            Toast.makeText(requireContext(), "Please fill up all fields", Toast.LENGTH_LONG).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            selectedDate = "$year-${month + 1}-$day"
            val editTextDate = view?.findViewById<EditText>(R.id.addTextDate)
            if (editTextDate != null) {
                editTextDate.setText(selectedDate)
            }
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun inputCheck(newName: String, newDescription: String): Boolean {
        return !(TextUtils.isEmpty(newName) && TextUtils.isEmpty(newDescription))
    }

}
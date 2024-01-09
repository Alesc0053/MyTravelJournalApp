package com.example.mytraveljournal.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.mytraveljournal.R
import com.example.mytraveljournal.TravelData.Travel
import com.example.mytraveljournal.TravelData.TravelViewModel
import com.example.mytraveljournal.weatherApi.MoshiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class EditFragment : Fragment() {
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var mTravelViewModel: TravelViewModel
    val travelTypes = arrayOf("Leisure", "Business", "Family", "Other")
    private var selectedDate: String = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_edit, container, false)


        mTravelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)

        //fill the elements with the data of the chosen Travel
        view.findViewById<EditText>(R.id.editTravelNotes).setText(args.currentTravel.travelNotes)
        view.findViewById<EditText>(R.id.editName).setText(args.currentTravel.name)
        view.findViewById<EditText>(R.id.editLocation).setText(args.currentTravel.location)
        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val seekBarTravelMood = view.findViewById<SeekBar>(R.id.editTravelMood)
        val spinner: Spinner = view.findViewById(R.id.editTravelType)
        val moodText: TextView = view.findViewById(R.id.editMoodText)
        editTextDate.setText(args.currentTravel.date)
        selectedDate = args.currentTravel.date


        //Mood seekBar stuff
        val moodResource = when {
            args.currentTravel.travelMood < 20 -> R.string.sad_Mood
            args.currentTravel.travelMood < 51 -> R.string.normal_Mood
            args.currentTravel.travelMood < 76 -> R.string.happy_Mood
            else -> R.string.exalted_Mood
        }
        moodText.setText(moodResource)
        var newMood = 0
        seekBarTravelMood.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        //Travel type spinner stuff
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, travelTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val travelType = spinner.selectedItem.toString()
        val position = travelTypes.indexOf(args.currentTravel.travelType)
        spinner.setSelection(position)
        seekBarTravelMood.progress = args.currentTravel.travelMood


        //Delete
        view.findViewById<Button>(R.id.buttonDelete).setOnClickListener{
            deleteNote()
        }

        //Data (calendar)
        editTextDate.setOnClickListener{
            showDatePickerDialog()
        }

        //Map and location autocomplete
        val mapsView: ImageView = view.requireViewById(R.id.mapsContainer2)
        val long = args.currentTravel.longitude
        val lat = args.currentTravel.latitude
        mapsView.isVisible = true
        mapsView.load(
            "https://api.mapbox.com/styles/v1/" +
                    "mapbox/outdoors-v11/static/pin-s+555555(" +
                    "$long,$lat/$long,$lat," +
                    "11.21,0/128x128@2x" +
                    "?access_token=${getString(R.string.mapbox_access_token)}"
        )

        var newLong: Double = args.currentTravel.longitude
        var newLat: Double = args.currentTravel.latitude
        var newLocation = args.currentTravel.location
        val addLocation = view.findViewById<EditText>(R.id.editLocation)
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

        //update button pressed
        view.findViewById<Button>(R.id.buttonEdit).setOnClickListener{
            val newName = view.findViewById<EditText>(R.id.editName).text.toString()
            val newDescription = view.findViewById<EditText>(R.id.editTravelNotes).text.toString()
            val newTravelType = spinner.selectedItem.toString()
            val newMood = seekBarTravelMood.progress
            val newLocation = view.findViewById<EditText>(R.id.editLocation).text.toString()
            insertDataToDatabase(newName,newDescription,newTravelType,newMood, newLocation, newLat, newLong)
        }



        //Vremea
        val apiKey = "17c38684c154bbbb079e26335f826f79"
        val weather = view.findViewById<TextView>(R.id.editTemperature)
        val humidity = view.findViewById<TextView>(R.id.editHumidity)
        val weatherDesciption = view.findViewById<TextView>(R.id.editWeatherDescription)

        lifecycleScope.launch(Dispatchers.IO){
            try {
                val weatherList =
                    MoshiApi.API.retrofitService.listWeatherModel(newLat, newLong, apiKey)

                weatherList.let {
                    withContext(Dispatchers.Main.immediate) {


                        val temperature = weatherList.main?.temp
                        val mHumidity = weatherList.main?.humidity
                        val mWeatherDescription = weatherList.weather?.firstOrNull()?.description

                        temperature?.let {
                            val formattedTemperature = String.format("%.2f", it - 274)
                            weather.text = "Temperature: $formattedTemperature °C"
                        }

                        mHumidity?.let {
                            humidity.text = "Humidity: $mHumidity%"
                        }

                        mWeatherDescription?.let {
                            weatherDesciption.text = "Weather Description: $mWeatherDescription"
                        }

                    }
                }

            } catch (e: Exception) {
                Log.e("WeatherApp", "Error fetching weather data: ${e.message}")
            }
        }

        return view
    }

    private fun insertDataToDatabase(newName: String, newDescription: String, newTravelType: String, newMood: Int, newLocation: String, newLatitude: Double, newLongitude: Double){
        if(inputCheck(newName,newDescription)){

            val newTravel = Travel(
                name = newName,
                date = selectedDate,
                favorite = false,
                travelType = newTravelType,
                travelMood = newMood,
                travelNotes = newDescription,
                id = args.currentTravel.id,
                userId = args.currentTravel.userId,
                longitude = newLongitude,
                latitude = newLatitude,
                location = newLocation)
            mTravelViewModel.updateTravel(newTravel)

            Toast.makeText(requireContext(),"Modificat cu succes", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_editFragment_to_nav_home)
        }
        else
            Toast.makeText(requireContext(),"Please fill up all fields", Toast.LENGTH_LONG).show()
    }


    private fun inputCheck(newName: String, newDescription: String): Boolean{
        return !(TextUtils.isEmpty(newName) && TextUtils.isEmpty(newDescription))
    }

    private fun deleteNote(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            mTravelViewModel.deleteTavel(args.currentTravel)

            findNavController().navigate(R.id.action_editFragment_to_nav_home)
        }
        builder.setNegativeButton("No") {_, _->}
        builder.setTitle("Delete ${args.currentTravel.name}?")
        builder.setMessage("Are you sure you want to delete this travel?")
        builder.show()
    }

    private fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            selectedDate = "$year-${month + 1}-$day"
            val editTextDate = view?.findViewById<EditText>(R.id.editTextDate)
            editTextDate?.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }


}
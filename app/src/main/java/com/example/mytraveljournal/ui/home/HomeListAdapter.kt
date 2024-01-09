package com.example.mytraveljournal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraveljournal.R
import com.example.mytraveljournal.TravelData.Travel
import com.example.mytraveljournal.TravelData.TravelViewModel

class HomeListAdapter(private val onStarClickListener: (Travel) -> Unit) : RecyclerView.Adapter<HomeListAdapter.MyViewHolder>() {

    private var travelList = emptyList<Travel>()


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val travelTitle: TextView = itemView.findViewById(R.id.placeNameList)
        val date: TextView = itemView.findViewById(R.id.dateList)
        val star: ImageButton = itemView.findViewById(R.id.StarList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return travelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTravel = travelList[position]

        holder.travelTitle.text = currentTravel.name
        holder.date.text = currentTravel.date

        holder.star.setImageResource(
            if (currentTravel.favorite) R.drawable.full_star_24 else R.drawable.empty_star_24
        )
        holder.star.setOnClickListener {
            // Update the favorite status
            currentTravel.favorite = !currentTravel.favorite

            // Update star image
            holder.star.setImageResource(
                if (currentTravel.favorite) R.drawable.full_star_24 else R.drawable.empty_star_24
            )

            // update dataBase
            onStarClickListener.invoke(currentTravel)
        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener{
            val action = HomeFragmentDirections.actionNavHomeToEditFragment(currentTravel)
            holder.itemView.findNavController().navigate(action)
        }

    }

    fun setData(travel: List<Travel>){
        this.travelList = travel
        notifyDataSetChanged()
    }
}
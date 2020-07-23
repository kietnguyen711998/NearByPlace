package com.example.nearbyplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbyplace.R
import com.example.nearbyplace.model.nearby.NearByPlace

class CustomRecyclerAdapter (val context : Context, val detailsList: ArrayList<NearByPlace>) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>(){
    override fun onBindViewHolder(holder: CustomRecyclerAdapter.ViewHolder, position: Int) {
        holder?.place_name?.text = detailsList[position].results.get(0).name
//        val image_url: String = detailsList.get(position).
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place_name = itemView.findViewById<TextView>(R.id.txtName)
        val txtDetails = itemView.findViewById<TextView>(R.id.txtDetails)
        val image = itemView.findViewById<ImageView>(R.id.imgView)
    }

}
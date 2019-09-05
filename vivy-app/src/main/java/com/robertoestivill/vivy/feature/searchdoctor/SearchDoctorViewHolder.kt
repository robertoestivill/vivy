package com.robertoestivill.vivy.feature.searchdoctor

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robertoestivill.vivy.R

class SearchDoctorViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {

  val picture by lazy { layout.findViewById<ImageView>(R.id.search_doctor_item_picture) }
  val name by lazy { layout.findViewById<TextView>(R.id.search_doctor_item_name) }
  val address by lazy { layout.findViewById<TextView>(R.id.search_doctor_item_address) }
}

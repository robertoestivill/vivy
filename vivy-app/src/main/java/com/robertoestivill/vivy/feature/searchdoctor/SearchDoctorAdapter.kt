package com.robertoestivill.vivy.feature.searchdoctor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertoestivill.vivy.R
import com.robertoestivill.vivy.model.DoctorModel
import com.squareup.picasso.Picasso

class SearchDoctorAdapter(
  private val context: Context,
  private val picasso: Picasso
) : RecyclerView.Adapter<SearchDoctorViewHolder>() {

  private var items = mutableListOf<DoctorModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchDoctorViewHolder(
    LayoutInflater.from(context).inflate(R.layout.item_search_doctor, parent, false)
  )

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: SearchDoctorViewHolder, position: Int) {
    val doctor = items[position]

    picasso.load(doctor.pictureUrl)
      .placeholder(R.drawable.ic_profile_placeholder)
      .fit()
      .into(holder.picture)

    holder.name.text = doctor.name
    holder.address.text = doctor.address
  }

  fun addItems(isNewSearch: Boolean, newItems: List<DoctorModel>) {
    if (isNewSearch) {
      items.clear()
    }
    items.addAll(newItems)
    notifyDataSetChanged()
  }
}

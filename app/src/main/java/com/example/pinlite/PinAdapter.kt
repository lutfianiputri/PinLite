package com.example.pinlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinlite.model.Pin

class PinAdapter(
    private val pinList: MutableList<Pin>,
    private val listener: (Pin) -> Unit,
    private val onLongClick: ((Pin, Int) -> Unit)? = null
) : RecyclerView.Adapter<PinAdapter.PinViewHolder>() {

    private var displayList: MutableList<Pin> = pinList.toMutableList()

    inner class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgPin)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pin, parent, false)
        return PinViewHolder(view)
    }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        val pin = displayList[position]

        holder.title.text = pin.title

        when {
            pin.imageRes != null -> {
                holder.image.setImageResource(pin.imageRes)
            }

            pin.imageUri != null -> {
                Glide.with(holder.itemView.context)
                    .load(pin.imageUri)
                    .into(holder.image)
            }

            else -> {
                holder.image.setImageResource(R.drawable.bg_placeholder)
            }
        }

        holder.itemView.setOnClickListener {
            listener(pin)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(pin, position)
            true
        }
    }

    // 🔥 INI KUNCI UTAMA (WAJIB ADA)
    fun updateList(newList: MutableList<Pin>) {
        displayList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        displayList = if (query.isBlank()) {
            pinList.toMutableList()
        } else {
            pinList.filter {
                it.title.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}

package com.example.testovoe14

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class SaleAdapter(val crypts: List<Crypta>) :
    RecyclerView.Adapter<SaleAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItem(position: Int): Crypta {
        return crypts[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buy_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypta = crypts[position]
        holder.crypto_name.text = crypta.name
        val random = Random()
        val randomPercentage = random.nextDouble() * 0.4 - 0.2

        val originalPrice = crypta.price
        val modifiedPrice = (originalPrice * (1 + randomPercentage)).toInt()

        holder.crypto_price.text = modifiedPrice.toString()

        Glide.with(holder.itemView.context).load(crypta.image).into(holder.crypto_icon)
        holder.buy_image.setImageResource(R.drawable.sell)

        holder.buy_image.setOnClickListener {
            itemClickListener?.onItemClick(position)
            crypta.isBought = false
        }
    }

    override fun getItemCount(): Int {
        return crypts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val crypto_icon: ImageView = itemView.findViewById(R.id.crypto_icon)
        val crypto_name: TextView = itemView.findViewById(R.id.crypto_name)
        val crypto_price: TextView = itemView.findViewById(R.id.crypto_price)
        val buy_image: ImageView = itemView.findViewById(R.id.buy_image)

    }
}
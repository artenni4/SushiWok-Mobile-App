package com.example.sushiwokmobile

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

class RecyclerViewAdapter(private val itemList: ArrayList<OrderDataHolder>) : Adapter<RecyclerViewAdapter.InnerViewHolder>() {

    private var listener: OnItemClickListener? = null
    var buttonImageSrc: Drawable? = null
        set(value) {
            field = value
        }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onButtonClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class InnerViewHolder(itemView: View, ln: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val tvReadyTime: TextView = itemView.findViewById(R.id.tv_ready_take_time)
        val tvShopAddress: TextView = itemView.findViewById(R.id.tv_shop_address)
        val tvDestinationAddress: TextView = itemView.findViewById(R.id.tv_street_address)
        val btnTakeOrder: ImageButton = itemView.findViewById(R.id.btn_take_order)

        init {
            itemView.setOnClickListener {
                if (ln != null) {
                    val position = adapterPosition
                    if (position != NO_POSITION) {
                        ln.onItemClick(position)
                    }
                }
            }


            btnTakeOrder.setOnClickListener {
                if (ln != null) {
                    val position = adapterPosition
                    if (position != NO_POSITION) {
                        ln.onButtonClick(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return InnerViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val currentItem: OrderDataHolder = itemList[position]
        holder.tvReadyTime.text = currentItem.time
        holder.tvShopAddress.text = currentItem.senderName
        holder.tvDestinationAddress.text = currentItem.destination
        holder.btnTakeOrder.setImageDrawable(buttonImageSrc)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}


package com.example.sushiwokmobile

import android.graphics.drawable.Drawable
import com.google.firebase.database.ChildEventListener

object OrdersListsHolder {
    private val activeOrderDataArray: ArrayList<OrderDataHolder> = ArrayList()
    private val takenOrderDataArray: ArrayList<OrderDataHolder> = ArrayList()
    private val activeOrdersListAdapter: RecyclerViewAdapter = RecyclerViewAdapter(activeOrderDataArray)
    private val takenOrdersListAdapter: RecyclerViewAdapter = RecyclerViewAdapter(takenOrderDataArray)

    fun setActiveOrdersListImageSource(src: Drawable) {
        activeOrdersListAdapter.buttonImageSrc = src
    }
    fun setTakenOrdersListImageSource(src: Drawable) {
        takenOrdersListAdapter.buttonImageSrc = src
    }

    fun setActiveOrdersListOnItemClickListener(listener: RecyclerViewAdapter.OnItemClickListener) {
        activeOrdersListAdapter.setOnItemClickListener(listener)
    }
    fun setTakenOrdersListOnItemClickListener(listener: RecyclerViewAdapter.OnItemClickListener) {
        takenOrdersListAdapter.setOnItemClickListener(listener)
    }

    fun getActiveOrdersListAdapter(): RecyclerViewAdapter {
        return activeOrdersListAdapter
    }
    fun getTakenOrdersListAdapter(): RecyclerViewAdapter {
        return takenOrdersListAdapter
    }

    fun addActiveOrder(item: OrderDataHolder, atIndex: Int? = null) {
        val orderId = item.ID
        var alreadyIn = false
        for (order in activeOrderDataArray) {
            if (order.ID == orderId) {
                alreadyIn = true
            }
        }

        if(!alreadyIn) {
            if (atIndex == null) {
                activeOrderDataArray.add(item)
                activeOrdersListAdapter.notifyItemInserted(activeOrderDataArray.size - 1)
            } else {
                activeOrderDataArray.add(atIndex, item)
                activeOrdersListAdapter.notifyItemInserted(atIndex)
            }
        }
    }
    fun removeActiveOrderAtIndex(atIndex: Int) {
        activeOrderDataArray.removeAt(atIndex)
        activeOrdersListAdapter.notifyItemRemoved(atIndex)
    }
    fun removeActiveOrderById(ID: String) {
        var removeItem: OrderDataHolder? = null
        for (i in activeOrderDataArray) {
            if (i.ID == ID) {
                removeItem = i
                break
            }
        }
        if (removeItem != null) {
            activeOrderDataArray.remove(removeItem)
        }
        activeOrdersListAdapter.notifyDataSetChanged()
    }
    fun getActiveOrderItem(index: Int): OrderDataHolder {
        return activeOrderDataArray[index]
    }
    fun addTakenOrder(item: OrderDataHolder, atIndex: Int? = null) {
        val orderId = item.ID
        var alreadyIn = false
        for (order in takenOrderDataArray) {
            if (order.ID == orderId) {
                alreadyIn = true
            }
        }

        if(!alreadyIn) {
            if (atIndex == null) {
                takenOrderDataArray.add(item)
                takenOrdersListAdapter.notifyItemInserted(takenOrderDataArray.size - 1)
            } else {
                takenOrderDataArray.add(atIndex, item)
                takenOrdersListAdapter.notifyItemInserted(atIndex)
            }
        }
    }
    fun removeTakenOrderAtIndex(atIndex: Int) {
        takenOrderDataArray.removeAt(atIndex)
        takenOrdersListAdapter.notifyItemRemoved(atIndex)
    }
    fun removeTakenOrderById(ID: String) {
        var removeItem: OrderDataHolder? = null
        for (i in takenOrderDataArray) {
            if (i.ID == ID) {
                removeItem = i
                break
            }
        }
        if (removeItem != null) {
            takenOrderDataArray.remove(removeItem)
        }
        takenOrdersListAdapter.notifyDataSetChanged()
    }
    fun getTakenOrderItem(index: Int): OrderDataHolder {
        return takenOrderDataArray[index]
    }

    fun clearAllLists() {
        activeOrderDataArray.clear()
        takenOrderDataArray.clear()
    }
}
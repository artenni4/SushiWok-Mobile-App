package com.example.sushiwokmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sushiwokmobile.RecyclerViewAdapter.OnItemClickListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import maes.tech.intentanim.CustomIntent


class ActiveOrdersTab : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val database = Firebase.database.reference
    private val functions = FirebaseFunctions.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = buildRecyclerView(inflater, container)



        database.child("ActiveOrders").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val id = snapshot.child("ID").value.toString()
                val shopAddress = snapshot.child("ShopAddress").value.toString()
                val destinationAddress = snapshot.child("DestinationAddress").value.toString()
                val phone = snapshot.child("Phone").value.toString()
                val readyTime = snapshot.child("ReadyTime").value.toString()
                val takeTime = snapshot.child("TakeTime").value.toString()
                val description = snapshot.child("Description").value.toString()
                OrdersListsHolder.addActiveOrder(OrderDataHolder(id, shopAddress, destinationAddress, readyTime, takeTime, phone, description))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                OrdersListsHolder.removeActiveOrderById(snapshot.child("ID").value.toString())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                OrdersListsHolder.removeActiveOrderById(snapshot.child("ID").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Связь с сервером потеряна", Toast.LENGTH_SHORT).show()
            }

        })

        return rootView
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buildRecyclerView (inflater: LayoutInflater, container: ViewGroup?): View {
        val rootView = inflater.inflate(R.layout.fragment_orders_tab, container, false)

        OrdersListsHolder.setActiveOrdersListImageSource(requireContext().resources.getDrawable(R.drawable.ic_baseline_add_24))
        OrdersListsHolder.setActiveOrdersListOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, OrderInformationActivity::class.java)
                intent.putExtra("order_information", OrdersListsHolder.getActiveOrderItem(position))
                startActivity(intent)
                CustomIntent.customType(context, "left-to-right")
            }

            override fun onButtonClick(position: Int) {
                val id = OrdersListsHolder.getActiveOrderItem(position).ID
                AlertDialog.Builder(context)
                    .setTitle("Подтверждение")
                    .setMessage("Вы действительно хотите взять заказ?")
                    .setPositiveButton("Да") { dialogInterface, i ->
                        dialogInterface.cancel()
                        val checkId: String? = try {
                            OrdersListsHolder.getActiveOrderItem(position).ID
                        } catch (e : java.lang.IndexOutOfBoundsException) {
                            null
                        }
                        if (id == checkId) {
                            val loading = LoadingDialog(activity as Activity)
                            loading.startLoadingDialog()
                            Log.d(null, "Take order # ${OrdersListsHolder.getActiveOrderItem(position).ID}")
                            functions.getHttpsCallable("takeOrder").call(hashMapOf("id" to OrdersListsHolder.getActiveOrderItem(position).ID)).addOnSuccessListener {
                                Log.d(null, "Success")
                                Log.d(null, it.data.toString())
                                OrdersListsHolder.removeActiveOrderAtIndex(position)
                                loading.dismissDialog()
                                Toast.makeText(context, "Заказ взят", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Log.d(null, "Fail")
                                loading.dismissDialog()
                                Toast.makeText(context, "Отсутсвует соединение с интернетом", Toast.LENGTH_SHORT).show()
                                //throw it
                            }
                        } else {
                            Toast.makeText(context, "Похоже, кто-то уже взял этот заказ", Toast.LENGTH_LONG).show()
                        }
                    }
                    .setNegativeButton("Нет") { dialogInterface, i ->
                        dialogInterface.cancel()
                    }
                    .setCancelable(true).create().show()

            }
        })
        recyclerView = rootView.findViewById(R.id.recycler_list_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = OrdersListsHolder.getActiveOrdersListAdapter()
        recyclerView.itemAnimator = DefaultItemAnimator()


        return rootView
    }

}






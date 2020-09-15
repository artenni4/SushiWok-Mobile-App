package com.example.sushiwokmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sushiwokmobile.RecyclerViewAdapter.OnItemClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.consumesAll
import maes.tech.intentanim.CustomIntent

class TakenOrdersTab : Fragment() {
    private lateinit var recyclerViewActive: RecyclerView
    val functions = FirebaseFunctions.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = buildRecyclerView(inflater, container)

        val database = Firebase.database.reference

        database.child("TakenOrders").addChildEventListener( object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                if (uid == snapshot.child("UID").value.toString()) {
                    val id = snapshot.child("ID").value.toString()
                    val shopAddress = snapshot.child("ShopAddress").value.toString()
                    val destinationAddress = snapshot.child("DestinationAddress").value.toString()
                    val phone = snapshot.child("Phone").value.toString()
                    val readyTime = snapshot.child("ReadyTime").value.toString()
                    val takeTime = snapshot.child("TakeTime").value.toString()
                    val description = snapshot.child("Description").value.toString()
                    OrdersListsHolder.addTakenOrder(OrderDataHolder(id, shopAddress, destinationAddress, readyTime, takeTime, phone, description))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                OrdersListsHolder.removeTakenOrderById(snapshot.child("ID").value.toString())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                OrdersListsHolder.removeTakenOrderById(snapshot.child("ID").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Связь с сервером потеряна", Toast.LENGTH_SHORT).show()
            }

        })

        return rootView
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buildRecyclerView (inflater: LayoutInflater, container: ViewGroup?): View {
        val rootView = inflater.inflate(R.layout.fragment_active_orders_tab, container, false)

        OrdersListsHolder.setTakenOrdersListImageSource(requireContext().resources.getDrawable(R.drawable.ic_baseline_check_24))
        OrdersListsHolder.setTakenOrdersListOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, OrderInformationActivity::class.java)
                intent.putExtra("show_extra", true)
                intent.putExtra("order_information", OrdersListsHolder.getTakenOrderItem(position))
                startActivity(intent)
                CustomIntent.customType(context, "left-to-right")
            }

            override fun onButtonClick(position: Int) {
                AlertDialog.Builder(context)
                    .setTitle("Подтверждение")
                    .setMessage("Вы действительно доставили заказ?")
                    .setPositiveButton("Да") { dialogInterface, i ->
                        dialogInterface.cancel()
                        val loading = LoadingDialog(activity as Activity)
                        loading.startLoadingDialog()
                        functions.getHttpsCallable("deliverOrder").call(hashMapOf("id" to OrdersListsHolder.getTakenOrderItem(position).ID)).addOnSuccessListener {
                            Toast.makeText(context, "Заказ доставлен", Toast.LENGTH_SHORT).show()
                            OrdersListsHolder.removeTakenOrderAtIndex(position)
                            loading.dismissDialog()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Ошибка, попробуйте ещё раз", Toast.LENGTH_SHORT).show()
                            loading.dismissDialog()
                            throw it
                        }
                    }
                    .setNegativeButton("Нет") { dialogInterface, i ->
                        dialogInterface.cancel()
                    }
                    .setCancelable(true).create().show()
            }

        })

        recyclerViewActive = rootView.findViewById(R.id.recycler_list_view_active)
        recyclerViewActive.layoutManager = LinearLayoutManager(activity)
        recyclerViewActive.adapter = OrdersListsHolder.getTakenOrdersListAdapter()
        recyclerViewActive.itemAnimator = DefaultItemAnimator()

        return rootView
    }

}
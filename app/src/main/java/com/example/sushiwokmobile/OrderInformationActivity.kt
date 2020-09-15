package com.example.sushiwokmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.functions.FirebaseFunctions
import maes.tech.intentanim.CustomIntent


class OrderInformationActivity : AppCompatActivity() {

    private val functions = FirebaseFunctions.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_information)

        val tvSenderName = findViewById<TextView>(R.id.info_take_from)
        val tvDestination = findViewById<TextView>(R.id.info_deliver_to)
        val tvTime = findViewById<TextView>(R.id.info_ready_take_time)
        val tvPhoneNumber = findViewById<TextView>(R.id.info_phone_number)
        val tvDescription = findViewById<TextView>(R.id.info_description)
        val fabCall = findViewById<FloatingActionButton>(R.id.fab_call_to_client)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_order)

        val showExtraButtons = intent.getBooleanExtra("show_extra", false)
        val parcelInfo = intent.getParcelableExtra<OrderDataHolder>("order_information")

        tvSenderName.text = "${resources.getText(R.string.info_take_from)} ${parcelInfo?.senderName.toString()}"
        tvDestination.text = "${resources.getText(R.string.info_deliver_to)} ${parcelInfo?.destination.toString()}"
        tvTime.text = "${resources.getText(R.string.info_time)} ${parcelInfo?.time.toString()}"
        tvPhoneNumber.text = "${resources.getText(R.string.info_phone_number)} ${parcelInfo?.phoneNumber.toString()}"
        tvDescription.text = "${resources.getText(R.string.description)} ${parcelInfo?.generalDescription.toString()}"
        if (showExtraButtons) {
            fabCall.visibility = View.VISIBLE
            btnCancel.visibility = View.VISIBLE
        }
        else {
            fabCall.visibility = View.GONE
            btnCancel.visibility = View.GONE
        }

        fabCall.setOnClickListener {
            val number: String = parcelInfo?.phoneNumber.toString()
            if (number.isNotEmpty()) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
            } else {
                Toast.makeText(applicationContext, "Номер не указан, возможно, он есть в чеке", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Подтверждение")
                .setMessage("Вы действительно хотите отменить заказ?")
                .setPositiveButton("Да") { dialogInterface, i ->
                    dialogInterface.cancel()
                    val loading = LoadingDialog(this)
                    loading.startLoadingDialog()
                    btnCancel.isEnabled = false
                    fabCall.isEnabled = false
                    btnCancel.setTextColor(resources.getColor(R.color.colorInactivity))
                    functions.getHttpsCallable("cancelOrder").call(hashMapOf("id" to parcelInfo!!.ID)).addOnSuccessListener {
                        loading.dismissDialog()
                        finish()
                        Toast.makeText(applicationContext, "Доставка успешно отменена", Toast.LENGTH_LONG).show()
                        OrdersListsHolder.removeTakenOrderById(parcelInfo.ID)
                    }.addOnFailureListener {
                        loading.dismissDialog()
                        Toast.makeText(applicationContext, "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show()
                        //throw it
                        Toast.makeText(applicationContext, "Отсутсвует соединение с интернетом", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Нет") { dialogInterface, i ->
                    dialogInterface.cancel()
                }
                .setCancelable(true).create().show()

        }

    }

    override fun finish() {
        super.finish()
        CustomIntent.customType(this, "right-to-left")
    }
}
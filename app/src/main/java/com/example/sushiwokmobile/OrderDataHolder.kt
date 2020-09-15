package com.example.sushiwokmobile

import android.os.Parcel
import android.os.Parcelable

class OrderDataHolder(
    var ID: String,
    var senderName: String,
    var destination: String,
    var orderReady: String,
    var takeOrder: String,
    var phoneNumber: String,
    var generalDescription: String) : Parcelable {
    val time: String
        get() = "$orderReady / $takeOrder"


    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(ID)
        p0?.writeString(senderName)
        p0?.writeString(destination)
        p0?.writeString(orderReady)
        p0?.writeString(takeOrder)
        p0?.writeString(phoneNumber)
        p0?.writeString(generalDescription)
    }

    companion object CREATOR : Parcelable.Creator<OrderDataHolder> {
        override fun createFromParcel(parcel: Parcel): OrderDataHolder {
            return OrderDataHolder(parcel)
        }

        override fun newArray(size: Int): Array<OrderDataHolder?> {
            return arrayOfNulls(size)
        }
    }


}
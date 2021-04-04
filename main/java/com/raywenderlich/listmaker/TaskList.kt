package com.raywenderlich.listmaker

import android.os.Parcel
import android.os.Parcelable

class TaskList constructor(val name: String, val tasks: ArrayList<String> =
    ArrayList()) : Parcelable {
    // 1 - Adding second constructor so TaskList object can be created from Parcel.
    //     readString - grabs title; createStringArrayList - grabs task list. Passes into primary constructor.
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.createStringArrayList()!!
    )

    override fun describeContents() = 0

    // 2 - Creating Parcel from TaskList object.
    override fun writeToParcel(dest: Parcel, flags: Int)  {
        dest.writeString(name)
        dest.writeStringList(tasks)
    }

    // 3 - Creating companion object to override certain functions.
    companion object CREATOR : Parcelable.Creator<TaskList> {
        // 4 - Overrides to pass parcel to give back TaskList with all data from Parcel.
        override fun createFromParcel(source: Parcel): TaskList =
            TaskList(source)
        override fun newArray(size: Int): Array<TaskList?> =
            arrayOfNulls(size)
    }


}
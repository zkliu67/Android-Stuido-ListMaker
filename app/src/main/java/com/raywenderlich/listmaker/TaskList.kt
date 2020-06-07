package com.raywenderlich.listmaker

import android.os.Parcel
import android.os.Parcelable
import android.os.WorkSource

// Parcelable helps to convert types to the intent may accept:
// such as strings, ints, floats etc.
class TaskList constructor (val name: String, val hashedTasks: ArrayList<String> = ArrayList()):
    Parcelable{
    // name: the exact input string.

    // Reading from a Parcel: Add a second constructor so that
    // a TaskList object can be created from a passed-in Parcel
    // 1. Grabs the values from the Parcel and passed into the primary construction.
    // !! non-null assertion operation to get non-optional value.
    constructor(source: Parcel): this(
        source.readString()!!,
        source.createStringArrayList()!!
    )

    override fun describeContents() = 0

    // Called when a Parcel need to be created from the TaskList object.
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeStringList(hashedTasks)
    }

    companion object CREATOR: Parcelable.Creator<TaskList> {
        override fun createFromParcel(source: Parcel): TaskList =
            TaskList(source)

        override fun newArray(size: Int): Array<TaskList?> =
            arrayOfNulls(size)
    }
}
package ru.andrewkir.hse_mooc.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Rating(
    val `external`: External,
    val `internal`: Internal
) : Parcelable
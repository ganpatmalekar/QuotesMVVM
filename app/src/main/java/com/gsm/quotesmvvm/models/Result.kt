package com.gsm.quotesmvvm.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("quote")
data class Result(
    @PrimaryKey(autoGenerate = true)
    val quoteId: Int,
    val _id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val tags: ArrayList<String> = arrayListOf(),
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
) : Parcelable
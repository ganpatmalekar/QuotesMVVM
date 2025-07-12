package com.gsm.quotesmvvm.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromJsonToList(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(value, listType) ?: arrayListOf()
    }

    @TypeConverter
    fun fromListToJson(list: ArrayList<String>): String {
        return gson.toJson(list)
    }
}
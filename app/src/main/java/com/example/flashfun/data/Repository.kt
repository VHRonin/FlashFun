package com.example.flashfun.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Repository {

    private lateinit var sharedPreferences: SharedPreferences
    const val SHARED_PREFS_KEY = "cards"
    const val PREFS_SAVE_KEY = "cards_key"
    val gson = Gson()

    private var _cardSets: MutableList<FlashCardSet> = mutableListOf()

    val cardSets: List<FlashCardSet> get() = _cardSets

    fun getSetById(id: String): FlashCardSet? = _cardSets.find { it.id == id }

    fun addSet(set: FlashCardSet) {
        _cardSets.add(set)
    }

    fun deleteSet(id: String) {
        _cardSets.removeAll { it.id == id }
    }

    fun load(context: Context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)

        val type = object : TypeToken<List<FlashCardSet>>() {}.type

        val defaultValue = gson.toJson(_cardSets)

        val loadedSets = sharedPreferences.getString(PREFS_SAVE_KEY, defaultValue)

        val sets: MutableList<FlashCardSet> = gson.fromJson(loadedSets, type)

        _cardSets = sets

    }

    fun save(){
        val json = gson.toJson(_cardSets)
        sharedPreferences.edit{
            putString(PREFS_SAVE_KEY, json)
        }
    }
}

package com.example.proba2.data.model

import androidx.room.TypeConverter
import com.example.proba2.breeds.api.model.Weight
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class Converter {

    @TypeConverter
    fun fromWeight(weight: Weight): String {
        return Json.encodeToString(weight)
    }

    @TypeConverter
    fun toWeight(json: String): Weight {
        return Json.decodeFromString(json)
    }
}

package com.example.proba2.networking.serialization

import kotlinx.serialization.json.Json

val NetworkingJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}
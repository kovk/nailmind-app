package com.nailmind.app.data.api

import com.google.gson.Gson
import com.google.gson.JsonParser

internal class MeimeiSseParser(
    private val gson: Gson = Gson()
) {
    fun parse(eventName: String, rawData: String): MeimeiStreamEvent? {
        if (rawData.isBlank()) return null
        val payload = runCatching { JsonParser.parseString(rawData).asJsonObject }.getOrNull()

        return when (eventName) {
            "status" -> MeimeiStreamEvent.Status(
                stage = payload?.get("stage")?.asString.orEmpty(),
                message = payload?.get("message")?.asString.orEmpty()
            )
            "delta" -> payload?.get("text")?.asString
                ?.takeIf { it.isNotEmpty() }
                ?.let(MeimeiStreamEvent::Delta)
            "result" -> runCatching {
                MeimeiStreamEvent.Result(gson.fromJson(rawData, MeimeiChatResponse::class.java))
            }.getOrNull()
            "error" -> MeimeiStreamEvent.Error(
                payload?.get("message")?.asString?.ifBlank { null } ?: "小美暂时无法回复"
            )
            "done" -> MeimeiStreamEvent.Done
            else -> null
        }
    }
}

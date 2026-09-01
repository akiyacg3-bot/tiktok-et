package com.example.network

import android.util.Log
import com.example.BuildConfig
import com.example.data.MockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiHashtagService {
    private const val TAG = "GeminiHashtagService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Calls Gemini API to generate trending Ethiopian hashtags based on context.
     * Returns a list of strings with '#' prefix.
     */
    suspend fun generateTrendingEthiopianHashtags(
        contextPrompt: String = "Ethiopian TikTok cultural dance, music, coffee ceremony, and lifestyle vibes"
    ): List<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            logDebug(TAG, "API key is placeholder or blank. Using fallback curated Ethiopian tags.")
            return@withContext getFallbackEthiopianTags(contextPrompt)
        }

        try {
            val endpoint = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"

            val prompt = """
                You are a social media trend analyst specializing in TikTok in Ethiopia and East Africa (Habesha culture).
                Generate 8 to 12 popular, viral, and culturally authentic Ethiopian TikTok hashtags for this video context:
                "$contextPrompt"

                Include a mix of popular tags like #HabeshaTikTok, #EthioVibes, music, cities (Addis, Gondar, Hawassa, Bahir Dar), cultural moments (Buna, Gursha, Eskista, Timket), and current Ethiopian Gen-Z trends.
                Return ONLY a comma-separated or space-separated list of hashtags starting with #, with no extra text or numbering.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                logWarn(TAG, "Gemini API error: HTTP ${response.code}")
                return@withContext getFallbackEthiopianTags(contextPrompt)
            }

            val responseString = response.body?.string() ?: ""
            val parsedTags = parseHashtagsFromResponse(responseString)

            if (parsedTags.isNotEmpty()) {
                parsedTags
            } else {
                getFallbackEthiopianTags(contextPrompt)
            }
        } catch (e: Exception) {
            logError(TAG, "Exception calling Gemini API: ${e.message}")
            getFallbackEthiopianTags(contextPrompt)
        }
    }

    private fun parseHashtagsFromResponse(jsonResponse: String): List<String> {
        return try {
            val root = JSONObject(jsonResponse)
            val candidates = root.optJSONArray("candidates") ?: return emptyList()
            if (candidates.length() == 0) return emptyList()

            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return emptyList()
            val parts = content.optJSONArray("parts") ?: return emptyList()
            if (parts.length() == 0) return emptyList()

            val rawText = parts.getJSONObject(0).optString("text", "")
            extractHashtags(rawText)
        } catch (e: Exception) {
            logError(TAG, "Error parsing Gemini response: ${e.message}")
            emptyList()
        }
    }

    private fun logDebug(tag: String, msg: String) {
        try { Log.d(tag, msg) } catch (e: Throwable) { println("[$tag] $msg") }
    }

    private fun logWarn(tag: String, msg: String) {
        try { Log.w(tag, msg) } catch (e: Throwable) { println("[$tag] WARN: $msg") }
    }

    private fun logError(tag: String, msg: String) {
        try { Log.e(tag, msg) } catch (e: Throwable) { System.err.println("[$tag] ERROR: $msg") }
    }

    private fun extractHashtags(text: String): List<String> {
        val regex = Regex("#[A-Za-z0-9_\\u1200-\\u137F]+")
        val matches = regex.findAll(text).map { it.value }.distinct().toList()
        return if (matches.isNotEmpty()) {
            matches
        } else {
            text.split(",", " ", "\n")
                .map { it.trim().removePrefix("#") }
                .filter { it.isNotBlank() }
                .map { "#$it" }
                .distinct()
        }
    }

    private fun getFallbackEthiopianTags(context: String): List<String> {
        val lower = context.lowercase()
        return when {
            lower.contains("coffee") || lower.contains("buna") -> listOf(
                "#BunaTetu", "#EthiopianCoffee", "#HabeshaTikTok", "#AddisAbaba",
                "#EthioVibes", "#CoffeeCeremony", "#JebenaBuna", "#HabeshaCulture"
            )
            lower.contains("dance") || lower.contains("eskista") -> listOf(
                "#Eskista", "#EthiopianDance", "#HabeshaTikTok", "#GurshaChallenge",
                "#EthioMusic", "#AddisNightlife", "#GondarDance", "#TikTokeEthiopia"
            )
            lower.contains("travel") || lower.contains("lalibela") -> listOf(
                "#Lalibela", "#VisitEthiopia", "#LandOfOrigins", "#HistoricEthiopia",
                "#HabeshaTikTok", "#AfricaTravel", "#SimienMountains", "#EthioTourism"
            )
            else -> MockData.defaultEthiopianHashtags
        }
    }
}

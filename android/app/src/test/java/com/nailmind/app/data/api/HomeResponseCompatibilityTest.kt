package com.nailmind.app.data.api

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeResponseCompatibilityTest {
    private val gson = Gson()

    @Test
    fun `omitted legacy home collections are empty`() {
        val response = gson.fromJson(
            """
            {
              "hotKeywords": [],
              "recommended": [],
              "hot": []
            }
            """.trimIndent(),
            HomeResponse::class.java
        ).normalized()

        assertEquals(0, response.sceneSections.orEmpty().size)
        assertEquals(0, response.sceneStyles.orEmpty().size)
    }

    @Test
    fun `explicit null home collections and display tags are empty`() {
        val response = gson.fromJson(
            """
            {
              "hotKeywords": [],
              "recommended": [
                {
                  "id": "recommended-1",
                  "name": "Recommended style",
                  "displayTags": null,
                  "imageUrl": "https://example.com/recommended.jpg"
                }
              ],
              "hot": [],
              "sceneSections": null,
              "sceneStyles": {
                "Daily": [
                  {
                    "id": "legacy-scene-1",
                    "name": "Legacy scene style",
                    "displayTags": null,
                    "imageUrl": "https://example.com/legacy.jpg"
                  }
                ]
              }
            }
            """.trimIndent(),
            HomeResponse::class.java
        ).normalized()

        assertEquals(1, response.sceneSections.orEmpty().getValue("Daily").orEmpty().size)
        assertEquals(1, response.sceneStyles.orEmpty().getValue("Daily").orEmpty().size)
        assertEquals(1, response.recommended.size)
        assertEquals(0, response.recommended.single().displayTags.orEmpty().size)
    }

    @Test
    fun `null scene category does not hide valid legacy category`() {
        val response = gson.fromJson(
            """
            {
              "hotKeywords": [],
              "recommended": [],
              "hot": [],
              "sceneSections": { "Daily": null },
              "sceneStyles": {
                "Daily": [
                  {
                    "id": "legacy-scene-1",
                    "name": "Legacy scene style",
                    "tags": [],
                    "colors": [],
                    "imageUrl": "https://example.com/legacy.jpg"
                  }
                ]
              }
            }
            """.trimIndent(),
            HomeResponse::class.java
        ).normalized()

        assertEquals(
            "legacy-scene-1",
            response.sceneSections.orEmpty().getValue("Daily").orEmpty().single().id
        )
    }
}

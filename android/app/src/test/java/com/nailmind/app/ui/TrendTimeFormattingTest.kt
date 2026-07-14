package com.nailmind.app.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrendTimeFormattingTest {
    @Test
    fun `try-on history utc time is displayed in Beijing time`() {
        assertEquals("2026-07-15 12:21", formatBeijingDateTime("2026-07-15T04:21:18"))
    }

    @Test
    fun `utc trend time is displayed in Beijing time`() {
        assertEquals("2026-07-15 05:39", formatBeijingDateTime("2026-07-14T21:39:00Z"))
    }

    @Test
    fun `offset trend time is normalized to Beijing time`() {
        assertEquals("2026-07-15 05:39", formatBeijingDateTime("2026-07-14T21:39:00+00:00"))
    }

    @Test
    fun `naive server utc time is displayed in Beijing time`() {
        assertEquals("2026-07-15 05:39", formatBeijingDateTime("2026-07-14T21:39:00.123456"))
    }

    @Test
    fun `mixed timestamp offsets sort by their actual instant`() {
        val earlier = parseApiInstant("2026-07-15T01:00:00+08:00")
        val later = parseApiInstant("2026-07-14T20:00:00Z")

        assertTrue(earlier != null && later != null && earlier < later)
    }
}

package com.nailmind.app.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class BookingReviewCopyTest {
    @Test
    fun `review reward copy clearly states the next visit discount`() {
        assertEquals("完成评价，下次美甲立减 5 元", BOOKING_REVIEW_REWARD_COPY)
    }
}

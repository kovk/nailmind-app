package com.nailmind.app.data.api

import org.junit.Assert.assertEquals
import org.junit.Test

class RegistrationValidationTest {
    @Test
    fun `short password has a readable local message`() {
        assertEquals("密码至少需要 8 位", registrationPasswordValidationMessage("1234567"))
    }

    @Test
    fun `server detail is shown instead of HTTP status`() {
        assertEquals(
            "该邮箱已经注册",
            registrationErrorMessage(409, """{"detail":"该邮箱已经注册"}""")
        )
    }

    @Test
    fun `unknown registration failure stays readable`() {
        assertEquals("注册失败，请稍后重试", registrationErrorMessage(500, ""))
    }
}

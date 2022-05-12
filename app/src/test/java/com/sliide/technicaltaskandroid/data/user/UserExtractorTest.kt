package com.sliide.technicaltaskandroid.data.user

import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserExtractorTest {

    lateinit var userExtractor: UserExtractor

    companion object {
        const val testUserList = "[{\"id\":6431,\"name\":\"Tara Kocchar\",\"email\":\"tara_kocchar@bins.co\",\"gender\":\"female\",\"status\":\"inactive\"},{\"id\":3954,\"name\":\"Chidaatma Joshi\",\"email\":\"joshi_chidaatma@emmerich-bashirian.info\",\"gender\":\"female\",\"status\":\"active\"}]"
        const val testIndividualUser = "{\"id\":6431,\"name\":\"Tara Kocchar\",\"email\":\"tara_kocchar@bins.co\",\"gender\":\"female\",\"status\":\"inactive\"}"
    }

    @BeforeEach
    fun setUp() {
        userExtractor = UserExtractor()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when providing a valid user list feed then a complete list of usermodels should return`() {
        val demoFeed = JSONArray(testUserList)
        val result = userExtractor.extractUserList(demoFeed)

        assertTrue(result.isNotEmpty())
        assertTrue(result[0].name == "Tara Kocchar")
        assertTrue(result[0].id == 6431)
        assertTrue(result[0].gender == "female")
    }

    @Test
    fun `when providing an invalid user list jsonobject then a null response should be returned`() {
        val result = userExtractor.extractUserList(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `when providing a valid individual user feed then a complete usermodel should return`() {
        val demoFeed = JSONObject(testIndividualUser)
        val result = userExtractor.extractIndividualUser(demoFeed)

        assertTrue(result != null)
        assertTrue(result!!.name == "Tara Kocchar")
        assertTrue(result.id == 6431)
        assertTrue(result.gender == "female")
    }

    @Test
    fun `when providing an invalid user jsonobject then a null response should be returned`() {
        val result = userExtractor.extractIndividualUser(null)
        assertTrue(result == null)
    }
}
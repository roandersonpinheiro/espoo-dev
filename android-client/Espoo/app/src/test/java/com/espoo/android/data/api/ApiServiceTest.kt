package com.espoo.android.data.api

import com.espoo.android.MockResponseFileReader
import com.espoo.android.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var service: ApiService

    @Before
    fun createService() {
        val factory = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(factory))
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
    @Test
    fun `read sample success json file`(){
        val reader = MockResponseFileReader("surveys/success_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `should reach the correct endpoint`() {
        runBlocking {
            /**
             * Test endpoint /Surveys
             */
            mockWebServer.enqueue(MockResponse().setBody("[]"))
            val resultLogin = service.getSurveys().execute()
            val requestSurvey = mockWebServer.takeRequest()
            assertEquals(requestSurvey.path, "/api/v1/surveys")
        }
    }

}
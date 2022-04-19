package com.espoo.android.data.api

import com.espoo.android.MockResponseFileReader
import com.espoo.android.api.ApiService
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
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class ApiSurveysTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var service: ApiService

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    @Test
    fun `read sample success json file`(){
        val reader = MockResponseFileReader("surveys/success_response.json")
        assertNotNull(reader.content)
    }
    @Test
    fun `read sample failed json file`(){
        val reader = MockResponseFileReader("surveys/failed_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `fetch surveys and check response Code 200 returned`(){
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("surveys/success_response.json").content)
        mockWebServer.enqueue(response)
        // Act
        val  actualResponse = service.getSurveys().execute()
        // Assert
        assertEquals(response.toString().contains("200"),actualResponse.code().toString().contains("200"))
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }


}
package com.srikanthpuram.tmtsampleapp

import com.google.gson.Gson
import com.srikanthpuram.tmtsampleapp.FileReader.readJsonFile
import com.srikanthpuram.tmtsampleapp.api.ImageListAPI
import com.srikanthpuram.tmtsampleapp.util.Constants
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ImageListAPITest {

    private var mockWebServer = MockWebServer()

    private lateinit var apiService: ImageListAPI

    @Before
    fun setUp() {
        mockWebServer.start()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client)
            .build()
            .create(ImageListAPI::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testgetImageList()  = runBlocking {

        val response = MockResponse()
            .setResponseCode(200)
            .setBody(readJsonFile("ImageList.json"))
        mockWebServer.enqueue(response)

        val imageListResponse = apiService.getImageList()

        //Assert for Successful Reponse
        assertTrue(imageListResponse.isSuccessful)
    }

}
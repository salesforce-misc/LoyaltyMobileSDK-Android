package com.salesforce.loyalty.mobile.myntorewards

import com.google.gson.Gson
import java.io.InputStreamReader
import java.lang.reflect.Type

class MockResponseFileReader(path: String) {

    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}
fun mockResponse(fileName: String, java: Type): Any {
    val mockResponse = MockResponseFileReader(fileName).content
    return Gson().fromJson(mockResponse, java)
}
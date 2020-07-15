package com.srikanthpuram.tmtsampleapp

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

object FileReader {

    fun readJsonFile(fileName: String) : String {
        val path = "../app/src/main/assets/"
        try {
            var bufferedReader = BufferedReader(InputStreamReader(FileInputStream(path + fileName)))
            val builder = StringBuilder()
            bufferedReader.readLines().forEach {
                builder.append(it)
            }
            return bufferedReader.toString()
        } catch (e: IOException) {
            throw e
        }

    }
}
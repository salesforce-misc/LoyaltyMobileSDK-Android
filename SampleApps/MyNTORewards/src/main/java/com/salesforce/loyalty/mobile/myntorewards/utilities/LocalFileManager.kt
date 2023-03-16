package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.*

object LocalFileManager {

    const val TAG = "LocalFileManager"
    const val DIRECTORY_PROFILE = "Profile"
    const val DIRECTORY_BENEFITS = "Benefits"
    const val DIRECTORY_TRANSACTIONS = "Transactions"
    const val DIRECTORY_PROMOTIONS = "Promotions"
    const val DIRECTORY_VOUCHERS = "Vouchers"

    /**
     * Utility method to save data to internal storage
     *
     * @param context Context
     * @param data The actual data to be saved
     * @param id The unique identifier used to create filename
     * @param folderName Name of the folder to which the data has to be saved
     */
    fun <T> saveData(context: Context, data: T, id: String, folderName: String) {
        val internalDir: File = context.filesDir
        val folderDir = File(internalDir, folderName)
        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }
        val fileName = id + "_" + folderName
        val fileWithinDir: File = File(folderDir, fileName)

        var out =
            FileOutputStream(fileWithinDir)
        var outputStreamWriter = OutputStreamWriter(out)
        try {
            val content = Gson().toJson(data)
            outputStreamWriter.write(content.toString())
            Log.d(TAG, "saveData $content")
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Exception occured when saving data to file ${e.message}")
        } finally {
            outputStreamWriter.close()
            out.close()
        }

    }

    /**
     * Utility method to get data from the internal storage
     *
     * @param context Context
     * @param id The unique identifier used to create filename
     * @param folderName Name of the folder where the data is saved
     * @param type The class to which the retrieved data must be converted to before returning
     * @return T The data in the requested format
     */
    fun <T> getData(context: Context, id: String, folderName: String, type: Class<T>): T? {
        val internalDir: File = context.filesDir
        val folderDir = File(internalDir, folderName)
        if (!folderDir.exists()) {
            return null
        }
        val fileName = id + "_" + folderName
        val fileWithinDir: File = File(folderDir, fileName)
        val input =
            FileInputStream(fileWithinDir)
        try {
            var inputStreamReader = InputStreamReader(input)
            val stringBuilder: StringBuilder = StringBuilder()
            val bufferedReader = BufferedReader(inputStreamReader)

            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            val stringContent = stringBuilder.toString()
            if (stringContent.isNotEmpty()) {
                val content = Gson().fromJson(stringContent, type)
                Log.d(TAG, "getData  $content")
                return content
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Exception occured when getting data from file ${e.message}")
        } finally {
            input.close()
        }

        return null
    }

    /**
     * Deletes all files and folders used for caching purpose
     *
     * @param context Context
     */
    fun clearAllFolders(context: Context) {
        clearFolder(context, DIRECTORY_PROMOTIONS)
        clearFolder(context, DIRECTORY_BENEFITS)
        clearFolder(context, DIRECTORY_PROFILE)
        clearFolder(context, DIRECTORY_VOUCHERS)
        clearFolder(context, DIRECTORY_TRANSACTIONS)
    }

    private fun clearFolder(context: Context, folderName: String) {
        val internalDir: File = context.filesDir
        val folderDir = File(internalDir, folderName)

        if (folderDir.exists() && folderDir.isDirectory) {
            for (child in folderDir.listFiles()) {
                val isDeleted = child.delete()
                Log.d(TAG, "isDeleted: $isDeleted")
            }
            val folderDeleted = folderDir.delete()
            Log.d(TAG, "folderDeleted: $folderDeleted")
        }
    }
}
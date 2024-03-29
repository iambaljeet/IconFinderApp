package com.app.baljeet.iconfinderapp.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import java.io.File


class DirectoryHelper private constructor(context: Context) : ContextWrapper(context) {

    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == extStorageState
        }

    init {
        createFolderDirectories()
    }

    private fun createFolderDirectories() {
        if (isExternalStorageAvailable)
            createDirectory(Constants.ROOT_DIRECTORY_NAME)
    }

    private fun createDirectory(directoryName: String) {
        if (!isDirectoryExists(directoryName)) {
            val file = File(getExternalStorageDirectory(), directoryName)
            file.mkdir()
        }
    }

    private fun isDirectoryExists(directoryName: String): Boolean {
        val file = File("${getExternalStorageDirectory()}/$directoryName")
        return file.isDirectory && file.exists()
    }

    companion object {
        fun createDirectory(context: Context) {
            DirectoryHelper(context)
        }
    }
}
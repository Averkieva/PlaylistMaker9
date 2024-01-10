package com.example.playlistmaker.data.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.playlist.repository.ImagesRepository
import com.example.playlistmaker.ui.playlist.fragment.CreateNewPlaylistFragment
import java.io.File
import java.io.FileOutputStream

class ImagesRepositoryImpl(private val context: Context) : ImagesRepository {
    override fun saveImageToPrivateStorage(uri: Uri): Uri {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover_${fileCount + 1}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, CreateNewPlaylistFragment.quality, outputStream)
        return file.toUri()
    }
}
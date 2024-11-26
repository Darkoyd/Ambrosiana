package com.example.ambrosianaapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StoragePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

class ImageLoader(private val context: Context) {
    companion object {
        private const val CACHE_SIZE = 1024 * 1024 * 20 // 20MB
        private const val DISK_CACHE_SUBDIR = "book_thumbnails"
    }

    // Memory cache
    private val memoryCache = object : LruCache<String, Bitmap>(CACHE_SIZE) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }

    // Disk cache directory
    private val cacheDir = File(context.cacheDir, DISK_CACHE_SUBDIR).apply {
        if (!exists()) mkdirs()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    suspend fun loadImage(key: String): Flow<ImageLoadingState> = flow {
        emit(ImageLoadingState.Loading)

        try {
            // Check memory cache first
            memoryCache.get(key)?.let {
                emit(ImageLoadingState.Success(it))
                return@flow
            }

            // Check disk cache
            val cachedFile = getCachedFile(key)
            if (cachedFile.exists()) {
                val bitmap = withContext(Dispatchers.IO) {
                    BitmapFactory.decodeFile(cachedFile.path)
                }
                if (bitmap != null) {
                    memoryCache.put(key, bitmap)
                    emit(ImageLoadingState.Success(bitmap))
                    return@flow
                }
            }

            // Download from S3

            var tempFile = File(context.cacheDir, "images/$key")
            val dl = Amplify.Storage.downloadFile(StoragePath.fromString("images/$key"), tempFile)
            val file = dl.result().file


            // Decode and optimize bitmap
            val bitmap = withContext(Dispatchers.IO) {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeFile(file.path, options)

                options.inSampleSize = calculateInSampleSize(options, 300, 300)
                options.inJustDecodeBounds = false

                BitmapFactory.decodeFile(file.path, options)
            }

            // Save to both caches
            withContext(Dispatchers.IO) {
                FileOutputStream(cachedFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }
            }
            memoryCache.put(key, bitmap)

            // Clean up file
            file.delete()

            emit(ImageLoadingState.Success(bitmap))
        } catch (e: Exception) {
            emit(ImageLoadingState.Error(e))
        }
    }

    private fun getCachedFile(key: String): File {
        return File(cacheDir, key.hashKey())
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun String.hashKey(): String {
        return MessageDigest.getInstance("MD5")
            .digest(toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    fun clearCache() {
        memoryCache.evictAll()
        cacheDir.listFiles()?.forEach { it.delete() }
    }
}

sealed class ImageLoadingState {
    object Loading : ImageLoadingState()
    data class Success(val bitmap: Bitmap) : ImageLoadingState()
    data class Error(val exception: Exception) : ImageLoadingState()
}

@Composable
fun rememberImageLoader(context: Context): ImageLoader {
    return remember { ImageLoader(context) }
}
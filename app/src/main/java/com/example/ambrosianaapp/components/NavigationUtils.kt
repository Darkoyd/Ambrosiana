package com.example.ambrosianaapp.components

import android.content.Context
import android.content.Intent
import com.example.ambrosianaapp.library.LibraryActivity
import com.example.ambrosianaapp.search.SearchActivity

object NavigationUtils {
    fun navigateToScreen(context: Context, currentScreen: Screen, destinationScreen: Screen) {
        if (currentScreen == destinationScreen) return

        val intent = when (destinationScreen) {
            Screen.SEARCH -> Intent(context, SearchActivity::class.java)
            Screen.LIBRARY -> Intent(context, LibraryActivity::class.java)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        context.startActivity(intent)
    }

    enum class Screen {
        SEARCH,
        LIBRARY,
    }
}
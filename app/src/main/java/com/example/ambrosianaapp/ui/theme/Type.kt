package com.example.ambrosianaapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.ambrosianaapp.R

object AppFont {
    private val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    private val manropeFontName = GoogleFont("Manrope")

    private val manropeFamily = FontFamily(
        Font(googleFont = manropeFontName, fontProvider = provider, weight = FontWeight.Light),
        Font(googleFont = manropeFontName, fontProvider = provider, weight = FontWeight.Normal),
        Font(googleFont = manropeFontName, fontProvider = provider, weight = FontWeight.Medium),
        Font(googleFont = manropeFontName, fontProvider = provider, weight = FontWeight.SemiBold),
        Font(googleFont = manropeFontName, fontProvider = provider, weight = FontWeight.Bold),
    )

    val typography = Typography(
        displayLarge = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ), displayMedium = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ), displaySmall = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ), headlineLarge = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ), headlineMedium = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ), headlineSmall = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ), titleLarge = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ), titleMedium = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ), titleSmall = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ), bodyLarge = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ), bodyMedium = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ), bodySmall = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ), labelLarge = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ), labelMedium = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ), labelSmall = TextStyle(
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}
package com.kelompok4.serena.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 1. Definisikan Font Family SF Pro Display dengan fallback ke default
val SFProDisplay = FontFamily.Default

// 2. Buat data class untuk menampung tiga weight untuk setiap style
data class AppTextStyle(
    val regular: TextStyle,
    val medium: TextStyle,
    val bold: TextStyle
)

// 3. Buat objek yang berisi semua style tipografi
object AppTypography {
    val H1 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 32.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 32.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )
    val H2 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 28.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 28.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 28.sp)
    )
    val H3 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 24.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 24.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    )
    val H4 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 22.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 22.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    )
    val H5 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 20.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 20.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    )
    val H6 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 18.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 18.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    )
    val Body1 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 16.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 16.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    )
    val Subtitle2 = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 14.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 14.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    )
    val Button = AppTextStyle(
        regular = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Normal, fontSize = 12.sp),
        medium = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Medium, fontSize = 12.sp),
        bold = TextStyle(fontFamily = SFProDisplay, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    )
}

// 4. Atur default Typography untuk MaterialTheme agar sesuai dengan design system
val Typography = Typography()
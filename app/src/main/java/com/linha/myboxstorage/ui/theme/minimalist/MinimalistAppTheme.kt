package com.linha.myboxstorage.ui.theme.minimalist

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.linha.myboxstorage.R

@Composable
fun MinimalistAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkThemeColors
        else -> lightThemeColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

private val lightThemeColors = lightColorScheme(
    primary = Color.Black, // Warna utama untuk tombol dan ikon
    secondary = Color.Gray, // Warna sekunder
    onBackground = Color.Black,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White, // Teks pada tombol primer
    onSecondary = Color.Black, // Teks pada tombol sekunder
    onSurface = Color.Black, // Teks pada permukaan
    primaryContainer = Color.White, // Kontainer utama
    onPrimaryContainer = Color.Black // Teks/Label pada kontainer utama (PENTING)
)

private val darkThemeColors = darkColorScheme(
    primary = Color.White, // Warna utama untuk tombol dan ikon
    secondary = Color.Gray,
    onBackground = Color.White,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black, // Teks pada tombol primer
    onSecondary = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFF242424), // Warna gelap Custom dari figma
    onPrimaryContainer = Color.White // Teks/Label pada kontainer utama harus putih
)

val appFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = appFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.2).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = appFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    // yg lain
)
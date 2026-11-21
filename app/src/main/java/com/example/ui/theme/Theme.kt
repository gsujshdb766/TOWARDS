package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = CyanNeon,
  onPrimary = Color(0xFF00363F),
  primaryContainer = CyanDeep,
  onPrimaryContainer = CyanGlow,
  secondary = IndigoNeon,
  onSecondary = Color(0xFF1E1B4B),
  secondaryContainer = IndigoDeep,
  onSecondaryContainer = Color(0xFFC7D2FE),
  tertiary = EmeraldNeon,
  onTertiary = Color(0xFF064E3B),
  tertiaryContainer = Color(0xFF065F46),
  onTertiaryContainer = EmeraldGlow,
  background = DarkBg,
  onBackground = TextPrimaryDark,
  surface = DarkSurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondaryDark,
  outline = DarkSurfaceBorder,
  error = RoseNeon,
  onError = Color.White
)

private val LightColorScheme = lightColorScheme(
  primary = CyanDeep,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFBAF3FF),
  onPrimaryContainer = Color(0xFF001F26),
  secondary = Color(0xFF4338CA),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0E7FF),
  onSecondaryContainer = Color(0xFF1E1B4B),
  tertiary = Color(0xFF047857),
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFFA7F3D0),
  onTertiaryContainer = Color(0xFF064E3B),
  background = LightBg,
  onBackground = TextPrimaryLight,
  surface = LightSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = TextSecondaryLight,
  outline = LightSurfaceBorder,
  error = Color(0xFFBE123C),
  onError = Color.White
)

@Composable
fun OpenDroidTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep high-tech neon personality consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  OpenDroidTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}

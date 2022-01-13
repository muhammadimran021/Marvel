package org.saulmm.marvel.app.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.saulmm.marvel.R

val SecularOne = FontFamily(
    Font(R.font.secularone_regular, FontWeight.Normal)
)

val Nunito = FontFamily(
    Font(R.font.nunito_semi_bold, FontWeight.SemiBold)
)

val MarvelTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 24.sp,
        fontFamily = SecularOne
    ),
    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = SecularOne
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = SecularOne
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito
    )
)
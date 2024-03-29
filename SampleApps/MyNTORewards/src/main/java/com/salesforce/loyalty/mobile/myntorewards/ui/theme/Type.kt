package com.salesforce.loyalty.mobile.myntorewards.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val font_sf_pro = FontFamily(
    Font(R.font.sf_pro)
)

val font_archivo_bold = FontFamily(
    Font(R.font.archivo_bold)
)
val font_archivo = FontFamily(
    Font(R.font.archivo_regular)
)

val font_ibm_plex_mono_regular = FontFamily(Font(R.font.ibm_plex_mono_regular))
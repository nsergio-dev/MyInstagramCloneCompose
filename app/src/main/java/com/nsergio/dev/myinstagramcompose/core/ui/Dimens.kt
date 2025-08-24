package com.nsergio.dev.myinstagramcompose.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DimensDP {
    DP0  { override val dp = 0.dp },
    DP1  { override val dp = 1.dp },
    DP2  { override val dp = 2.dp },
    DP3  { override val dp = 3.dp },
    DP4  { override val dp = 4.dp },
    DP6  { override val dp = 6.dp },
    DP8  { override val dp = 8.dp },
    DP12 { override val dp = 12.dp },
    DP16 { override val dp = 16.dp },
    DP24 { override val dp = 24.dp },
    DP32 { override val dp = 32.dp },
    DP36 { override val dp = 36.dp },
    DP40 { override val dp = 40.dp },
    DP48 { override val dp = 48.dp },
    DP50 { override val dp = 50.dp },
    DP56 { override val dp = 56.dp },
    DP64 { override val dp = 64.dp },
    DP66 { override val dp = 66.dp },
    DP72 { override val dp = 72.dp },
    DP92 { override val dp = 92.dp },
    DP280 { override val dp = 280.dp },
    DP300 { override val dp = 300.dp };

    abstract val dp: Dp
}

enum class DimensSp {

    SP12 { override val sp = 12.sp },
    SP14 { override val sp = 14.sp },
    SP20 { override val sp = 20.sp };

    abstract val sp: TextUnit

}
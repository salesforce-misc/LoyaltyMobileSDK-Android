package com.salesforce.loyalty.mobile.myntorewards.views


import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.myntorewards.views.home.PromotionCardRow

//Main Activity Application Entry Point
class LoyaltyAppBaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {

            //HomeTabScreen()
            MainScreenStart()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PromotionCardRow(NavController(context = LocalContext.current))
    //MainScreenStart()
}


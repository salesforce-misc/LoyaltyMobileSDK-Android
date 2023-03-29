package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs

@Composable
fun HomeSubViewHeader(heading: String, navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = heading,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(id = R.string.view_all),
            fontWeight = FontWeight.Bold,
            color = VibrantPurple40,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier.clickable {
                navController.navigate(BottomNavTabs.MyOffers.route) {

                    navController.graph.startDestinationRoute?.let { screen_route ->
                        popUpTo(screen_route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun HomeSubViewHeaderVoucher(
    heading: Int,
    navController: NavController,
    openHomeScreen: (homeScreenState: HomeScreenState) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = stringResource(id = heading),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(id = R.string.view_all),
            fontWeight = FontWeight.Bold,
            color = VibrantPurple40,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier.clickable {
                openHomeScreen(HomeScreenState.VOUCHER_VIEW)
            }
        )
    }
}
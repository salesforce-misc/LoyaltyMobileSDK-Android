package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextDarkGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs

@Composable
fun BottomNavigationUI(
    bottomTabsNavController: NavController,
    bottomBarState: MutableState<Boolean>
) {
    val items = listOf(
        BottomNavTabs.Home,
        BottomNavTabs.MyOffers,
        BottomNavTabs.MyProfile,
        //BottomNavTabs.Redeem,  //part of UX but not part of MVP
        BottomNavTabs.More
    )
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {

            BottomNavigation(
                backgroundColor = colorResource(id = R.color.white),
                contentColor = Color.Black,
                modifier = Modifier.height(83.dp)
            ) {
                val navBackStackEntry by bottomTabsNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .fillMaxWidth(),
                        icon = {
                            Icon(
                                painterResource(id = item.iconID),
                                contentDescription = stringResource(id = item.titleID),
                                modifier = Modifier.size(24.dp, 24.dp)
                            )
                        },
                        label = {   Text(
                            text = stringResource(id = item.titleID),
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                        ) },
                        selectedContentColor = VibrantPurple40,
                        unselectedContentColor = TextDarkGray,
                        alwaysShowLabel = true,
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomTabsNavController.navigate(item.route) {

                                bottomTabsNavController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(0)
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        })
}
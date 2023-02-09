package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R


@Composable
fun Navigation()
{
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination= Screen.OnboardingScreen.route)
    {
        composable(route = Screen.OnboardingScreen.route){
            MainScreen(navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController)
        }
    }
}

@Composable
fun MainScreen(navController: NavController){
    OnboardingScreenBox(navController)
}

@Composable
fun LoginScreen(navController: NavController){
        //LoginUI(navController)
}

@Composable
fun HomeScreen(navController: NavController){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 16.dp).fillMaxHeight().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(text = "Home Page", modifier = Modifier.fillMaxHeight().fillMaxHeight().align(alignment = Alignment.CenterHorizontally),
            textAlign = TextAlign.Center)
    }
}

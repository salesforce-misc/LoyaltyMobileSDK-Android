package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get

@Composable
fun EnrollmentCongratulationsView(navController: NavController, closePopups: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {

            Image(
                painter = painterResource(id = R.drawable.congratulations),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Spacer(modifier = Modifier.height(25.dp))
            Image(
                painter = painterResource(id = R.drawable.gift),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                modifier = Modifier.width(135.dp),
                contentScale = ContentScale.FillWidth,

                )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            var programName = PrefHelper.customPrefs(LocalContext.current)
                .get("programName_key", "")
            var emailID = PrefHelper.customPrefs(LocalContext.current)
                .get("emailID_key", "")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome on board!",
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "We’re thrilled that you joined ${programName}. You’re on your way to earning points and receiving exclusive rewards.\n",
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(77.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append("We’ve sent an email to ")
                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(emailID)
                    }
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append(" with more details.")
                    }
                }, fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 59.dp, end = 59.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(92.dp))


            Button(  modifier = Modifier
                .fillMaxWidth(), onClick = {
                closePopups()
                navController.navigate(Screen.HomeScreen.route)
            },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.join_text),
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                )
            }


            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
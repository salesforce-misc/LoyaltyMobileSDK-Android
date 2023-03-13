package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo_bold
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.BenefitListView
import com.salesforce.loyalty.mobile.myntorewards.views.ListItemMyBenefit

@Composable
fun VoucherFullScreen(openHomeScreen: (homeScreenState: HomeScreenState) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.background(Color.White)
    )
    {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
                .clickable {
                    openHomeScreen(HomeScreenState.MAIN_VIEW)
                }
        )
        Text(
            text = stringResource(R.string.vouchers),
            fontFamily = font_archivo_bold,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 11.5.dp, bottom = 11.5.dp, start = 16.dp, end = 16.dp)
        )
        VoucherListView(Modifier.fillMaxHeight())
    }


}

@Composable
fun VoucherListView(modifier: Modifier) {

    /* val model: MembershipBenefitViewModel = viewModel()
     val membershipBenefit by model.membershipBenefitLiveData.observeAsState() // collecting livedata as state
     val context: Context = LocalContext.current
     //calling member benefit
     model.memberBenefitAPI(context)

     membershipBenefit?.let {
         LazyColumn(modifier = modifier) {
             items(it) {
                 ListItemMyBenefit(it)
             }
         }
     }*/

}
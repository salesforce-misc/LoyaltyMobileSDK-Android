package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.Assets.ViewPagerSupport.getBenefitsLogo
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit

@Composable
fun MyBenefitView(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit) {

    val model: MembershipBenefitViewModel = viewModel()
    val membershipBenefit by model.membershipBenefitLiveData.observeAsState() // collecting livedata as state
    val context: Context = LocalContext.current
    //calling member benefit
    model.memberBenefitAPI(context)

    Column(
        modifier = Modifier
            .background(MyProfileScreenBG)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)

    ) {
        ProfileSubViewHeader("My Benefits")
        {
            openProfileScreen(it)
        }
        membershipBenefit?.let { LazyRowItemsDemo(it) }
    }

}



@Composable
fun LazyRowItemsDemo(itemViewStates: List<MemberBenefit>) {
    LazyColumn(modifier = Modifier.height(200.dp)) {
        items(itemViewStates) {
            ListItemMyBenefit(it)
        }
    }
}

@Composable
fun ListItemMyBenefit(benefit: MemberBenefit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MyProfileScreenBG)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {

        Column(modifier = Modifier
            .size(32.dp)
            .background(Color.White, CircleShape)
            .wrapContentSize(Alignment.Center)

        ) {

            benefit.benefitTypeName?.let {
                Image(
                    painter = painterResource(getBenefitsLogo(benefit.benefitTypeName)),
                    contentDescription = stringResource(id = R.string.cd_onboard_screen_bottom_fade),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Column {
            benefit.benefitTypeName?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = VibrantPurple40,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                )
            }
            benefit.benefitName?.let {
                Text(
                    text = it,
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Divider(color = VibrantPurple90, thickness = 1.dp)
    }

}

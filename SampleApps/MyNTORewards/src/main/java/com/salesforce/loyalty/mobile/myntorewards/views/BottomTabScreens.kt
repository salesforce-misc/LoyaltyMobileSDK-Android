package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo_bold
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.PopupState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpoleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_home),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun MyOfferScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpoleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_my_offers),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun MyProfileScreen() {
    var currentMyProfileState by remember { mutableStateOf(MyProfileScreenState.MAIN_VIEW) }

    when(currentMyProfileState)
    {
        MyProfileScreenState.MAIN_VIEW -> MyProfileLandingView {
            currentMyProfileState= it
        }
        MyProfileScreenState.BENEFIT_VIEW -> MyProfileBenefitFullScreenView {
            currentMyProfileState= it
            }
        MyProfileScreenState.TRANSACTION_VIEW -> MyProfileBenefitFullScreenView {
            currentMyProfileState= it
        }
    }
}

@Composable
fun MyProfileBenefitFullScreenView(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit)
{
    Column(verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(start=16.dp, end = 16.dp)
        )
    {

        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.padding(top= 10.dp, bottom = 10.dp).clickable {
                openProfileScreen(MyProfileScreenState.MAIN_VIEW)
            }
        )

        Text(
            text = "My Benefits",
            fontFamily = font_archivo_bold,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.padding(top= 11.5.dp, bottom = 11.5.dp)
        )

        val model: MembershipBenefitViewModel = viewModel()
        val membershipBenefit by model.membershipBenefitLiveData.observeAsState() // collecting livedata as state
        val context: Context = LocalContext.current
        //calling member benefit
        model.memberBenefitAPI(context)
        membershipBenefit?.let { LazyRowItemsDemo2(it) }

    }

}

@Composable
fun LazyRowItemsDemo2(itemViewStates: List<MemberBenefit>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(itemViewStates) {
            ListItemMyBenefit(it)
        }
    }
}

@Composable
fun MyProfileLandingView(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        Spacer(modifier = Modifier.height(50.dp))
        ScreenTabHeader()

        Column(modifier = Modifier.verticalScroll(
            rememberScrollState()
        ),) {
            Spacer(modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(Color.White))
            UserInfoRow()
            Spacer(modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(Color.White))
            ProfileCard()
            Spacer(modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(MyProfileScreenBG))
            TransactionCard()
            Spacer(modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(MyProfileScreenBG))
            MyBenefitView(){
                openProfileScreen(it)
            }
            Spacer(modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(MyProfileScreenBG))

        }
    }
}



@Composable
fun RedeemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpoleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_redeem),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


@Composable
fun MoreScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpoleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_more),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}
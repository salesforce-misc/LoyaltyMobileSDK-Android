package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Assets.LoyaltyAppAsset.getBenefitsLogo
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BenefitViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ProfileViewScreen
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileBenefitFullScreenView(
    navProfileController: NavHostController,
    benefitViewModel: BenefitViewModelInterface
) {

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    val context: Context = LocalContext.current


    fun refresh() = refreshScope.launch {
        benefitViewModel.loadBenefits(context, true)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(Color.White)
                .pullRefresh(state)
        )
        {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "benefit_back_button",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
                    .clickable {
                        navProfileController.popBackStack()
                    }
            )
            Text(
                text = stringResource(R.string.my_benefits),
                fontFamily = font_archivo_bold,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(
                    top = 11.5.dp,
                    bottom = 11.5.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )
            Column(
                modifier = Modifier
                    .background(MyProfileScreenBG)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)

            ) {
                BenefitListView(Modifier.fillMaxHeight(), benefitViewModel)
            }
        }
        PullRefreshIndicator(refreshing, state)
    }

}

@Composable
fun MyBenefitMiniScreenView(
    navProfileController: NavHostController,
    benefitViewModel: BenefitViewModelInterface
) {

    Column(
        modifier = Modifier
            .background(MyProfileScreenBG)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)

    ) {
        ProfileSubViewHeader(stringResource(R.string.my_benefits))
        {
            navProfileController.navigate(ProfileViewScreen.BenefitFullScreen.route)
        }
        BenefitListView(Modifier.height(300.dp), benefitViewModel)
    }
}

@Composable
fun BenefitListView(modifier: Modifier, benefitViewModel: BenefitViewModelInterface) {

    var isInProgress by remember { mutableStateOf(false) }

    val membershipBenefit by benefitViewModel.membershipBenefitLiveData.observeAsState() // collecting livedata as state
    val membershipBenefitFetchStatus by benefitViewModel.benefitViewState.observeAsState() // collecting livedata as state
    val context: Context = LocalContext.current
    //calling member benefit
    LaunchedEffect(key1 = true) {
        benefitViewModel.loadBenefits(context)
        isInProgress = true
    }

    when (membershipBenefitFetchStatus) {
        BenefitViewStates.BenefitFetchSuccess -> {
            isInProgress = false

        }
        BenefitViewStates.BenefitFetchInProgress -> {
            isInProgress = true

        }
        BenefitViewStates.BenefitFetchFailure -> {
            isInProgress = false
        }

        else -> {}
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
            )
        } else {
            if (membershipBenefit?.isEmpty() == true) {
                BenefitsEmptyView()
            }

            membershipBenefit?.let {
                LazyColumn(modifier = modifier.testTag("benefits")) {
                    if (it.size >= AppConstants.MAX_SIZE_BENEFIT_LIST) {
                        items(it.subList(0, AppConstants.MAX_SIZE_BENEFIT_LIST)) {
                            ListItemMyBenefit(it)
                        }
                    } else {
                        items(it) {
                            ListItemMyBenefit(it)
                        }
                    }
                }
            }
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

        Column(
            modifier = Modifier
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

@Composable
fun BenefitsEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = stringResource(id = R.string.label_empty_benefits)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        androidx.compose.material3.Text(
            text = stringResource(id = R.string.label_empty_benefits),
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

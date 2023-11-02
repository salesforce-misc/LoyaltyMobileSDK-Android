package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_PROGRESS_FIRST_STEP
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_PROGRESS_SECOND_STEP
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_PROGRESS_STARTED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_PROGRESS_COMPLETED
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReceiptProgressScreen(
    navHostController: NavHostController,
    closePopup: () -> Unit,
    currentProgress: String
) {

    Column(
        verticalArrangement = Arrangement.Center,

        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
            .background(
                MyProfileScreenBG,
                RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .background(MyProfileScreenBG)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ProgressBarRow(currentProgress)
            Spacer(modifier = Modifier.height(24.dp))
            ProgressText(currentProgress)
        }
        Column(
            modifier = Modifier
                .weight(0.1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.btn_cancel),
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 3.dp)
                    .clickable {
                        closePopup()
                        navHostController.popBackStack(
                            MoreScreens.CaptureImageScreen.route,
                            false
                        )
                    },
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = LighterBlack,
                fontWeight = FontWeight.Normal,

                )
        }
    }
}

@Composable
fun ProgressBarRow(currentProgress: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        var firststepImageID = R.drawable.in_progress
        var secondstepImageID = R.drawable.before_progress
        var thirdStepImageID = R.drawable.before_progress
        var firstProgressBarID = R.drawable.rectangle_before_progress
        var secondProgressBarID = R.drawable.rectangle_before_progress

        when (currentProgress) {
            RECEIPT_PROGRESS_STARTED -> {
            }

            RECEIPT_PROGRESS_FIRST_STEP -> {
                firststepImageID = R.drawable.after_poress
                firstProgressBarID = R.drawable.rectangle_after_progress
                secondstepImageID = R.drawable.in_progress
            }

            RECEIPT_PROGRESS_SECOND_STEP -> {
                firststepImageID = R.drawable.after_poress
                firstProgressBarID = R.drawable.rectangle_after_progress
                secondstepImageID = R.drawable.after_poress
                secondProgressBarID = R.drawable.rectangle_after_progress
                thirdStepImageID = R.drawable.in_progress
            }

            RECEIPT_PROGRESS_COMPLETED -> {
                firststepImageID = R.drawable.after_poress
                firstProgressBarID = R.drawable.rectangle_after_progress
                secondstepImageID = R.drawable.after_poress
                secondProgressBarID = R.drawable.rectangle_after_progress
                thirdStepImageID = R.drawable.after_poress
            }
        }

        Image(
            painter = painterResource(id = firststepImageID),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
        Image(
            painter = painterResource(id = firstProgressBarID),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
        Image(
            painter = painterResource(id = secondstepImageID),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
        Image(
            painter = painterResource(id = secondProgressBarID),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
        Image(
            painter = painterResource(id = thirdStepImageID),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
    }
}


@Composable
fun ProgressText(currentProgress: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    )
    {


        var progressHeadingText = ""
        var progressSubHeadingText = ""
        when (currentProgress) {
            "progress_started" -> {
                progressHeadingText = stringResource(id = R.string.receipt_progress_step_1)
                progressSubHeadingText = stringResource(id = R.string.receipt_progress_subheading)
            }

            "progress_first_step_completed" -> {
                progressHeadingText = stringResource(id = R.string.receipt_progress_step_2)
                progressSubHeadingText = stringResource(id = R.string.receipt_progress_subheading)
            }

            "progress_second_step_completed" -> {
                progressHeadingText = stringResource(id = R.string.receipt_progress_step_3)
                progressSubHeadingText = stringResource(id = R.string.receipt_progress_subheading)
            }

            "progress_third_step_completed" -> {
                progressHeadingText = stringResource(id = R.string.receipt_progress_step_4)
                progressSubHeadingText = ""
            }
        }
        androidx.compose.material.Text(
            text = progressHeadingText,
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
        androidx.compose.material.Text(
            text = progressSubHeadingText,
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Normal,
            color = LighterBlack,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }


}
package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags

@Composable
fun SpinWheelPointer(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.wheel_pointer),
        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
        modifier = Modifier
            .padding(bottom = 60.dp)
            .testTag(TestTags.TEST_TAG_APP_LOGO_HOME_SCREEN)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        contentScale = ContentScale.FillWidth
    )
}
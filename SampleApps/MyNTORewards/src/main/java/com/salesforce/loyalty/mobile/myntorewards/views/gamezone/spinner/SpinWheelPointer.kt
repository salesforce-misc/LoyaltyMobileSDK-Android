package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var wheelTapPointer by remember { mutableIntStateOf(R.drawable.wheel_pointer) }
    var isClickedBefore by remember { mutableStateOf(false) }
    Image(
        painter = painterResource(wheelTapPointer),
        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
        modifier = Modifier
            .padding(bottom = 60.dp)
            .testTag(TestTags.TEST_TAG_APP_LOGO_HOME_SCREEN)
            .clickable(
                enabled= !isClickedBefore,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                wheelTapPointer= R.drawable.wheel_pointer_without_text
                isClickedBefore= true
                onClick()
            },
        contentScale = ContentScale.FillWidth
    )
}
package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.views.components.HeaderTextSmaller

@Composable
fun BadgeFullScreenHeader(clickBack: () -> Unit) {

    Spacer(modifier = Modifier.height(50.dp))
    Image(
        painter = painterResource(id = R.drawable.back_arrow),
        contentDescription = stringResource(R.string.badge_back_button),
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
            .clickable {
                clickBack()
            }
    )
    HeaderTextSmaller(stringResource(R.string.text_my_badges),Modifier.padding(
        top = 11.5.dp,
        bottom = 11.5.dp,
        start = 16.dp,
        end = 16.dp
    ) )

}
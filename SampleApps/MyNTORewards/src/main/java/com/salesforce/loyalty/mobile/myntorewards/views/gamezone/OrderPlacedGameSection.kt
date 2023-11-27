package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextBold
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmall
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent

@Composable
fun OrderConfirmationGameSection(gameType: GameType) {
    val (headerId, placeholderImage) = if (gameType == GameType.SCRATCH_CARD) {
        Pair(R.string.place_order_game_header_scratch_card, R.drawable.placeholder_scratch_card)
    } else {
        Pair(R.string.place_order_game_header_spin_wheel, R.drawable.placeholder_game_thumbnail)
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BodyTextBold(text = stringResource(headerId))
        BodyTextSmall(
            text = stringResource(R.string.place_order_game_subheader),
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        ImageComponent(
            drawableId = placeholderImage,
            contentDescription = stringResource(headerId),
            modifier = Modifier.height(80.dp).width(120.dp)
        )
    }
}
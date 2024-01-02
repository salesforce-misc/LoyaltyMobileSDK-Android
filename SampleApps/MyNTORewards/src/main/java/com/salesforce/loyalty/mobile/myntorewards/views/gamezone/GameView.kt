package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ScratchCardBackground
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_EXPIRY
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_IMAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_TITLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_TYPE

@Composable
fun GameView(isExpired: Boolean, gamePlayingStatus: String?, title: String, gameType: GameType, gameReward:String= "", onClicked: () -> Unit) {

    val GAME_STATUS_PLAYED= "Played"
    Column(
        modifier = Modifier
            .width(165.dp)
            .height(210.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(bottom = 8.dp)
            .clickable {
                onClicked()
            }.testTag(TEST_TAG_GAME_ZONE_ITEM)
    )

    {

            val thumbnailId = when (gameType) {
                GameType.SPIN_A_WHEEL -> {
                    if (isExpired) {
                        R.drawable.placeholder_spin_wheel_expired
                    } else {
                        R.drawable.placeholder_game_thumbnail
                    }
                }
                GameType.SCRATCH_CARD -> {
                    if (isExpired) {
                        R.drawable.placeholder_scratch_card_expired
                    } else {
                        R.drawable.placeholder_scratch_card
                    }
                }
            }
            Image(
                painter = painterResource(id = thumbnailId),
                contentDescription ="game",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(ScratchCardBackground).testTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE),
                contentScale = ContentScale.Crop
            )
        Box(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ){
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth().align(Alignment.TopStart).testTag(TEST_TAG_GAME_ZONE_ITEM_TITLE)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                val gameTypeText = when (gameType) {
                    GameType.SPIN_A_WHEEL -> R.string.game_spin_a_wheel
                    GameType.SCRATCH_CARD -> R.string.game_scratch_card
                }
                Text(
                    text = stringResource(id = gameTypeText),
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth().testTag(TEST_TAG_GAME_ZONE_ITEM_TYPE)
                )
                Spacer(modifier = Modifier.height(4.dp))
                var expirationTextColor = Color.White
                var expirationBgColor = Color.Black
                if (isExpired) {
                    expirationTextColor = Color.Black
                    expirationBgColor = ScratchCardBackground
                }

                gamePlayingStatus ?.let {
                    Text(
                        text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle()
                                ) {
                                    append(it)
                                }
                            },
                        fontWeight = FontWeight.Normal,
                        color = expirationTextColor,
                        fontFamily = font_sf_pro,
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .height(24.dp)
                            .background(expirationBgColor, shape = RoundedCornerShape(4.dp))
                            .padding(start = 12.dp, end = 12.dp, bottom = 4.dp).testTag(TEST_TAG_GAME_ZONE_ITEM_EXPIRY)
                    )
                    if(gamePlayingStatus== GAME_STATUS_PLAYED){
                        Text(
                            text = gameReward,
                            fontFamily = font_sf_pro,
                            color = LightBlack,
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun previewGameView(){
    GameView(false, "Expiring Tomorrow", title = "R.string.game_placeholder_title", gameType = GameType.SPIN_A_WHEEL){}
}
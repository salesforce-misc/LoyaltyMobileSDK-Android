package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextDarkGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.confirmationScreenMsg

@Composable
fun PlayedGamePopup(
    playedGameRewardType: String,
    rewardValue: String,
    crossButtonClicked: () -> Unit,
    textButtonClicked: () -> Unit,
    backClicked: () -> Unit,
) {
    var playedGamePopupupImage:Int= R.drawable.played_game_rewarded
    var playedGamePopupupHeading=""
    var playedGamePopupupMsg=""
    if(playedGameRewardType== RewardType.NO_VOUCHER.rewardType){
        playedGamePopupupImage= R.drawable.played_game_better_luck
        playedGamePopupupHeading= stringResource(id = R.string.better_luck_next_time)
        playedGamePopupupMsg= stringResource(id = R.string.text_better_luck_reward)
    }
    else{
        playedGamePopupupImage= R.drawable.played_game_rewarded
        playedGamePopupupHeading= stringResource(id = R.string.text_congratulations)
        playedGamePopupupMsg= confirmationScreenMsg(playedGameRewardType, rewardValue)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .weight(0.75f)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close_button_without_bg),
                    contentDescription = stringResource(R.string.cd_close_popup),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .clickable {
                            crossButtonClicked()
                        }
                        .align(Alignment.End))
            }

            Image(
                painter = painterResource(id = playedGamePopupupImage),
                contentDescription = stringResource(R.string.error_cd),
                modifier = Modifier
                    .width(220.dp)
                    .height(220.dp),
                contentScale = ContentScale.FillWidth,

                )

            Text(
                text = playedGamePopupupHeading,
                fontFamily = font_sf_pro,
                color = LighterBlack,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = playedGamePopupupMsg,
                fontFamily = font_sf_pro,
                color = LightBlack,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .weight(0.25f)
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    backClicked()
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.back_text),
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                )
            }
            if(playedGameRewardType!= RewardType.NO_VOUCHER.rewardType){
                Text(
                    text = stringResource(id = R.string.text_go_to_voucher_section),
                    fontFamily = font_sf_pro,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 3.dp)
                        .clickable {
                            textButtonClicked()
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = LighterBlack,
                    fontWeight = FontWeight.Normal,

                    )
            }

        }
    }
}
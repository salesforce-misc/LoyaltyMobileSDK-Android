package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.os.SystemClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common

@Composable
fun GameView(thumbnailId: Int, titleId: Int, gameType: GameType, onClicked: () -> Unit) {
    var lastClickTime by remember { mutableStateOf(0L) }
    Column(
        modifier = Modifier
            .width(165.dp)
            .height(203.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(bottom = 8.dp)
            .clickable {
                onClicked()
            }
    )

    {
        Box() {
            Image(
                painter = painterResource(id = thumbnailId),
                contentDescription ="game",
                modifier = Modifier
                    .width(165.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(ScratchCardBackground),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ){
            Text(
                text = stringResource(id = titleId),
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            val gameTypeText = when(gameType){
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
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append(stringResource(id = R.string.game_expiring_tomorrow))
                    }
                },
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                modifier = Modifier
                    .height(24.dp)
                    .background(Color.Black, shape = RoundedCornerShape(4.dp))
                    .padding(start = 12.dp, end = 12.dp, bottom = 4.dp)

            )
        }

    }
}

@Preview
@Composable
fun previewGameView(){
    GameView(thumbnailId = R.drawable.placeholder_game_thumbnail, titleId = R.string.game_placeholder_title, gameType = GameType.SPIN_A_WHEEL){}
}
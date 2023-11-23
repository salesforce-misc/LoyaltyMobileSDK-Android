package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

data class ScratchedPath(
    val path: Path,
    val thickness: Float = 50f
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScratchCardView(navController: NavHostController, gameViewModel: GameViewModelInterface) {
    val overlayImage = ImageBitmap.imageResource(id = R.drawable.overlay_img)
    val currentState = remember { mutableStateOf(ScratchedPath(path = Path())) }
    val movedState = remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPurple)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight()
                .background(LightPurple)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "game_back_button",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .clickable {
                            // Add action for back button press.
                            navController.popBackStack()
                        }
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ){
                Text(
                    text = stringResource(id = R.string.game_scratch_card_title),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro
                )
                Text(
                    text = stringResource(id = R.string.game_scratch_card_sub_title),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro
                )

            }
        }
        // Scratch Card Implementation
        CanvasForScratching(
            overlay = overlayImage,
            modifier = Modifier.align(Alignment.Center),
            cursorMovedOffset = movedState.value,
            onCursorMovedOffset = { x, y ->
                movedState.value = Offset(x, y)
            },
            path = currentState.value.path,
            scratchThickness = currentState.value.thickness,
            gameViewModel = gameViewModel,
            navController = navController
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(id = R.string.game_scratch_card_instruction),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro
            )
            Text(
                text = stringResource(id = R.string.game_scratch_card_detail_instruction),
                color = LightBlack,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                modifier = Modifier.padding(top=16.dp)
            )
        }
    }
}
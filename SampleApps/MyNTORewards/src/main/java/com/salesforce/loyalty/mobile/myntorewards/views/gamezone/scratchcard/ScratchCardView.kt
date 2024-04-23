package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.scratchcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SCRATCH_CARD_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import kotlinx.coroutines.launch

data class ScratchedPath(
    val path: Path,
    val thickness: Float = 50f
)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScratchCardView(navController: NavHostController, gameViewModel: GameViewModel, gameParticipantRewardId: String) {
    val overlayImage = ImageBitmap.imageResource(id = R.drawable.overlay_img)
    val currentState = remember { mutableStateOf(ScratchedPath(path = Path())) }
    val movedState = remember { mutableStateOf<Offset?>(null) }

    var openBottomsheet by remember { mutableStateOf(false) }
    var gameName by remember { mutableStateOf("") }
    var gameDescription by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        gameViewModel.getGameDefinitionFromGameParticipantRewardId(gameParticipantRewardId).let {
            gameName= it?.name ?: ""
            gameDescription= it?.description ?: ""
        }
    }
    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    val coroutineScope = rememberCoroutineScope()
    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }

    val closeBottomSheet = {
        //showBottomBar(true)
        //blurBG = AppConstants.NO_BLUR_BG // need confirmation if background blur is needed
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

     androidx.compose.material.BottomSheetScaffold(
          scaffoldState = bottomSheetScaffoldState,

          sheetContent = {
              Spacer(modifier = Modifier.height(1.dp))
              ErrorPopup(stringResource(id = R.string.game_error_msg), textButtonClicked = {},
                  tryAgainClicked = { openBottomsheet = false
                  closeBottomSheet()
                  navController.popBackStack() })
          },
          sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE, AppConstants.POPUP_ROUNDED_CORNER_SIZE, 0.dp, 0.dp),
          sheetPeekHeight = 0.dp,
          sheetGesturesEnabled = false
      ){
         Box(
             modifier = Modifier
                 .fillMaxSize()
                 .background(LightPurple)
                 .testTag(TEST_TAG_SCRATCH_CARD_SCREEN)
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

                 if(gameName.isEmpty())
                     gameName=stringResource(id = R.string.game_scratch_card_title)
                 if(gameDescription.isEmpty() )
                     gameDescription= stringResource(id = R.string.game_scratch_card_sub_title)

                 Column(
                     verticalArrangement = Arrangement.spacedBy(4.dp),
                     horizontalAlignment = Alignment.CenterHorizontally,
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                 ){
                     Text(
                         text =gameName ,
                         color = Color.Black,
                         textAlign = TextAlign.Center,
                         fontSize = 24.sp,
                         fontWeight = FontWeight.Bold,
                         fontFamily = font_sf_pro
                     )
                     Text(
                         text = gameDescription,
                         color = Color.Black,
                         textAlign = TextAlign.Center,
                         fontSize = 18.sp,
                         fontWeight = FontWeight.Bold,
                         fontFamily = font_sf_pro
                     )

                 }
             }
             // Scratch Card Implementation
             gameParticipantRewardId?.let {
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
                     navController = navController,
                     gameParticipantRewardId = it
                 ){
                     openBottomsheet = true
                 }
             }
             Column(
                 verticalArrangement = Arrangement.spacedBy(16.dp),
                 modifier = Modifier
                     .align(Alignment.BottomCenter)
                     .fillMaxWidth()
                     .padding(start = 60.dp, end = 60.dp, bottom = 100.dp),
                 horizontalAlignment = Alignment.CenterHorizontally
             ){
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


}
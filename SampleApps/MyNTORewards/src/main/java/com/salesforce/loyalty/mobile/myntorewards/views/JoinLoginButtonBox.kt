package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.BottomSheetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Combine UI of Onboarding screen having buttons to open join Popup and Login Popup
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JoinLoginButtonBox(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    setBottomSheetState: (bottomSheetState: BottomSheetType) -> Unit
) {

    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    JoinButton { setBottomSheetState(BottomSheetType.POPUP_JOIN)
        openBottomSheet()
    }

    Spacer(modifier = Modifier.height(24.dp))

    AlreadyAMemberButton {
        setBottomSheetState(BottomSheetType.POPUP_LOGIN)
        openBottomSheet()
    }
}
@Composable
fun JoinButton(openJoinPopup: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            openJoinPopup()
        },
        colors = ButtonDefaults.buttonColors(LightPurple),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.join_text),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}
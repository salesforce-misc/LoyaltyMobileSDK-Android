package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColorLight
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.utilities.copyToClipboard
import com.salesforce.loyalty.mobile.myntorewards.utilities.isValidEmail
import com.salesforce.loyalty.mobile.myntorewards.utilities.showToast
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReferFriendViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferFriendViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyText
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextBold
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmall
import com.salesforce.loyalty.mobile.myntorewards.views.components.CommonText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.RoundedIconButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextFieldCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.dashedBorder
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.*

@Composable
fun ReferFriendScreen(viewModel: ReferFriendViewModel = viewModel(), closeAction: () -> Unit) {
    val viewState by viewModel.uiState.observeAsState(null)
    val context = LocalContext.current

    viewState?.let {
        when (it) {
            is ReferFriendViewState.ReferFriendProgramState ->
                ReferFriendScreenUI(viewModel, it.referralProgramType) {
                    closeAction()
                }

            ReferFriendViewState.ShowSignupInvalidEmailMessage -> context.showToast("Please enter valid E-mails!")
        }
    }
}

@Composable
fun ReferFriendScreenUI(viewModel: ReferFriendViewModel, referralProgramType: ReferralProgramType = SIGNUP, closeAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.88F)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ImageComponent(
                drawableId = R.drawable.bg_refer_friend_banner,
                contentDescription = stringResource(R.string.refer_friend_banner_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp)
            )

            RoundedIconButton(
                onClick = { closeAction() },
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
        ) {
            BodyTextBold(text = stringResource(R.string.refer_a_friend_and_earn_header))
            BodyText(text = stringResource(R.string.refer_a_friend_and_earn_sub_header))
            when(referralProgramType) {
                SIGNUP -> SignupToReferUi(viewModel)
                JOIN_PROGRAM -> JoinReferralProgramUi(viewModel)
                START_REFERRING -> StartReferUi(viewModel) { closeAction() }
            }
        }
    }
}

@Composable
fun SignupToReferUi(viewModel: ReferFriendViewModel) {
    var textField by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    TextFieldCustom(
        textField,
        stringResource(R.string.email_address_placeholder),
        modifier = Modifier.padding(vertical = 16.dp),
        rightIconId = null,
        updateTextField = { textField = it }
    )
    BodyText(text = stringResource(R.string.refer_a_friend_and_earn_bottom_text))
    Spacer(modifier = Modifier.height(24.dp))
    PrimaryButton(textContent = stringResource(id = R.string.referral_signup_button_text), onClick = {
        if (!isValidEmail(textField.text.trim())) {
            context.showToast("Please enter valid E-mails!")
            return@PrimaryButton
        }
        focusManager.clearFocus()
        viewModel.onSignUpToReferClicked(textField.text)
    })
}

@Composable
fun JoinReferralProgramUi(viewModel: ReferFriendViewModel) {
    BodyText(text = stringResource(R.string.refer_a_friend_and_earn_bottom_text))
    Spacer(modifier = Modifier.height(24.dp))
    PrimaryButton(textContent = stringResource(id = R.string.referral_join_button_text), onClick = {
        viewModel.onReferralProgramJoinClicked()
    })
}

@Composable
private fun StartReferUi(viewModel: ReferFriendViewModel, doneAction: () -> Unit) {
    val focusManager = LocalFocusManager.current
    var textField by remember { mutableStateOf(TextFieldValue("")) }
    TextFieldCustom(
        textField,
        stringResource(R.string.friends_email_address_placeholder),
        modifier = Modifier.padding(top = 16.dp),
        rightIconId = R.drawable.ic_arrow_forward,
        singleLine = true,
        rightIconClick = {
            focusManager.clearFocus()
            viewModel.sendReferralMail()
        },
        updateTextField = { textField = it }
    )
    BodyTextSmall(
        text = stringResource(R.string.separate_emails_with_commas),
        color = TextGray,
        modifier = Modifier.padding(8.dp)
    )
    CommonText(
        text = stringResource(R.string.share_via),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold
    )
    SocialMediaRow()
    ReferralCodeView("845FFF907ZX6") // TODO: REPLACE THIS WITH ACTUAL VALUES
    BodyTextSmall(
        text = stringResource(R.string.share_referral_code_label),
        color = TextGray,
        modifier = Modifier.padding(8.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
    PrimaryButton(textContent = stringResource(id = R.string.scanning_done), onClick = { doneAction() })
}

@Composable
fun SocialMediaRow() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ImageComponent(drawableId = R.drawable.ic_facebook, contentDescription = stringResource(R.string.share_via_facebook_icon_description), modifier = Modifier.weight(1f))
        ImageComponent(drawableId = R.drawable.ic_instagram, contentDescription = stringResource(R.string.share_via_instagram_icon_description), modifier = Modifier.weight(1f))
        ImageComponent(drawableId = R.drawable.ic_whatsapp, contentDescription = stringResource(R.string.share_via_whatsapp_icon_description), modifier = Modifier.weight(1f))
        ImageComponent(drawableId = R.drawable.ic_twitter, contentDescription = stringResource(R.string.share_via_twitter_icon_description), modifier = Modifier.weight(1f))
        ImageComponent(drawableId = R.drawable.ic_share, contentDescription = stringResource(R.string.share_via_icon_description), modifier = Modifier.weight(1f))
    }
}

@Composable
fun ReferralCodeView(referralCode: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .dashedBorder(1.dp, SaffronColor, SaffronColorLight, 8.dp, 10.dp)
    ) {
        val context = LocalContext.current
        BodyTextBold(
            text = referralCode,
            modifier = Modifier.wrapContentSize().padding(12.dp)
        )
        CommonText(
            text = stringResource(R.string.tap_to_copy), textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable {
                    context.copyToClipboard(text = referralCode)
                }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ReferFriendScreenPreview() {
    ReferFriendScreen {  }
}
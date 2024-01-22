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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.testTag
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
import com.salesforce.loyalty.mobile.myntorewards.utilities.ShareType
import com.salesforce.loyalty.mobile.myntorewards.utilities.copyToClipboard
import com.salesforce.loyalty.mobile.myntorewards.utilities.isValidEmail
import com.salesforce.loyalty.mobile.myntorewards.utilities.shareReferralCode
import com.salesforce.loyalty.mobile.myntorewards.utilities.showToast
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferFriendViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyText
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextBold
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmall
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CommonText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.ProgressDialogComposable
import com.salesforce.loyalty.mobile.myntorewards.views.components.RoundedIconButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextButtonCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextFieldCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.dashedBorder
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.*
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup

const val TEST_TAG_REFER_FRIEND_SCREEN = "TEST_TAG_REFER_FRIEND_SCREEN"

@Composable
fun ReferFriendScreen(viewModel: MyReferralsViewModel, backAction: () -> Boolean, closeAction: () -> Unit) {
    val programState by viewModel.programState.observeAsState()
    val viewState by viewModel.viewState.observeAsState(null)
    val context = LocalContext.current

    programState?.let {
        when(it) {
            is ERROR -> ErrorPopup(
                it.errorMessage ?: stringResource(id = R.string.receipt_scanning_error_desc),
                tryAgainClicked = { viewModel.enrollToReferralPromotion(context, true) },
                textButtonClicked = {  }
            )
            is EMPTY_STATE -> {
                CircularProgress(modifier = Modifier
                    .fillMaxHeight(0.92f)
                    .fillMaxWidth())
            }
            else -> ReferFriendScreenUI(viewModel, it, backAction) {
                closeAction()
                viewModel.resetViewState()
            }
        }
    }

    viewState?.let {
        when (it) {
            ReferFriendViewState.ShowSignupInvalidEmailMessage -> context.showToast("Please enter valid E-mails!")
            ReferFriendViewState.ReferFriendInProgress -> {
                ProgressDialogComposable() { } // Do nothing on progress dismiss
            }
            ReferFriendViewState.ReferFriendSendMailsSuccess -> { context.showToast(stringResource(R.string.emails_sent_successfully)) }
            ReferFriendViewState.ReferFriendSendMailsFailed -> { context.showToast(stringResource(R.string.failed_try_again)) }
            is ReferFriendViewState.EnrollmentFailed -> {
                // Do nothing
            }

            ReferFriendViewState.ReferFriendTryAgainInProgress -> CircularProgress()
        }
    }
}

@Composable
fun ReferFriendScreenUI(viewModel: MyReferralsViewModel, referralProgramType: ReferralProgramType, backAction: () -> Boolean, closeAction: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Color.White)
            .testTag(TEST_TAG_REFER_FRIEND_SCREEN)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ImageComponent(
                drawableId = R.drawable.bg_refer_friend_banner,
                contentDescription = stringResource(R.string.refer_friend_banner_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp)
            )

            if (referralProgramType == START_REFERRING) {
                RoundedIconButton(
                    onClick = { closeAction() },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
        ) {
            when(referralProgramType) {
                SIGNUP -> SignupToReferUi(viewModel)
                JOIN_PROGRAM -> JoinReferralProgramUi(viewModel, backAction)
                START_REFERRING -> StartReferUi(viewModel) { closeAction() }
                else -> {}
            }
        }
    }
}

@Composable
fun SignupToReferUi(viewModel: MyReferralsViewModel) {
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
fun JoinReferralProgramUi(viewModel: MyReferralsViewModel, backAction: () -> Boolean) {
    val context = LocalContext.current
    BodyTextBold(text = stringResource(R.string.join_referral_program_header))
    BodyText(text = stringResource(R.string.join_referral_program_description))
    Spacer(modifier = Modifier.height(24.dp))
    PrimaryButton(textContent = stringResource(id = R.string.referral_join_button_text), onClick = {
        viewModel.enrollToReferralPromotion(context, false)
//        PrefHelper.customPrefs(context)[AppConstants.REFERRAL_PROGRAM_JOINED] = true
    })
    TextButtonCustom(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        textContent = stringResource(id = R.string.back_text),
        onClick = { backAction() }
    )
}

@Composable
private fun StartReferUi(viewModel: MyReferralsViewModel, doneAction: () -> Unit) {
    val context = LocalContext.current
    val referralCode = viewModel.referralCode(context)
    val extraText = context.getString(R.string.share_referral_message, referralCode)
    val focusManager = LocalFocusManager.current
    var textField by remember { mutableStateOf(TextFieldValue("")) }

    BodyTextBold(text = stringResource(R.string.refer_a_friend_and_earn_header))
    BodyText(text = stringResource(R.string.refer_a_friend_and_earn_sub_header))
    TextFieldCustom(
        textField,
        stringResource(R.string.friends_email_address_placeholder),
        modifier = Modifier.padding(top = 16.dp),
        rightIconId = R.drawable.ic_arrow_forward,
        singleLine = true,
        rightIconClick = {
            val emails = textField.text.split(",")
            if (emails.isEmpty() || emails.any { !isValidEmail(it.trim()) }) {
                context.showToast("Please enter valid E-mails!")
                return@TextFieldCustom
            }
            textField = TextFieldValue("")
            focusManager.clearFocus()
            viewModel.sendReferralMail(context, emails)
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
    SocialMediaRow(extraText)
    ReferralCodeView(referralCode)
    Spacer(modifier = Modifier.height(24.dp))
    PrimaryButton(textContent = stringResource(id = R.string.scanning_done), onClick = { doneAction() })
}

@Composable
fun SocialMediaRow(referralContent: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val context = LocalContext.current
        ImageComponent(drawableId = R.drawable.ic_facebook, contentDescription = stringResource(R.string.share_via_facebook_icon_description), modifier = Modifier
            .weight(1f)
            .clickable {
                context.shareReferralCode(referralContent, ShareType.FACEBOOK)
            })
        ImageComponent(drawableId = R.drawable.ic_instagram, contentDescription = stringResource(R.string.share_via_instagram_icon_description), modifier = Modifier
            .weight(1f)
            .clickable {
                context.shareReferralCode(referralContent, ShareType.INSTAGRAM)
            })
        ImageComponent(drawableId = R.drawable.ic_whatsapp, contentDescription = stringResource(R.string.share_via_whatsapp_icon_description), modifier = Modifier
            .weight(1f)
            .clickable {
                context.shareReferralCode(referralContent, ShareType.WHATSAPP)
            })
        ImageComponent(drawableId = R.drawable.ic_twitter, contentDescription = stringResource(R.string.share_via_twitter_icon_description), modifier = Modifier
            .weight(1f)
            .clickable {
                context.shareReferralCode(referralContent, ShareType.TWITTER)
            })
        ImageComponent(drawableId = R.drawable.ic_share, contentDescription = stringResource(R.string.share_via_icon_description), modifier = Modifier
            .weight(1f)
            .clickable {
                context.shareReferralCode(referralContent, ShareType.SHARE_OTHERS)
            })
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
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp)
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
    ReferFriendScreen(viewModel(), backAction = { true }) {  }
}
package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_TANDC_LINK
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerBackground
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple65
import com.salesforce.loyalty.mobile.myntorewards.utilities.ShareType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.REFER_FRIEND_PROMOTION_DESC
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.REFER_FRIEND_PROMOTION_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_REFER_POPUP
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
import com.salesforce.loyalty.mobile.myntorewards.views.components.HtmlText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.ProgressDialogComposable
import com.salesforce.loyalty.mobile.myntorewards.views.components.RoundedIconButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextButtonCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextFieldCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.components.dashedBorder
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.EMPTY_STATE
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.ERROR
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.JOIN_PROGRAM
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.SIGNUP
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.START_REFERRING
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

const val TEST_TAG_REFER_FRIEND_SCREEN = "TEST_TAG_REFER_FRIEND_SCREEN"

/**
 * Bottom sheet screen - Shows the UI states based on user enrolment status
 * If user not enrolled to the given promotion, shows enrolment UI
 * If user already enrolled tot he promotion, shows UI to refer friends
 */
@Composable
fun ReferFriendScreen(viewModel: MyReferralsViewModel, promotionDetails: ReferralPromotionStatusAndPromoCode? = null, backAction: () -> Any, closeAction: () -> Unit) {
    val programState by viewModel.programState.observeAsState()
    val viewState by viewModel.viewState.observeAsState(null)
    val context = LocalContext.current

    programState?.let {
        when(it) {
            is ERROR -> ErrorPopup(
                it.errorMessage ?: stringResource(id = R.string.game_error_msg),
                tryAgainClicked = {
                    closeAction()
                    viewModel.resetProgramTypeOnError()
                },
                tryAgainButtonText = stringResource(id = R.string.referral_back_button_text),
                textButtonClicked = { }
            )
            is EMPTY_STATE -> {
                CircularProgress(modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth())
            }
            else -> ReferFriendScreenUI(viewModel, it, promotionDetails, backAction) {
                closeAction()
                viewModel.resetViewState()
            }
        }
    }

    viewState?.let {
        when (it) {
            ReferFriendViewState.ShowSignupInvalidEmailMessage -> context.showToast(stringResource(R.string.invalid_email_error_message))
            ReferFriendViewState.ReferFriendInProgress -> {
                ProgressDialogComposable() { } // Do nothing on progress dismiss
            }
            ReferFriendViewState.ReferFriendSendMailsSuccess -> { context.showToast(stringResource(R.string.emails_sent_successfully)) }
            is ReferFriendViewState.ReferFriendSendMailsFailed -> {
                // Do nothing
            }
            is ReferFriendViewState.EnrollmentTaskFinished -> {
                // Do nothing
            }

            ReferFriendViewState.ReferFriendTryAgainInProgress -> CircularProgress()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReferFriendScreenUI(viewModel: MyReferralsViewModel, referralProgramType: ReferralProgramType, promotionDetails: ReferralPromotionStatusAndPromoCode? = null, backAction: () -> Any, closeAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth()
            .background(Color.White)
            .testTag(TEST_TAG_REFER_FRIEND_SCREEN)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val isStartReferring = referralProgramType == START_REFERRING
            val imageSize = if (isStartReferring) {
                170.dp
            } else {
                250.dp
            }
            ImageComponent(
                drawableId = R.drawable.promotion_card_placeholder,
                contentDescription = stringResource(R.string.refer_friend_banner_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(imageSize)
                    .clip(bottomSheetShape),
                contentScale = ContentScale.Crop
            )

            promotionDetails?.imageUrl?.let {
                GlideImage(
                    model = it,
                    contentDescription = stringResource(id = R.string.refer_friend_banner_content_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(imageSize)
                        .clip(bottomSheetShape),
                    contentScale = ContentScale.Crop
                ){
                    it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                }
            } ?: ImageComponent(
                drawableId = R.drawable.bg_refer_friend_banner,
                contentDescription = stringResource(R.string.refer_friend_banner_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(imageSize)
                    .clip(bottomSheetShape),
                contentScale = ContentScale.Crop
            )

            if (isStartReferring) {
                RoundedIconButton(
                    onClick = { closeAction() },
                    modifier = Modifier.align(Alignment.TopEnd).testTag(TEST_TAG_CLOSE_REFER_POPUP)
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 36.dp, start = 24.dp, end = 24.dp)
        ) {
            when(referralProgramType) {
                SIGNUP -> SignupToReferUi(viewModel)
                JOIN_PROGRAM -> this@Column.JoinReferralProgramUi(viewModel, promotionDetails, closeAction)
                START_REFERRING -> StartReferUi(viewModel, promotionDetails) { closeAction() }
                else -> {}
            }
        }
    }
}

//NOTE: This UI state is never called as user is already logged in to this sample app to access Referral feature
@Composable
fun SignupToReferUi(viewModel: MyReferralsViewModel) {
    var textField by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val invalidEmailMessage = stringResource(R.string.invalid_email_error_message)
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
            context.showToast(invalidEmailMessage)
            return@PrimaryButton
        }
        focusManager.clearFocus()
        viewModel.onSignUpToReferClicked(textField.text)
    })
}

@Composable
fun ColumnScope.JoinReferralProgramUi(
    viewModel: MyReferralsViewModel,
    promotionDetails: ReferralPromotionStatusAndPromoCode? = null,
    backAction: () -> Any
) {
    val context = LocalContext.current
    BodyTextBold(text = promotionDetails?.name ?: stringResource(R.string.join_referral_program_header), modifier = Modifier.testTag(REFER_FRIEND_PROMOTION_NAME))
    promotionDetails?.description?.let {
        BodyText(text = it, modifier = Modifier.testTag(REFER_FRIEND_PROMOTION_DESC))
    }
    HtmlText(text = stringResource(R.string.refer_a_friend_and_earn_bottom_text, REFERRAL_TANDC_LINK), size = 16f)
    Spacer(modifier = Modifier.weight(1f))
    PrimaryButton(textContent = stringResource(id = R.string.referral_join_button_text), onClick = {
        viewModel.enrollToReferralPromotion(context, false)
    })
    TextButtonCustom(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        textContent = stringResource(id = R.string.referral_back_button_text),
        onClick = { backAction() }
    )
}

@Composable
private fun ColumnScope.StartReferUi(viewModel: MyReferralsViewModel, promotionDetails: ReferralPromotionStatusAndPromoCode? = null, doneAction: () -> Unit) {
    val context = LocalContext.current
    val referralLink = viewModel.referralLink(promotionDetails?.promotionPageUrl.orEmpty())
    val referralCode = viewModel.referralCode(context).orEmpty()
    val extraText = context.getString(R.string.share_referral_message, referralLink.plus(referralCode))
    val focusManager = LocalFocusManager.current
    var textField by remember { mutableStateOf(TextFieldValue("")) }
    val invalidEmailMessage = stringResource(R.string.invalid_email_error_message)

    BodyTextBold(text = promotionDetails?.name ?: stringResource(R.string.refer_a_friend_and_earn_header), modifier = Modifier.testTag(
        REFER_FRIEND_PROMOTION_NAME))
    promotionDetails?.description?.let {
        BodyText(text = it, modifier = Modifier.testTag(REFER_FRIEND_PROMOTION_DESC))
    }
    TextFieldCustom(
        textField,
        stringResource(R.string.friends_email_address_placeholder),
        modifier = Modifier.padding(top = 16.dp),
        rightIconId = R.drawable.ic_arrow_forward,
        singleLine = true,
        rightIconClick = {
            val emails = textField.text.split(",")
            if (emails.isEmpty() || emails.any { !isValidEmail(it.trim()) }) {
                context.showToast(invalidEmailMessage)
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
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        text = stringResource(R.string.share_via),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold
    )
    SocialMediaRow(extraText)
    Spacer(modifier = Modifier.height(8.dp))
    ReferralCodeView(referralLink, referralCode)
    Spacer(modifier = Modifier.weight(1f))
    PrimaryButton(textContent = stringResource(id = R.string.scanning_done), onClick = { doneAction() })
}

@Composable
fun SocialMediaRow(referralContent: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        ShareType.values().forEach {
            ImageComponent(
                drawableId = it.iconId,
                contentDescription = it.name,
                modifier = Modifier.clickable {
                    context.shareReferralCode(referralContent, it)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ReferralCodeView(referralLink: String, referralCode: String) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (referralLinkView, copyView) = createRefs()
        val context = LocalContext.current

        ImageComponent(
            drawableId = R.drawable.ic_copy, stringResource(id = R.string.tap_to_copy),
            modifier = Modifier
                .constrainAs(copyView) {
                    end.linkTo(parent.end, margin = 2.dp)
                }
                .clickable {
                    context.copyToClipboard(text = referralLink.plus(referralCode))
                },
            contentScale = ContentScale.Inside
        )

        Column(modifier = Modifier
            .constrainAs(referralLinkView) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                end.linkTo(copyView.start, margin = 8.dp)
                bottom.linkTo(copyView.bottom)
                top.linkTo(copyView.top)
            }
            .dashedBorder(0.dp, VeryLightPurple, VeryLightPurple, 8.dp, 10.dp)
        ) {
            CommonText(
                text = referralLink, color = VibrantPurple65,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 8.dp, start = 12.dp)
            )
            CommonText(
                text = referralCode,
                color = SpinnerBackground,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Top,
                        trim = LineHeightStyle.Trim.None
                    ))
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ReferFriendScreenPreview() {
    ReferFriendScreen(viewModel(), backAction = { true }) {  }
}
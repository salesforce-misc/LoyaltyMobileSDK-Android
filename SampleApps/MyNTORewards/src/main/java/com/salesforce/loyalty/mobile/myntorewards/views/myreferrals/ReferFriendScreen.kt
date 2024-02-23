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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.CopyColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColorLight
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextLightGray
import com.salesforce.loyalty.mobile.myntorewards.utilities.ShareType
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

@Composable
fun ReferFriendScreen(viewModel: MyReferralsViewModel, promotionDetails: Results? = null, backAction: () -> Any, closeAction: () -> Unit) {
    val programState by viewModel.programState.observeAsState()
    val viewState by viewModel.viewState.observeAsState(null)
    val context = LocalContext.current

    programState?.let {
        when(it) {
            is ERROR -> ErrorPopup(
                it.errorMessage ?: stringResource(id = R.string.receipt_scanning_error_desc),
                tryAgainButtonText = stringResource(id = R.string.join_referral_program_button_text),
                tryAgainClicked = { viewModel.enrollToReferralPromotion(context, true) },
                textButtonClicked = {  }
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
                context.showToast(it.errorMessage ?: stringResource(R.string.failed_try_again))
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
fun ReferFriendScreenUI(viewModel: MyReferralsViewModel, referralProgramType: ReferralProgramType, promotionDetails: Results? = null, backAction: () -> Any, closeAction: () -> Unit) {
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

            promotionDetails?.promotionImageUrl?.let {
                GlideImage(
                    model = it,
                    contentDescription = stringResource(id = R.string.content_description_promotion_image),
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
                .padding(top = 16.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
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
    promotionDetails: Results? = null,
    backAction: () -> Any
) {
    val context = LocalContext.current
    BodyTextBold(text = promotionDetails?.promotionName ?: stringResource(R.string.join_referral_program_header))
    promotionDetails?.description?.let {
        BodyText(text = it)
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
private fun StartReferUi(viewModel: MyReferralsViewModel, promotionDetails: Results? = null, doneAction: () -> Unit) {
    val context = LocalContext.current
    val referralLink = viewModel.referralLink(context, promotionDetails?.promotionId.orEmpty())
    val extraText = context.getString(R.string.share_referral_message, referralLink)
    val focusManager = LocalFocusManager.current
    var textField by remember { mutableStateOf(TextFieldValue("")) }
    val invalidEmailMessage = stringResource(R.string.invalid_email_error_message)

    BodyTextBold(text = promotionDetails?.promotionName ?: stringResource(R.string.refer_a_friend_and_earn_header))
    promotionDetails?.description?.let {
        BodyText(text = it)
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
        text = stringResource(R.string.share_via),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold
    )
    SocialMediaRow(extraText)
    Spacer(modifier = Modifier.height(16.dp))
    ReferralCodeView(referralLink)
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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(1.dp, SaffronColor, SaffronColorLight, 8.dp, 10.dp)
    ) {
        val (referralLinkView, dividerView, copyView) = createRefs()
        val context = LocalContext.current

        BodyTextSmall(
            text = stringResource(R.string.tap_to_copy), color = CopyColor,
            modifier = Modifier
                .constrainAs(copyView) {
                    end.linkTo(parent.end)
                }
                .padding(12.dp)
                .clickable {
                    context.copyToClipboard(text = referralCode)
                }
        )
        ImageComponent(
            drawableId = R.drawable.divider_line_vertical, "",
            modifier = Modifier
                .constrainAs(dividerView) {
                    height = Dimension.fillToConstraints
                    top.linkTo(parent.top, margin = 2.dp)
                    bottom.linkTo(parent.bottom, margin = 2.dp)
                    start.linkTo(copyView.start)
                }
                .width(1.dp),
            contentScale = ContentScale.FillHeight
        )

        CommonText(
            text = referralCode,
            color = TextLightGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(referralLinkView) {
                    width = Dimension.fillToConstraints

                    start.linkTo(parent.start)
                    end.linkTo(dividerView.start)
                }
                .padding(12.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ReferFriendScreenPreview() {
    ReferFriendScreen(viewModel(), backAction = { true }) {  }
}
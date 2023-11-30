package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import android.widget.Toast
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
import com.salesforce.loyalty.mobile.myntorewards.utilities.sendMail
import com.salesforce.loyalty.mobile.myntorewards.utilities.shareReferralCode
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyText
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextBold
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmall
import com.salesforce.loyalty.mobile.myntorewards.views.components.CommonText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.RoundedIconButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextFieldCustom
import com.salesforce.loyalty.mobile.myntorewards.views.components.dashedBorder

const val TEST_TAG_REFER_FRIEND_SCREEN = "TEST_TAG_REFER_FRIEND_SCREEN"

@Composable
fun ReferFriendScreen(closeAction: () -> Unit) {
    val referralCode = "845FFF907ZX6"
    val context = LocalContext.current
    val extraText = context.getString(R.string.share_referral_message, referralCode)
    Column(
        modifier = Modifier
            .fillMaxHeight(0.88F)
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

            var textField by remember { mutableStateOf(TextFieldValue("")) }
            TextFieldCustom(
                textField,
                stringResource(R.string.friends_email_address_placeholder),
                modifier = Modifier.padding(top = 16.dp),
                singleLine = true,
                rightIconClick = {
                    context.sendMail(textField.text.split(","), "Subject", extraText)
                },
                updateTextField = { textField = it }
            )

            BodyTextSmall(text = stringResource(R.string.separate_emails_with_commas), color = TextGray, modifier = Modifier.padding(8.dp))
            CommonText(text = stringResource(R.string.share_via), fontSize = 16.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
            SocialMediaRow(extraText)
            ReferralCodeView(referralCode)
            BodyTextSmall(text = stringResource(R.string.share_referral_code_label), color = TextGray, modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(textContent = stringResource(id = R.string.scanning_done), onClick = { closeAction() })
        }
    }
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
        ImageComponent(drawableId = R.drawable.ic_facebook, contentDescription = stringResource(R.string.share_via_facebook_icon_description), modifier = Modifier.weight(1f).clickable {
            context.shareReferralCode(referralContent, ShareType.FACEBOOK)
        })
        ImageComponent(drawableId = R.drawable.ic_instagram, contentDescription = stringResource(R.string.share_via_instagram_icon_description), modifier = Modifier.weight(1f).clickable {
            context.shareReferralCode(referralContent, ShareType.INSTAGRAM)
        })
        ImageComponent(drawableId = R.drawable.ic_whatsapp, contentDescription = stringResource(R.string.share_via_whatsapp_icon_description), modifier = Modifier.weight(1f).clickable {
            context.shareReferralCode(referralContent, ShareType.WHATSAPP)
        })
        ImageComponent(drawableId = R.drawable.ic_twitter, contentDescription = stringResource(R.string.share_via_twitter_icon_description), modifier = Modifier.weight(1f).clickable {
            context.shareReferralCode(referralContent, ShareType.TWITTER)
        })
        ImageComponent(drawableId = R.drawable.ic_share, contentDescription = stringResource(R.string.share_via_icon_description), modifier = Modifier.weight(1f).clickable {
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
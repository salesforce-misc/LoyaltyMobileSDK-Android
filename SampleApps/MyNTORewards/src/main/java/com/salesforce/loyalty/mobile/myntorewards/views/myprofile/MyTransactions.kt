package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Assets
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatTransactionDateTime
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.TransactionsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.TransactionViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PointsChange

@Composable
fun TransactionCard(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit) {
    Column(
        modifier = Modifier
            .background(MyProfileScreenBG)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        ProfileSubViewHeader(stringResource(id = R.string.my_transactions))
        {
            openProfileScreen(MyProfileScreenState.TRANSACTION_VIEW)
        }

        TransactionListView(modifier = Modifier.height(200.dp))

    }
}

@Composable
fun TransactionListView(modifier: Modifier) {
    val model: TransactionsViewModel = viewModel()  //fetching reference of viewmodel
    val transactions by model.transactionsLiveData.observeAsState() // collecting livedata as state
    val context: Context = LocalContext.current

    model.loadTransactions(context)
    val count = transactions?.transactionJournalCount ?: 0
    val pageCount = if (count > 0 && count > AppConstants.MAX_TRANSACTION_COUNT) {
        AppConstants.MAX_TRANSACTION_COUNT
    } else {
        count
    }
    var index = 0
    Column(modifier = Modifier.wrapContentHeight()) {
        Spacer(modifier = Modifier.height(12.dp))
        while (index < pageCount) {
            transactions?.transactionJournals?.get(index)?.apply {
                val transactionName = this.journalTypeName
                val points = getCurrencyPoints(this.pointsChange)
                val date = this.activityDate?.let { activityDate ->
                    formatTransactionDateTime(activityDate)
                }
                if (transactionName != null && points != null && date != null) {
                    ListItemTransaction(transactionName, points, date)
                }
            }
            index++
        }
    }
}

@Composable
fun TransactionFullScreenListView() {
    val model: TransactionsViewModel = viewModel()  //fetching reference of viewmodel
    val transactions by model.transactionsLiveData.observeAsState() // collecting livedata as state
    val transactionViewState by model.transactionViewState.observeAsState()
    val context: Context = LocalContext.current

    LaunchedEffect(true) {
        model.loadTransactions(context)
    }
    var isInProgress by remember { mutableStateOf(false) }

    when (transactionViewState) {
        is TransactionViewState.TransactionFetchSuccess -> {
            isInProgress = false
       }
        is TransactionViewState.TransactionFetchFailure -> {
            isInProgress = false
        }
        TransactionViewState.TransactionFetchInProgress -> {
            isInProgress = true
        }
        else -> {}
    }
    if (isInProgress) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
            )
        }

    }
    val transactionJournals = transactions?.transactionJournals
    val recentTransactions = transactionJournals?.filter {
        it.activityDate?.let { activityDate ->
            Common.isTransactionDateWithinCurrentMonth(activityDate)
        } == true
    }

    val oldTransactions = transactionJournals?.filter {
        it.activityDate?.let { activityDate ->
            Common.isTransactionDateWithinCurrentMonth(activityDate)
        } == false
    }

    recentTransactions?.let { transactionsJournals ->
        if (transactionsJournals.isNotEmpty()) {
            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = stringResource(id = R.string.label_transactions_recent),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
                LazyColumn() {
                    items(transactionsJournals) {
                        val transactionName = it.journalTypeName
                        val points = getCurrencyPoints(it.pointsChange)
                        val date = it.activityDate?.let { activityDate ->
                            formatTransactionDateTime(activityDate)
                        }
                        if (transactionName != null && points != null && date != null) {
                            ListItemTransaction(transactionName, points, date)
                        }
                    }
                }
            }
        }
    }
    oldTransactions?.let { transactionsJournals ->
        if (transactionsJournals.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = stringResource(id = R.string.label_transactions_one_month_ago),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
                LazyColumn() {
                    items(transactionsJournals) {
                        val transactionName = it.journalTypeName
                        val points = getCurrencyPoints(it.pointsChange)
                        val date = it.activityDate?.let { activityDate ->
                            formatTransactionDateTime(activityDate)
                        }
                        if (transactionName != null && points != null && date != null) {
                            ListItemTransaction(transactionName, points, date)
                        }
                    }
                }
            }
        }
    }
}

fun getCurrencyPoints(pointsChange: List<PointsChange>): Double? {
    for (pointChange in pointsChange) {
        pointChange.loyaltyMemberCurrency?.let {
            if (it.uppercase() == AppConstants.TRANSACTION_REWARD_CURRENCY_NAME.uppercase()) {
                return pointChange.changeInPoints
            }
        }
    }
    return null
}

@Composable
fun ListItemTransaction(transactionName: String, points: Double, date: String) {
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.weight(0.15f)) {
            Image(
                painter = painterResource(Assets.getTransactionsLogo(transactionName)),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(modifier = Modifier.weight(0.6f)) {
            Text(
                text = transactionName,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )
            Text(
                text = date,
                fontFamily = font_sf_pro,
                color = TextDarkGray,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )

        }
        Column(modifier = Modifier.weight(0.3f)) {
            val pointsString =
                if (points > 0) {
                    "+" + points.toString() + " " + AppConstants.TRANSACTION_REWARD_POINTS
                } else {
                    points.toString() + " " + AppConstants.TRANSACTION_REWARD_POINTS
                }
            Text(
                text = pointsString,
                fontWeight = FontWeight.Bold,
                color = TextGreen,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
            )
        }
    }
}
package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo_bold
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.TransactionViewModelInterface
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileTransactionFullScreenView(
    navProfileController: NavHostController,
    transactionViewModel: TransactionViewModelInterface
) {

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val context: Context = LocalContext.current

    fun refresh() = refreshScope.launch {
        transactionViewModel.loadTransactions(context, true)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight()
                .background(MyProfileScreenBG)
                .pullRefresh(state)
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
                    contentDescription = "transaction_back_button",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .clickable {
                            navProfileController.popBackStack()
                        }
                )
                Text(
                    text = stringResource(R.string.my_transactions),
                    fontFamily = font_archivo_bold,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 11.5.dp, bottom = 11.5.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                TransactionFullScreenListView(transactionViewModel)
            }
        }
        PullRefreshIndicator(refreshing, state)
    }

}
package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.LineItem
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_ibm_plex_mono_regular
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE

@Composable
fun ReceiptDetailTable(itemLists: List<LineItem>?) {

    val eligibleItems = itemLists?.filter { it.isEligible == true }
    val inEligibleItems = itemLists?.filter { it.isEligible == false }
    val column1Weight = 0.35f
    val column2Weight = 0.2f
    val column3Weight = 0.25f
    val column4Weight = 0.2f

    LazyColumn(
        Modifier
            .padding(8.dp)
            .background(color = Color.White, shape = RectangleShape)
            .testTag(TEST_TAG_RECEIPT_TABLE)
    ) {
        if(!eligibleItems.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(id = R.string.label_eligible_items),
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack))

            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TableCell(
                        text = stringResource(id = R.string.field_item),
                        weight = column1Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_qty),
                        weight = column2Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_unit_price),
                        weight = column3Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_total),
                        weight = column4Weight,
                        alignment = TextAlign.Right,
                        title = true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(8.dp))
            }
            itemList(eligibleItems)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Non-eligible items

        if(!inEligibleItems.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(id = R.string.label_ineligible_items),
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack))

            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TableCell(
                        text = stringResource(id = R.string.field_item),
                        weight = column1Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_qty),
                        weight = column2Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_unit_price),
                        weight = column3Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(
                        text = stringResource(id = R.string.field_total),
                        weight = column4Weight,
                        alignment = TextAlign.Right,
                        title = true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(8.dp))
            }
            itemList(inEligibleItems)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        modifier = if (title) Modifier
            .weight(weight)
            .padding(8.dp) else Modifier
            .weight(weight)
            .padding(start = 8.dp, end = 8.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        style = TextStyle(
            fontSize = 10.sp,
            lineHeight = 14.sp,
            fontFamily = font_ibm_plex_mono_regular,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF000000),
        ),
        textAlign = alignment
    )
}

@Composable
private fun DrawDashLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
    Canvas(
        Modifier.fillMaxWidth()
    ) {
        drawLine(
            color = Color(0xFF000000),
            strokeWidth = 4f,
            start = Offset(20f, 0f),
            end = Offset(size.width - 20, 0f),
            colorFilter = ColorFilter.tint(color = Color.Black),
            pathEffect = pathEffect
        )
    }
}

private fun LazyListScope.itemList(itemList: List<LineItem>) {
    val column1Weight = 0.35f
    val column2Weight = 0.2f
    val column3Weight = 0.25f
    val column4Weight = 0.2f
    items(itemList.size) {
        val invoice = itemList[it]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            invoice.productName?.let {
                TableCell(
                    text = it,
                    weight = column1Weight,
                    alignment = TextAlign.Left
                )
            }
            TableCell(
                text = invoice.quantity.toString(),
                weight = column2Weight,
                alignment = TextAlign.Left
            )
            TableCell(
                text = invoice.price.toString(),
                weight = column3Weight,
                alignment = TextAlign.Left
            )
            TableCell(
                text = invoice.lineItemPrice.toString(),
                weight = column4Weight,
                alignment = TextAlign.Right
            )
        }
    }
}
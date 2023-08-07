package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_ibm_plex_mono_regular
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE

data class Item(val itemName: String, val qty: String, val price: String, val total: String)

@Composable
fun ReceiptDetailTable() {

    val itemLists = listOf(
        Item("Converse Shoes", "1", "\$599", "\$599"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199"),
        Item("Converse Socks", "1", "\$199", "\$199")
    )

    val column1Weight = 0.4f
    val column2Weight = 0.15f
    val column3Weight = 0.2f
    val column4Weight = 0.2f

    LazyColumn(
        Modifier
            .padding(top = 16.dp)
            .background(color = Color.White, shape = RectangleShape)
            .padding(8.dp)
            .testTag(TEST_TAG_RECEIPT_TABLE)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            DrawDashLine()
            Spacer(modifier = Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TableCell(
                    text = "Item Name",
                    weight = column1Weight,
                    alignment = TextAlign.Left,
                    title = true
                )
                TableCell(
                    text = "Qty",
                    weight = column2Weight,
                    alignment = TextAlign.Left,
                    title = true
                )
                TableCell(
                    text = "Price",
                    weight = column3Weight,
                    alignment = TextAlign.Left,
                    title = true
                )
                TableCell(
                    text = "Total",
                    weight = column4Weight,
                    alignment = TextAlign.Right,
                    title = true
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            DrawDashLine()
            Spacer(modifier = Modifier.height(4.dp))
        }
        itemsIndexed(itemLists) { index, invoice ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TableCell(
                    text = invoice.itemName,
                    weight = column1Weight,
                    alignment = TextAlign.Left
                )
                TableCell(
                    text = invoice.qty,
                    weight = column2Weight,
                    alignment = TextAlign.Left
                )
                TableCell(
                    text = invoice.price,
                    weight = column3Weight,
                    alignment = TextAlign.Left
                )
                TableCell(
                    text = invoice.total,
                    weight = column4Weight,
                    alignment = TextAlign.Right
                )
            }
            if (index == itemLists.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
                DrawDashLine()
                Spacer(modifier = Modifier.height(150.dp))
            }
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
            .padding(5.dp) else Modifier
            .weight(weight)
            .padding(start = 5.dp, end = 5.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        style = TextStyle(
            fontSize = 10.sp,
            lineHeight = 14.sp,
            fontFamily = font_ibm_plex_mono_regular,
            fontWeight = FontWeight(400),
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
            strokeWidth = 5f,
            start = Offset(20f, 0f),
            end = Offset(size.width - 20, 0f),
            colorFilter = ColorFilter.tint(color = Color.Black),
            pathEffect = pathEffect
        )
    }
}
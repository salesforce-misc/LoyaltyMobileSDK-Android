package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderCreationResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OrderPlacedState

interface CheckOutFlowViewModelInterface {
    val orderPlacedStatusLiveData: LiveData<OrderPlacedState>
    fun resetOrderPlacedStatusDefault()
    val orderIDLiveData: LiveData<String>
    val orderDetailLiveData: LiveData<OrderDetailsResponse>
    val shippingDetailsLiveData: LiveData<List<ShippingMethod>>

    val orderCreationResponseLiveData: LiveData<OrderCreationResponse>
    fun placeOrder()
    fun fetchOrderDetails(orderID: String)
    fun fetchShippingDetails()

    fun placeOrderAndGetParticipantReward()

}
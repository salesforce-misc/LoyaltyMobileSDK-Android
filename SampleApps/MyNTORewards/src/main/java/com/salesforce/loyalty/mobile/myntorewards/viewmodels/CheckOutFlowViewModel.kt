package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import kotlinx.coroutines.launch

class CheckOutFlowViewModel : ViewModel() {
    private val TAG = CheckOutFlowViewModel::class.java.simpleName

    val orderPlacedStatusLiveData: LiveData<OrderPlacedState>
        get() = orderPlacedStatus

    private val orderPlacedStatus = MutableLiveData<OrderPlacedState>()

    fun resetOrderPlacedStatusDefault() {
        orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_DEFAULT_EMPTY
    }

    val orderIDLiveData: LiveData<String>
        get() = orderID

    private var orderID = MutableLiveData<String>()

    val orderDetailLiveData: LiveData<OrderDetailsResponse>
        get() = orderDetails

    private val orderDetails = MutableLiveData<OrderDetailsResponse>()

    val shippingDetailsLiveData: LiveData<List<ShippingMethod>>
        get() = shippingDetails

    private val shippingDetails = MutableLiveData<List<ShippingMethod>>()

    fun placeOrder() {
        Log.d(TAG, "Order Placed request")
        viewModelScope.launch {

            CheckoutManager.createOrder().onSuccess {
                orderID.value = it
                Log.d(TAG, "Order Placed request Success")
                orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_SUCCESS
                Log.d(TAG, "orderPlacedStatus Success: ${orderID.value.toString()}")
            }
                .onFailure {
                    orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_FAILURE
                    Log.d(TAG, "orderPlaced request failed: ${it.message}")
                }

        }
    }

    fun fetchOrderDetails(orderID: String) {
        Log.d(TAG, "Order details fetch request")
        viewModelScope.launch {

            CheckoutManager.getOrderDetails(orderID).onSuccess {
                orderDetails.value = it
                Log.d(TAG, "Order Details Success: " + orderDetails.value.toString())
            }
                .onFailure {
                    Log.d(TAG, "orderDetails request failed: ${it.message}")
                }

        }
    }

    fun fetchShippingDetails() {
        Log.d(TAG, "fetch shipping details request")
        viewModelScope.launch {

            CheckoutManager.getShippingMethods().onSuccess {
                shippingDetails.value = it
                Log.d(TAG, "shipping Details Success: " + shippingDetails.value.toString())
            }
                .onFailure {
                    Log.d(TAG, "shipping Detailsrequest failed: ${it.message}")
                }

        }
    }

}
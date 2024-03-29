package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.CheckOutFlowViewModelInterface
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class CheckOutFlowViewModel(private val checkoutManager: CheckoutManager) : ViewModel(), CheckOutFlowViewModelInterface {
    private val TAG = CheckOutFlowViewModel::class.java.simpleName

    override val orderPlacedStatusLiveData: LiveData<OrderPlacedState>
        get() = orderPlacedStatus

    private val orderPlacedStatus = MutableLiveData<OrderPlacedState>()

    override fun resetOrderPlacedStatusDefault() {
        orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_DEFAULT_EMPTY
    }

    override val orderIDLiveData: LiveData<String>
        get() = orderID

    private var orderID = MutableLiveData<String>()

    override val orderDetailLiveData: LiveData<OrderDetailsResponse>
        get() = orderDetails

    private val orderDetails = MutableLiveData<OrderDetailsResponse>()

    override val shippingDetailsLiveData: LiveData<List<ShippingMethod>>
        get() = shippingDetails

    private val shippingDetails = MutableLiveData<List<ShippingMethod>>()

    override fun placeOrder() {
        Logger.d(TAG, "Order Placed request")
        viewModelScope.launch {

            checkoutManager.createOrder().onSuccess {
                orderID.value = it
                Logger.d(TAG, "Order Placed request Success")
                orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_SUCCESS
                Logger.d(TAG, "orderPlacedStatus Success: ${orderID.value.toString()}")
            }
                .onFailure {
                    orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_FAILURE
                    Logger.d(TAG, "orderPlaced request failed: ${it.message}")
                }

        }
    }

    override fun fetchOrderDetails(orderID: String) {
        Logger.d(TAG, "Order details fetch request")
        viewModelScope.launch {

            checkoutManager.getOrderDetails(orderID).onSuccess {
                orderDetails.value = it
                Logger.d(TAG, "Order Details Success: " + orderDetails.value.toString())
            }
                .onFailure {
                    Logger.d(TAG, "orderDetails request failed: ${it.message}")
                }

        }
    }

    override fun fetchShippingDetails() {
        Logger.d(TAG, "fetch shipping details request")
        viewModelScope.launch {

            checkoutManager.getShippingMethods().onSuccess {
                shippingDetails.value = it
                Logger.d(TAG, "shipping Details Success: " + shippingDetails.value.toString())
            }
                .onFailure {
                    Logger.d(TAG, "shipping Detailsrequest failed: ${it.message}")
                }

        }
    }

}
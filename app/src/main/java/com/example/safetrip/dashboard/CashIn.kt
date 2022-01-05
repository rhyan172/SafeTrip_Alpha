package com.example.safetrip.dashboard



import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.safetrip.BuildConfig

import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

import com.example.safetrip.R
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.config.Environment.SANDBOX
import kotlinx.android.synthetic.main.activity_cash_in.*


class CashIn : AppCompatActivity() {

    private val cId = "AVB428u3HbP5g1cpKYYqc22qV6c6BplJgkvMe6zZcQLxdpJIPFGS6FWeggVRElnxEjBwp1r6Qhuqdo_L"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_in)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        PayPalCheckout.setConfig(
            checkoutConfig = CheckoutConfig(
                application = application,
                clientId = cId,
                environment = SANDBOX,
                returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                    loggingEnabled = true,
                    shouldFailEligibility = false
                )
            )
        )
        paymentButton()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun paymentButton(){
        cashInBtn.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = payAmount.text.toString())
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                    successPayment()
                }
            }
        )
    }

    private fun successPayment()
    {

    }
}
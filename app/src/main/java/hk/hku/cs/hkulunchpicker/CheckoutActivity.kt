package hk.hku.cs.hkulunchpicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import hk.hku.cs.hkulunchpicker.databinding.ActivityCheckoutBinding
import hk.hku.cs.hkulunchpicker.util.PaymentsUtil.getBaseCardPaymentMethod
import hk.hku.cs.hkulunchpicker.util.updateDatabase
import hk.hku.cs.hkulunchpicker.viewmodel.CheckoutViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Checkout implementation for the app
 */
class CheckoutActivity : AppCompatActivity() {
    // Passed from MainActivity
    private var selectedID: Int = 0
    private var selectedName: String = ""

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private val loadPaymentDataRequestCode = 991

    private val model: CheckoutViewModel by viewModels()

    private lateinit var layoutBinding: ActivityCheckoutBinding
    private lateinit var googlePayButton: PayButton

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see AppCompatActivity.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Passed from MainActivity
        selectedID = intent.getIntExtra("restaurant_id", 0)
        selectedName = intent.getStringExtra("restaurant_name").toString()

        // Use view binding to access the UI elements
        layoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(layoutBinding.root)

        val restaurantName = findViewById<TextView>(R.id.restaurant_name)
        restaurantName.text = selectedName

        payButtonInit()

        // Check whether Google Pay can be used to complete a payment
        model.canUseGooglePay.observe(this, Observer(::setGooglePayAvailable))
    }

    private fun payButtonInit() {
        googlePayButton = layoutBinding.googlePayButton
        val paymentMethods: JSONArray = JSONArray().put(getBaseCardPaymentMethod())
        googlePayButton.initialize(
            ButtonOptions.newBuilder()
                .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                .setButtonType(ButtonConstants.ButtonType.PAY)
                .setCornerRadius(100)
                .setAllowedPaymentMethods(paymentMethods.toString())
                .build()
        )
        googlePayButton.setOnClickListener { requestPayment() }
    }


    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                    this,
                    "Unfortunately, Google Pay is not available on this device",
                    Toast.LENGTH_LONG).show()

            // Switch to MainActivity
            switchActivity()
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        googlePayButton.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val dummyPriceCents = 100L
        val shippingCostCents = 0L
        val task = model.getLoadPaymentDataTask(dummyPriceCents + shippingCostCents)

        // Shows the payment sheet and forwards the result to the onActivityResult method.
        AutoResolveHelper.resolveTask(task, this, loadPaymentDataRequestCode)
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
    @Deprecated("Deprecated in Java")
    @Suppress("Deprecation")
    // Suppressing deprecation until `registerForActivityResult` is available on the Google Pay API.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            loadPaymentDataRequestCode -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                    .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token"))

            // Update the Database
            updateDatabase(this, selectedID)

            // Switch to MainActivity with landing = "Featured Restaurants"
            switchActivity()

        } catch (error: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $error")
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", "Error code: $statusCode")
    }

    private fun switchActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("landing", "Featured Restaurants")
        }
        startActivity(intent)
    }
}



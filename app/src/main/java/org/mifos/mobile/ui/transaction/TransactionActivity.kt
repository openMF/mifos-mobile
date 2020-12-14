package org.mifos.mobile.ui.transaction

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_transaction.*
import org.json.JSONObject
import org.mifos.mobile.R
import org.mifos.mobile.models.entity.*
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Constants.TEST_PAYER
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

class TransactionActivity : BaseActivity(), TransactionContract.View, View.OnClickListener {

    @Inject
    internal lateinit var transactionPresenter: TransactionPresenter
    var jsonob: JSONObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        showBackButton()
        setToolbarTitle(resources.getString(R.string.send_money))
        activityComponent?.inject(this)
        transactionPresenter.attachView(this)
        btn_send.setOnClickListener(this)
        if (intent.getStringExtra("JSON") != null) {
            jsonob = JSONObject(intent.getStringExtra("JSON"))
            et_msisdntext.setText(jsonob?.getString("mobile"))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_send -> makeTransaction()
        }
    }

    fun makeTransaction() {
        val amount = et_amounttext.text.toString().trim()
        val msisdn = et_msisdntext.text.toString().trim()
        if (TextUtils.isEmpty(amount)) {
            et_amounttext.error = getString(R.string.amount_required)
            return
        }
        if (TextUtils.isEmpty(msisdn)) {
            et_msisdntext.error = getString(R.string.msisdn_required)
            return
        }
        val transaction = Transaction(TransactingEntity
        (PartyIdInfo(IdentifierType.MSISDN, TEST_PAYER)),
                TransactingEntity(PartyIdInfo(IdentifierType.MSISDN, msisdn)), Amount("TZS", amount))
        transactionPresenter.makeTransaction(transaction)
        Toaster.show(findViewById(android.R.id.content), R.string.sending_money, Toast.LENGTH_SHORT)
    }

    override fun showTransactionSuccessfully() {
        Toast.makeText(this, R.string.transaction_successful, Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorMessage: String) {
        Toaster.show(findViewById(android.R.id.content), errorMessage, Toast.LENGTH_SHORT)
    }

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun showTransactionUnSuccessfully() {
        Toast.makeText(this, Constants.ERROR_FETCHING_ACCOUNTS, Toast.LENGTH_SHORT).show()
    }
}


package org.mifos.mobile.models

import android.os.Parcel
import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import org.mifos.mobile.api.local.SelfServiceDatabase

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
@Table(database = SelfServiceDatabase::class)
class Charge : BaseModel, Parcelable {
    @JvmField
    @PrimaryKey
    var id: Int? = null

    var clientId: Int? = null
    private var chargeId: Int? = null

    @Column
    var name: String? = null

    var dueDate: List<Int?> = ArrayList()
    private var chargeTimeType: ChargeTimeType? = null
    private var chargeCalculationType: ChargeCalculationType? = null
    var currency: Currency? = null

    @Column
    var amount = 0.0

    @Column
    var amountPaid = 0.0

    @Column
    var amountWaived = 0.0

    @Column
    var amountWrittenOff = 0.0

    @Column
    var amountOutstanding = 0.0

    var penalty = false

    @Column
    var isActive = false

    var isChargePaid: Boolean = false
    var isChargeWaived: Boolean = false

    var paid = false
    var waived = false

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(clientId)
        dest.writeValue(chargeId)
        dest.writeString(name)
        dest.writeList(dueDate)
        dest.writeParcelable(chargeTimeType, flags)
        dest.writeParcelable(chargeCalculationType, flags)
        dest.writeParcelable(currency, flags)
        dest.writeValue(amount)
        dest.writeValue(amountPaid)
        dest.writeValue(amountWaived)
        dest.writeValue(amountWrittenOff)
        dest.writeValue(amountOutstanding)
        dest.writeValue(penalty)
        dest.writeValue(isActive)
        dest.writeValue(isChargePaid)
        dest.writeValue(isChargeWaived)
        dest.writeValue(paid)
        dest.writeValue(waived)
    }

    constructor()
    private constructor(`in`: Parcel) {
        id = `in`.readValue(Int::class.java.classLoader) as Int
        clientId = `in`.readValue(Int::class.java.classLoader) as Int
        chargeId = `in`.readValue(Int::class.java.classLoader) as Int
        name = `in`.readString()
        dueDate = ArrayList()
        `in`.readList(dueDate, Int::class.java.classLoader)
        chargeTimeType = `in`.readParcelable(ChargeTimeType::class.java.classLoader)
        chargeCalculationType = `in`.readParcelable(ChargeCalculationType::class.java.classLoader)
        currency = `in`.readParcelable(Currency::class.java.classLoader)
        amount = `in`.readValue(Double::class.java.classLoader) as Double
        amountPaid = `in`.readValue(Double::class.java.classLoader) as Double
        amountWaived = `in`.readValue(Double::class.java.classLoader) as Double
        amountWrittenOff = `in`.readValue(Double::class.java.classLoader) as Double
        amountOutstanding = `in`.readValue(Double::class.java.classLoader) as Double
        penalty = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        isActive = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        isChargePaid = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        isChargeWaived = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        isChargePaid = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        isChargeWaived = `in`.readValue(Boolean::class.java.classLoader) as Boolean
    }

    companion object {
        val CREATOR: Parcelable.Creator<Charge?> = object : Parcelable.Creator<Charge?> {
            override fun createFromParcel(source: Parcel): Charge {
                return Charge(source)
            }

            override fun newArray(size: Int): Array<Charge?> {
                return arrayOfNulls(size)
            }
        }
    }
}

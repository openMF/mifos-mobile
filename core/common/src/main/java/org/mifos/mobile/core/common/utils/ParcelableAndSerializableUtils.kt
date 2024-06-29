package org.mifos.mobile.core.common.utils

import android.os.Build
import android.os.Bundle
import java.io.Serializable

object ParcelableAndSerializableUtils {

    fun <T> Bundle.getCheckedArrayListFromParcelable(classType: Class<T>, key: String): List<T>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getParcelableArrayList(key, classType)
        } else {
            this.getParcelableArrayList(key)
        }
    }

    fun <T> Bundle.getCheckedParcelable(classType: Class<T>, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getParcelable(key, classType)
        } else {
            this.getParcelable(key)
        }
    }

    fun <T : Serializable> Bundle.getCheckedSerializable(classType: Class<T>, key: String): Any? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializable(key, classType)
        } else {
            this.getSerializable(key)
        }
    }
}
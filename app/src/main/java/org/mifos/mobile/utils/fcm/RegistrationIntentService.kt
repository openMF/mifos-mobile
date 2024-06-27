/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mifos.mobile.utils.fcm

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class RegistrationIntentService : IntentService(TAG) {
    override fun onHandleIntent(intent: Intent?) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result
                    sendRegistrationToServer(token)
                },
            )
    }

    private fun sendRegistrationToServer(token: String?) {
        val registrationComplete = Intent(org.mifos.mobile.core.common.Constants.REGISTER_ON_SERVER)
        registrationComplete.putExtra(org.mifos.mobile.core.common.Constants.TOKEN, token)
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }

    companion object {
        private const val TAG = "RegIntentService"
    }
}

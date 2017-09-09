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

package org.mifos.mobilebanking.utils.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.utils.Constants;

import java.io.IOException;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private PreferencesHelper preferencesHelper;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferencesHelper = new PreferencesHelper(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            sendRegistrationToServer(token);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            preferencesHelper.setSentTokenToServer(false);
        }
    }

    private void sendRegistrationToServer(String token) {
        Intent registrationComplete = new Intent(Constants.REGISTER_ON_SERVER);
        registrationComplete.putExtra(Constants.TOKEN, token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}

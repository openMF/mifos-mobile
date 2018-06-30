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

package org.mifos.mobilebanking.utils.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;

import org.mifos.mobilebanking.utils.Constants;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Intent registrationComplete = new Intent(Constants.REGISTER_ON_SERVER);
        registrationComplete.putExtra(Constants.TOKEN, token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}

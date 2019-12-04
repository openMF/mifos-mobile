package org.mifos.mobile.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mifos.mobile.passcode.MifosPassCodeActivity;
import com.mifos.mobile.passcode.utils.EncryptionUtil;

import org.mifos.mobile.R;
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest;
import org.mifos.mobile.utils.Constants;
import org.mifos.mobile.utils.Toaster;

public class PassCodeActivity extends MifosPassCodeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckSelfPermissionAndRequest.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)) {
            requestPermission();
        }
    }

    /**
     * Uses {@link CheckSelfPermissionAndRequest} to check for runtime permissions
     */
    private void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                this,
                Manifest.permission.READ_PHONE_STATE,
                Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE,
                getResources().getString(
                        R.string.dialog_message_phone_state_permission_denied_prompt),
                getResources().getString(R.string.
                        dialog_message_phone_state_permission_never_ask_again),
                Constants.PERMISSIONS_READ_PHONE_STATE_STATUS);
    }

    @Override
    public int getLogo() {
        return R.drawable.mifos_logo;
    }

    @Override
    public void startNextActivity() {
        startActivity(new Intent(PassCodeActivity.this, HomeActivity.class));
    }

    @Override
    public void startLoginActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to Login using Password?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(PassCodeActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", null);
                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.BLACK);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
    }
    @Override
    public void showToaster(View view, int msg) {
        Toaster.show(view, msg);
    }

    @Override
    public int getEncryptionType() {
        return EncryptionUtil.MOBILE_BANKING;
    }

}

package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.mifos.selfserviceapp.MifosSelfServiceApp;
import org.mifos.selfserviceapp.injection.component.ActivityComponent;
import org.mifos.selfserviceapp.injection.component.DaggerActivityComponent;
import org.mifos.selfserviceapp.injection.module.ActivityModule;

/**
 * @author ishan
 * @since 08/07/16
 */
public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MifosSelfServiceApp.get(this).component())
                    .build();
        }
        return activityComponent;
    }

}

package org.mifos.selfserviceapp;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import io.fabric.sdk.android.Fabric;
import org.mifos.selfserviceapp.injection.component.ApplicationComponent;
import org.mifos.selfserviceapp.injection.component.DaggerApplicationComponent;
import org.mifos.selfserviceapp.injection.module.ApplicationModule;
import org.mifos.selfserviceapp.utils.ForegroundChecker;

/**
 * @author ishan
 * @since 08/07/16
 */
public class MifosSelfServiceApp extends Application {

    ApplicationComponent applicationComponent;

    private static MifosSelfServiceApp instance;

    public static MifosSelfServiceApp get(Context context) {
        return (MifosSelfServiceApp) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        FlowManager.init(new FlowConfig.Builder(this).build());
        ForegroundChecker.init(this);
    }

    public ApplicationComponent component() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}

package org.mifos.selfserviceapp;

import android.app.Application;
import android.content.Context;

import org.mifos.selfserviceapp.injection.component.ApplicationComponent;
import org.mifos.selfserviceapp.injection.component.DaggerApplicationComponent;
import org.mifos.selfserviceapp.injection.module.ApplicationModule;

/**
 * @author ishan
 * @since 08/07/16
 */
public class MifosSelfServiceApp extends Application {

    ApplicationComponent applicationComponent;

    private static MifosSelfServiceApp instance;

    public static MifosSelfServiceApp getInstance() {
        return instance;
    }

    public static MifosSelfServiceApp get(Context context) {
        return (MifosSelfServiceApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}

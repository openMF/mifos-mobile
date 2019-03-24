package org.mifos.mobile;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.mifos.mobile.passcode.utils.ForegroundChecker;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.mifos.mobile.injection.component.ApplicationComponent;
import org.mifos.mobile.injection.component.DaggerApplicationComponent;
import org.mifos.mobile.injection.module.ApplicationModule;
import org.mifos.mobile.utils.LanguageHelper;

import androidx.appcompat.app.AppCompatDelegate;
import io.fabric.sdk.android.Fabric;

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

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base, "en"));
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

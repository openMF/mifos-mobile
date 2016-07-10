package org.mifos.selfserviceapp.injection.component;

import org.mifos.selfserviceapp.injection.PerActivity;
import org.mifos.selfserviceapp.injection.module.ActivityDataModule;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.activities.LoginActivity;

import dagger.Component;

/**
 * @author ishan
 * @since 08/07/16
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ActivityDataModule.class})
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

}

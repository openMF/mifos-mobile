package org.mifos.mobile.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Activity to be memorised in the
 * correct component.
 *
 * @author ishan
 * @since 08/07/16
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}

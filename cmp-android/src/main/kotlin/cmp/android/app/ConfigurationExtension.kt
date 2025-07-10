
package cmp.android.app

import android.content.res.Configuration

val Configuration.isSystemInDarkMode
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

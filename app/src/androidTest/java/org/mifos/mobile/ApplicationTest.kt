package org.mifos.mobile

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
@RunWith(AndroidJUnit4::class)
class ApplicationTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("org.mifos.mobile", appContext.packageName)
    }
}
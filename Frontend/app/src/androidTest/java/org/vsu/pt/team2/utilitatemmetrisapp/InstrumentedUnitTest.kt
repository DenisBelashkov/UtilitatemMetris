package org.vsu.pt.team2.utilitatemmetrisapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedUnitTest {
    private val prefName = "TEST_PREF"
    @Test
    fun test_sharedPref() {
        val mContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mSharedPreferences = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = mSharedPreferences.edit()
        editor.putString("key", "value")
        editor.apply()
        val mSharedPreferences2 = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        MatcherAssert.assertThat("value", Matchers.`is`(mSharedPreferences2.getString("key", null)))
    }
}
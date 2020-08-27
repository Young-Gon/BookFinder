package com.gondev.bookfinder

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gondev.bookfinder.di.networkModule
import com.gondev.bookfinder.model.network.api.BookAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest: KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(listOf(
            networkModule,
        ))
    }
    private val api: BookAPI by inject()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.gondev.bookfinder", appContext.packageName)
        CoroutineScope(Dispatchers.IO).launch {
            val result = api.requestBooks("life")
            assertEquals(20,result.items.size)
        }

    }
}
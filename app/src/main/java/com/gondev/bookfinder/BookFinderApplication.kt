package com.gondev.bookfinder

import android.app.Application
import com.gondev.bookfinder.di.networkModule
import com.gondev.bookfinder.di.roomModule
import com.gondev.bookfinder.di.viewModelModule
import com.gondev.bookfinder.util.timber.DebugLogTree
import com.gondev.bookfinder.util.timber.ReleaseLogTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@Suppress("unused")
class BookFinderApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree())
        }
        else{
            Timber.plant(ReleaseLogTree())
        }

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger(Level.ERROR)

            androidContext(this@BookFinderApplication)
            modules(listOf(
                networkModule,
                roomModule,
                viewModelModule,
            ))
        }
    }
}
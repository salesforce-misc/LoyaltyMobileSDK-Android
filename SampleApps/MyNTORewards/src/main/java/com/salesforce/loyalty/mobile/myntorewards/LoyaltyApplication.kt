package com.salesforce.loyalty.mobile.myntorewards

import android.app.Application
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger

class LoyaltyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //clear cache
        Logger.d("LoyaltyApplication", "onCreate() called")
        LocalFileManager.clearAllFolders(applicationContext)
    }
}
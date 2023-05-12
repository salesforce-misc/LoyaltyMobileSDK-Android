package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get

object AppSettings {

    val DEFAULT_FORCE_CONNECTED_APP = ConnectedApp(
        "Default",
        "3MVG9SemV5D80oBcyrqrDrrcbLgQBxn3kO2L9XD1sPRm315AGmtHcq1dFUMHh6vSotuV1uuZPfw==",
        "0ADA86BBC47966C01405CA64A6F409AB15BF9F80E9F83A1B098AB6136AEAC434",
        "https://hutl.my.site.com/oauth2/callback",
        "https://login.salesforce.com",
        "https://hutl.my.salesforce.com",
        "https://hutl.my.site.com"
    )

}
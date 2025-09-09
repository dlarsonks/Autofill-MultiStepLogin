package com.test.autofill.multisteplogin.customtab

import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import androidx.core.net.toUri
import com.test.autofill.multisteplogin.util.logD

/**
 * Helper class for Custom Tabs.
 *
 * https://github.com/GoogleChrome/android-browser-helper/blob/dc788207822576f6c867ff28d470ec51ad06d178/demos/custom-tabs-example-app/src/main/java/org/chromium/customtabsdemos/CustomTabsHelper.java
 */
object CustomTabsHelper {
    private const val TAG = "CustomTabsHelper"
    private const val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"

    private var sPackageNameToUse: String? = null

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is **not** threadsafe.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getPackageNameToUse(context: Context): String? {
        if (sPackageNameToUse != null) return sPackageNameToUse

        val pm = context.packageManager

        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, "http://www.example.com".toUri())
        activityIntent.addCategory(CATEGORY_BROWSABLE)
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)

        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        logD { "getPackageNameToUse: default browser = $defaultViewHandlerPackageName" }

        // Get all apps that can handle VIEW intents.
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, PackageManager.MATCH_ALL)
        logD { "getPackageNameToUse: browsers found = $resolvedActivityList" }

        val packagesSupportingCustomTabs = mutableListOf<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION)
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        logD { "getPackageNameToUse: packagesSupportingCustomTabs = $packagesSupportingCustomTabs" }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls. Prefer the default browser if it supports Custom Tabs.
        if (packagesSupportingCustomTabs.isEmpty()) {
            sPackageNameToUse = null
        } else if (packagesSupportingCustomTabs.size == 1) {
            sPackageNameToUse = packagesSupportingCustomTabs[0]
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName) &&
            !hasSpecializedHandlerIntents(context, activityIntent) &&
            packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)
        ) {
            sPackageNameToUse = defaultViewHandlerPackageName
        } else if (packagesSupportingCustomTabs.size > 1) {
            sPackageNameToUse = packagesSupportingCustomTabs.first()
        } else {
            // Otherwise, pick the next favorite Custom Tabs provider.
            sPackageNameToUse = packagesSupportingCustomTabs.get(0)
        }

        logD { "getPackageNameToUse: sPackageNameToUse = $sPackageNameToUse" }

        return sPackageNameToUse
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER
            )
            if (handlers.isEmpty()) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter
                if (filter == null) continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            Log.e(TAG, "Runtime exception while getting specialized handlers", e)
        }
        return false
    }

}

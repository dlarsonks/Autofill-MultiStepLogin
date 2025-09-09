package com.test.autofill.multisteplogin.customtab

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.logE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Created by dlarson on 9/4/25
 */
class CustomTabViewModel : ViewModel() {

    val url = mutableStateOf("facebook.com")

    private val _viewEvents = MutableSharedFlow<CustomTabViewEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val viewEvents: SharedFlow<CustomTabViewEvent> = _viewEvents.asSharedFlow()

    fun openUrlInCustomTab() {
        val localUrl = url.value

        logD { "openUrlInCustomTab: $localUrl" }

        if (!Patterns.WEB_URL.matcher(localUrl).matches()) {
            logE { "openUrlInCustomTab: not a valid url" }
            _viewEvents.tryEmit(CustomTabViewEvent.CustomTabToastEvent("Invalid URL"))
            return
        }

        val urlWithScheme = if (!localUrl.startsWith("http://") && !localUrl.startsWith("https://")) {
            "https://${localUrl}"
        } else {
            localUrl
        }

        _viewEvents.tryEmit(CustomTabViewEvent.CustomTabOpenUrlEvent(urlWithScheme))
    }

}

sealed class CustomTabViewEvent {
    data class CustomTabToastEvent(val toastMessage: String) : CustomTabViewEvent()
    data class CustomTabOpenUrlEvent(val url: String) : CustomTabViewEvent()
}

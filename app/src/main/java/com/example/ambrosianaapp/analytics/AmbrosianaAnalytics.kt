package com.example.ambrosianaapp.analytics

import android.util.Log
import com.amplifyframework.analytics.AnalyticsEvent
import com.amplifyframework.core.Amplify

object AmbrosianaAnalytics {
    private const val TAG = "AmbrosianaAnalytics"
    private const val EVENT_TYPE_API = "api_call"
    private const val ENDPOINT_PROPERTY = "endpoint"
    private const val STATUS_PROPERTY = "status"
    private const val ERROR_TYPE_PROPERTY = "error_type"
    private const val ERROR_MESSAGE_PROPERTY = "error_message"
    private const val DURATION_PROPERTY = "duration_ms"

    fun trackApiCall(
        endpoint: String,
        isSuccess: Boolean,
        durationMs: Long,
        errorType: String? = null,
        errorMessage: String? = null,
        properties: Map<String, String>? = emptyMap()
    ) {
        try {
            val eventBuilder = AnalyticsEvent.builder().name(EVENT_TYPE_API)
                .addProperty(ENDPOINT_PROPERTY, endpoint)
                .addProperty(STATUS_PROPERTY, if (isSuccess) "success" else "failure")
                .addProperty(DURATION_PROPERTY, "$durationMs")

            errorType?.let { eventBuilder.addProperty(ERROR_TYPE_PROPERTY, it) }
            errorMessage?.let { eventBuilder.addProperty(ERROR_MESSAGE_PROPERTY, it) }

            properties?.forEach { (key, value) ->
                eventBuilder.addProperty(key, value)
            }

            val event = eventBuilder.build()

            try {
                Amplify.Analytics.recordEvent(event)
                Log.d(TAG, "Successfully recorded analytics event for $endpoint")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to record analytics event", e)
            }
        } catch (e: Exception) {
            // Catch any exceptions to prevent analytics from crashing the app
            Log.e(TAG, "Error creating analytics event", e)
        }
    }

    fun recordAppEvent(eventName: String, properties: Map<String, String> = emptyMap()) {
        try {
            val eventBuilder = AnalyticsEvent.builder().name(eventName)

            properties.forEach { (key, value) ->
                eventBuilder.addProperty(key, value)
            }

            val event = eventBuilder.build()

            try {
                Amplify.Analytics.recordEvent(event)
                Log.d(TAG, "Successfully recorded app event: $eventName")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to record app event: $eventName", e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating app event: $eventName", e)
        }
    }
}
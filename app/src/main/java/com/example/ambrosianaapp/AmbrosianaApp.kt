package com.example.ambrosianaapp

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.configuration.AmplifyOutputs
import com.amplifyframework.geo.location.AWSLocationGeoPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.amplifyframework.core.Amplify as JavaAmplify

class AmbrosianaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            // Configure Analytics plugin first
            val analyticsPlugin = AWSPinpointAnalyticsPlugin()
            Amplify.addPlugin(analyticsPlugin)
            //Amplify Plugin installation
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSLocationGeoPlugin())


            Amplify.configure(AmplifyOutputs(R.raw.amplify_outputs), applicationContext)
            JavaAmplify.Analytics.recordEvent("APP_START")

            Log.i("AmbrosianaApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("AbrosianaApp", "Could not initialize Amplify", error)
        }
    }
}
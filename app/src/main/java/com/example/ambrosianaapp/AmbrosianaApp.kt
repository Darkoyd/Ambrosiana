package com.example.ambrosianaapp

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.core.configuration.AmplifyOutputs
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.storage.s3.AWSS3StoragePlugin

class AmbrosianaApp: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            //Amplify Plugin installation
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin());


            Amplify.configure(AmplifyOutputs(R.raw.amplify_outputs), applicationContext)
            Log.i("AmbrosianaApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("AbrosianaApp", "Could not initialize Amplify", error)
        }
    }
}
package com.example.ambrosianaapp

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.configuration.AmplifyOutputs
import com.amplifyframework.api.aws.AWSApiPlugin

class AmbrosianaApp: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            //Amplify Plugin installation
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())

            Amplify.configure(AmplifyOutputs(R.raw.amplify_outputs), applicationContext)
            Log.i("AmbrosianaApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("AbrosianaApp", "Could not initialize Amplify", error)
        }
    }
}
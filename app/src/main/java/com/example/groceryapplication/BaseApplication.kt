package com.example.groceryapplication

import android.app.Application
import com.example.groceryapplication.di.AppComponent
import com.example.groceryapplication.di.DaggerAppComponent

open class BaseApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
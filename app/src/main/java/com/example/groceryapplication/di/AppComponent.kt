package com.example.groceryapplication.di

import android.content.Context
import com.example.groceryapplication.ui.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    //fun inject(activity: MainActivity)
    fun inject(activity: CreateItemActivity)
    fun inject(activity: CreateListActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: GroceryListFragment)
    fun inject(fragment: GroceryItemsFragment)
}
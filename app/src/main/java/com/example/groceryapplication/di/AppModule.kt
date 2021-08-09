package com.example.groceryapplication.di

import android.content.Context
import com.example.groceryapplication.GroceryDatabase
import com.example.groceryapplication.dataSource.LocalDataSource
import com.example.groceryapplication.di.ViewModelModules.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGroceryDatabase(context: Context): GroceryDatabase {
        return GroceryDatabase(context.applicationContext)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(database: GroceryDatabase): LocalDataSource {
        return LocalDataSource(database)
    }
}
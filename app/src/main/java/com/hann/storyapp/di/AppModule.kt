package com.hann.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hann.storyapp.BuildConfig
import com.hann.storyapp.data.StoryRepository
import com.hann.storyapp.data.remote.RemoteDataSource
import com.hann.storyapp.data.remote.network.ApiService
import com.hann.storyapp.domain.repository.IStoryRepository
import com.hann.storyapp.domain.usecase.StoryInteractor
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.presentation.add.AddStoryViewModel
import com.hann.storyapp.presentation.detail.DetailViewModel
import com.hann.storyapp.presentation.login.LoginViewModel
import com.hann.storyapp.presentation.main.MainViewModel
import com.hann.storyapp.presentation.map.MapViewModel
import com.hann.storyapp.presentation.register.RegisterViewModel
import com.hann.storyapp.ui.preference.UserPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}



val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single<IStoryRepository> { StoryRepository(get()) }
}

val useCaseModule = module {
    factory<StoryUseCase> {
        StoryInteractor(get())
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val preferenceModule = module {
    factory {
        UserPreference.getInstance(androidContext().dataStore)
    }
}

val viewModelModule = module {

    viewModel {
        RegisterViewModel(get())
    }
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        MainViewModel(get(), get(), get())
    }
    viewModel {
        DetailViewModel(get())
    }
    viewModel {
        MapViewModel(get(), get())
    }
    viewModel {
        AddStoryViewModel(get(), get())
    }
}

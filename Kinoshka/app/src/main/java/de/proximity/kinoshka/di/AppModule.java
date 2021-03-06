package de.proximity.kinoshka.di;

import android.app.Application;
import android.content.ContentResolver;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.proximity.kinoshka.BuildConfig;
import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ApiClient;
import de.proximity.kinoshka.data.remote.MovieTaskImpl;
import de.proximity.kinoshka.ui.favorites.FavoritesViewModel;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsViewModel;
import de.proximity.kinoshka.ui.movielist.MovieListViewModel;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    ApiClient provideApiClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG)
            enableLogging(httpClient);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        return retrofit.create(ApiClient.class);
    }

    private void enableLogging(OkHttpClient.Builder httpClient) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
    }

    @Singleton
    @Provides
    public MovieTask providesMovieTask(ApiClient apiClient, ContentResolver contentResolver) {
        return new MovieTaskImpl(apiClient, contentResolver);
    }


    @Provides
    ContentResolver provideContentResolver(Application application) {
        return application.getContentResolver();
    }

    @Singleton
    @Provides
    FavoritesViewModel provideFavoritesViewModel(MovieTask task) {
        return new FavoritesViewModel(task);
    }

    @Singleton
    @Provides
    MovieListViewModel provideMovieListViewModel(MovieTask task) {
        return new MovieListViewModel(task);
    }

    @Singleton
    @Provides
    MovieDetailsViewModel provideMovieDetailsViewModel(MovieTask task) {
        return new MovieDetailsViewModel(task);
    }
}

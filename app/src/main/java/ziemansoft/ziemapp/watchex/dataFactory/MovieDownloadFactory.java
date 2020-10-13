package ziemansoft.ziemapp.watchex.dataFactory;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDownloadFactory {
    private static MovieDownloadFactory movieDownloadFactory;
    private Retrofit retrofit;
    private final String BASE_URL = "https://api.themoviedb.org/";

    public static MovieDownloadFactory getMovieDownloadFactory() {
        if (movieDownloadFactory == null) {
            movieDownloadFactory = new MovieDownloadFactory();
        }
        return movieDownloadFactory;
    }

    public MovieDownloadFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public MovieFactoryService getMovieFactoryService(){
        return retrofit.create(MovieFactoryService.class);
    }
}

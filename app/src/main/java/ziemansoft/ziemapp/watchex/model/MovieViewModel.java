package ziemansoft.ziemapp.watchex.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ziemansoft.ziemapp.watchex.dataFactory.MovieDownloadFactory;
import ziemansoft.ziemapp.watchex.dataFactory.MovieFactoryService;
import ziemansoft.ziemapp.watchex.database.MoviesDatabase;
import ziemansoft.ziemapp.watchex.pojo.GetTrailer;
import ziemansoft.ziemapp.watchex.pojo.LikedMovie;
import ziemansoft.ziemapp.watchex.pojo.Movie;
import ziemansoft.ziemapp.watchex.pojo.MovieTrailer;
import ziemansoft.ziemapp.watchex.pojo.ResultsResponse;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "cce0d189833afd248f4eaf6cac887086";
    private static final String LANGUAGE = "en-US";
    private static final String POPULARITY = "popularity.desc";
    private static final String RATED = "vote_average.desc";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;
    private static MoviesDatabase database;
    private LikedMovie likedMovie;

    public void contextUser(Context context) {
        database = MoviesDatabase.getInstance(context);
        movies = database.movieDao().getAllMovies();
        likedMovies = database.movieDao().getLikedMovies();
    }

    private LiveData<List<Movie>> movies;
    private LiveData<List<LikedMovie>> likedMovies;
    private LiveData<List<MovieTrailer>> trailers;


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<LikedMovie>> getLikedMovies() {
        return likedMovies;
    }

    public LiveData<List<MovieTrailer>> getTrailers() {
        return trailers;
    }

    public void setMovies(LiveData<List<Movie>> movies) {
        this.movies = movies;
    }

    @SuppressWarnings("unchecked")
    private void insertAllMovies(List<Movie> movies) {
        new InsertMovies().execute(movies);
    }

    private void insertLikedMovie(LikedMovie likedMovie) {
        new InsertLikedMovie().execute(likedMovie);
    }

    private void deleteAllMovies() {
        new DeleteMovies().execute();
    }

    private void deleteLikedMovie(LikedMovie likedMovie) {
        new DeleteLikedMovie().execute(likedMovie);
    }


    public Movie getMovie(int i) {
        try {
            return new GetMovieById().execute(i).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LikedMovie getLikedMovie(int i) {
        try {
            return new GetLikedMovie().execute(i).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint("StaticFieldLeak")
    private static class InsertMovies extends AsyncTask<List<Movie>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Movie>... lists) {
            if (lists != null && lists.length > 0) {
                database.movieDao().insertMovie(lists[0]);
            }
            return null;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private static class InsertLikedMovie extends AsyncTask<LikedMovie, Void, Void> {
        @Override
        protected Void doInBackground(LikedMovie... likedMovies) {
            if (likedMovies != null) {
                database.movieDao().insertLikedMovie(likedMovies[0]);
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class GetMovieById extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null) {
                return database.movieDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class GetLikedMovie extends AsyncTask<Integer, Void, LikedMovie> {
        @Override
        protected LikedMovie doInBackground(Integer... integers) {
            if (integers != null) {
                return database.movieDao().getLikedMovie(integers[0]);
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class DeleteMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            database.movieDao().deleteAllMovies();
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class DeleteLikedMovie extends AsyncTask<LikedMovie, Void, Void> {
        @Override
        protected Void doInBackground(LikedMovie... likedMovies) {
            if (likedMovies != null) {
                database.movieDao().deleteLikedMovie(likedMovies[0]);
            }
            return null;
        }
    }

    public void loadData(int i) {
        String sortType = null;
        if (i == 0) {
            sortType = POPULARITY;
        } else if (i == 1) {
            sortType = RATED;
        }
        MovieDownloadFactory movieDownloadFactory = MovieDownloadFactory.getMovieDownloadFactory();
        MovieFactoryService movieFactoryService = movieDownloadFactory.getMovieFactoryService();
        disposable = movieFactoryService.getResultsResponse(API_KEY, LANGUAGE, sortType, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultsResponse>() {
                    @Override
                    public void accept(ResultsResponse resultsResponses) throws Exception {
                        if (resultsResponses != null) {
                            deleteAllMovies();
                            insertAllMovies(resultsResponses.getMovies());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("WrongMessage", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getMovieTrailers(int id) {
        MovieDownloadFactory movieDownloadFactory = MovieDownloadFactory.getMovieDownloadFactory();
        MovieFactoryService movieFactoryService = movieDownloadFactory.getMovieFactoryService();
        disposable = movieFactoryService.getTrailers(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetTrailer>() {
                    @Override
                    public void accept(GetTrailer getTrailer) throws Exception {
                        if (getTrailer != null) {
                            
                            Log.i("Results", getTrailer.getResults().toString());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("WrongMessage", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
        compositeDisposable.add(disposable);
    }


    public void setLikedMovieStatus(int i) {
        likedMovie = getLikedMovie(i);
        Movie movie = getMovie(i);
        if (likedMovie == null) {
            insertLikedMovie(new LikedMovie(movie));
        } else {
            deleteLikedMovie(likedMovie);
        }
    }

    public Boolean checkLikedMovie(int i) {
        boolean mBoolean = false;
        likedMovie = getLikedMovie(i);
        if (likedMovie == null) {
            return mBoolean;
        } else {
            mBoolean = true;
        }
        return mBoolean;
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}
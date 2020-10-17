package ziemansoft.ziemapp.watchex.model;

import android.util.Log;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ziemansoft.ziemapp.watchex.adapter.TrailerAdapters;
import ziemansoft.ziemapp.watchex.dataFactory.MovieDownloadFactory;
import ziemansoft.ziemapp.watchex.dataFactory.MovieFactoryService;
import ziemansoft.ziemapp.watchex.pojo.GetTrailer;

public class TrailerPresenterModel {
    private Disposable disposable;
    private static final String API_KEY = "cce0d189833afd248f4eaf6cac887086";

    private TrailersSettingModel model;

    public TrailerPresenterModel(TrailersSettingModel model) {
        this.model = model;
    }


    public void getMovieTrailers(int id) {
        TrailerAdapters adapters = new TrailerAdapters();
        MovieDownloadFactory movieDownloadFactory = MovieDownloadFactory.getMovieDownloadFactory();
        MovieFactoryService movieFactoryService = movieDownloadFactory.getMovieFactoryService();
        disposable = movieFactoryService.getTrailers(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetTrailer>() {
                    @Override
                    public void accept(GetTrailer getTrailer) throws Exception {
                        if (getTrailer != null) {
                            model.show(getTrailer.getResults());
                            Log.i("Results", getTrailer.getResults().toString());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("WrongMessage", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
    }
}

package ziemansoft.ziemapp.watchex.dataFactory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ziemansoft.ziemapp.watchex.pojo.GetTrailer;
import ziemansoft.ziemapp.watchex.pojo.ResultsResponse;

public interface MovieFactoryService {
    @GET("3/discover/movie")
    Observable<ResultsResponse> getResultsResponse(@Query("api_key") String API_KEY,
                                                         @Query("language") String LANG,
                                                         @Query("sort_by") String SORT_BY,
                                                         @Query("page") int page);

    @GET("3/movie/{movie_id}/videos")
    Observable<GetTrailer> getTrailers(@Path("movie_id") int id,
                                       @Query("api_key") String API_KEY);
}

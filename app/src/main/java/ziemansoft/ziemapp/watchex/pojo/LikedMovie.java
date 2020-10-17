package ziemansoft.ziemapp.watchex.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "likedMovie")
public class LikedMovie extends Movie {
    public LikedMovie(int uniqId,double popularity, int voteCount, boolean video, String posterPath, int id, boolean adult, String backdropPath, String originalLanguage, String originalTitle, String title, double voteAverage, String overview, String releaseDate) {
        super(uniqId,popularity, voteCount, video, posterPath, id, adult, backdropPath, originalLanguage, originalTitle, title, voteAverage, overview, releaseDate);
    }

    @Ignore
    public LikedMovie(Movie movie){
        super(movie.getUniqId(), movie.getPopularity(), movie.getVoteCount(), movie.isVideo(), movie.getPosterPath(), movie.getId(), movie.isAdult(), movie.getBackdropPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(), movie.getTitle(), movie.getVoteAverage(), movie.getOverview(), movie.getReleaseDate());
    }
}
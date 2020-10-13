package ziemansoft.ziemapp.watchex.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ziemansoft.ziemapp.watchex.pojo.LikedMovie;
import ziemansoft.ziemapp.watchex.pojo.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();


    @Query("SELECT * FROM likedMovie")
    LiveData<List<LikedMovie>> getLikedMovies();

    @Query("SELECT * FROM movie WHERE id == :movieId")
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM likedMovie WHERE id ==:likedMovieId")
    LikedMovie getLikedMovie(int likedMovieId);


    @Delete
    void deleteMovie(Movie movie);

    @Delete
    void deleteLikedMovie(LikedMovie likedMovie);

    @Insert
    void insertMovie(List<Movie> movie);

    @Insert
    void insertLikedMovie(LikedMovie likedMovie);

    @Query("DELETE FROM movie")
    void deleteAllMovies();

    @Query("DELETE FROM likedMovie")
    void deleteAllLikedMovie();
}

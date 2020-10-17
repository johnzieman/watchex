package ziemansoft.ziemapp.watchex.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ziemansoft.ziemapp.watchex.pojo.LikedMovie;
import ziemansoft.ziemapp.watchex.pojo.Movie;

@Database(entities = {Movie.class, LikedMovie.class}, version = 3, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {
    private static MoviesDatabase database;
    private static final Object LOCK = new Object();

    public static MoviesDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MoviesDatabase.class, "database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return database;
        }
    }

    public abstract MovieDao movieDao();
}

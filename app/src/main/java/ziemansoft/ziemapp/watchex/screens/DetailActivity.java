package ziemansoft.ziemapp.watchex.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ziemansoft.ziemapp.watchex.R;
import ziemansoft.ziemapp.watchex.adapter.TrailerAdapters;
import ziemansoft.ziemapp.watchex.model.MovieViewModel;
import ziemansoft.ziemapp.watchex.pojo.Movie;
import ziemansoft.ziemapp.watchex.pojo.MovieTrailer;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView title;
    private TextView releaseDate;
    private TextView vote_average;
    private TextView description;
    private ImageView imageViewAdded;
    private TextView textView;
    private RecyclerView recyclerView;
    private TrailerAdapters adapters;
    private List<MovieTrailer> traiers;
    private Disposable disposable;

    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_SIZE = "w185";
    private static final String BIG_SIZE = "w780";

    private MovieViewModel viewModel;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        if (intent.hasExtra("movieId")) {
            id = intent.getIntExtra("movieId", -1);
        } else {
            finish();
        }
        adapters = new TrailerAdapters();
        recyclerView = findViewById(R.id.recyclerViewTrailers);
        imageView = findViewById(R.id.bigPoster);
        title = findViewById(R.id.titleValue);
        releaseDate = findViewById(R.id.releaseDate);
        vote_average = findViewById(R.id.voteAverage);
        description = findViewById(R.id.descriptionValue);
        imageViewAdded = findViewById(R.id.imageView2);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.contextUser(this);
        Movie movie = viewModel.getMovie(id);
        Picasso.get().load(BASE_URL + BIG_SIZE + movie.getPosterPath()).into(imageView);
        title.setText(movie.getTitle());
        releaseDate.setText("Release date: " + movie.getReleaseDate());
        vote_average.setText("Vote average: " + movie.getVoteAverage());
        description.setText(movie.getOverview());
        checkStatus();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapters);
    }


    public void changeStatus(View view) {
        viewModel.setLikedMovieStatus(id);
        checkStatus();
    }

    private void checkStatus() {
        boolean mBoolean = viewModel.checkLikedMovie(id);
        if (mBoolean) {
            imageViewAdded.setImageResource(R.drawable.heart_on);
        } else {
            imageViewAdded.setImageResource(R.drawable.heart_off);
        }
    }

    public void setTraiers(List<MovieTrailer> traiers) {
        this.traiers = traiers;
    }

    public void openLink(View view) {
    }
}
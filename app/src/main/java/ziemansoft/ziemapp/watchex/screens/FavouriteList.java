package ziemansoft.ziemapp.watchex.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

import ziemansoft.ziemapp.watchex.R;
import ziemansoft.ziemapp.watchex.adapter.HorizontalMenu;
import ziemansoft.ziemapp.watchex.adapter.MovieAdapter;
import ziemansoft.ziemapp.watchex.model.MovieViewModel;
import ziemansoft.ziemapp.watchex.pojo.LikedMovie;
import ziemansoft.ziemapp.watchex.pojo.Movie;

public class FavouriteList extends AppCompatActivity {
    private HorizontalMenu horizontalMenu;
    private MovieAdapter adapter;
    private RecyclerView recyclerViewMenu;
    private RecyclerView recyclerView;
    private List<String> menuList;

    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        horizontalMenu = new HorizontalMenu();
        adapter = new MovieAdapter();
        menuList = MainActivity.getMenus();
        recyclerViewMenu = findViewById(R.id.recyclerViewHorizontal);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getDisplayMetrics()));
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalMenu.setMenus(menuList);
        recyclerView.setAdapter(adapter);
        recyclerViewMenu.setAdapter(horizontalMenu);
        horizontalMenu.setItemClickListener(new HorizontalMenu.ItemClickListener() {
            @Override
            public void getClickedItem(int i) {
                switch (i) {
                    case 0:
                        Intent popular = new Intent(FavouriteList.this, MainActivity.class);
                        popular.putExtra("popular", i);
                        startActivity(popular);
                        break;
                    case 1:
                        Intent rated = new Intent(FavouriteList.this, MainActivity.class);
                        rated.putExtra("rated", i);
                        startActivity(rated);
                        break;
                    case 2:
                    case 4:
                    default:
                        break;
                }
            }
        });
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int i) {
                Movie movie = adapter.getMovies().get(i);
                Intent intent = new Intent(FavouriteList.this, DetailActivity.class);
                intent.putExtra("movieId", movie.getId());
                startActivity(intent);
            }
        });

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.contextUser(this);
        viewModel.getLikedMovies().observe(this, new Observer<List<LikedMovie>>() {
            @Override
            public void onChanged(List<LikedMovie> likedMovies) {
                if (likedMovies != null) {
                    List<Movie> movies = new ArrayList<>(likedMovies);
                    adapter.setMovies(movies);
                }
            }
        });
    }

    private int getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = (int) (metrics.widthPixels / metrics.density);
        return Math.max(width / 185, 2);
    }
}
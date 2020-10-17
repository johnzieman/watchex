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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ziemansoft.ziemapp.watchex.R;
import ziemansoft.ziemapp.watchex.adapter.HorizontalMenu;
import ziemansoft.ziemapp.watchex.adapter.MovieAdapter;
import ziemansoft.ziemapp.watchex.model.GetMovieAdapter;
import ziemansoft.ziemapp.watchex.model.MovieViewModel;
import ziemansoft.ziemapp.watchex.pojo.Movie;

public class MainActivity extends AppCompatActivity{
    private static List<String> menus;
    private MovieViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewMenu;
    private MovieAdapter adapter;
    private HorizontalMenu menuAdapter;
    private List<Movie> movies;
    private static int page = 1;
    private int numb;
    private static boolean isLoading = false;

    public static List<String> getMenus() {
        return menus;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewMenu = findViewById(R.id.recyclerViewHorizontal);
        adapter = new MovieAdapter();
        menuAdapter = new HorizontalMenu();
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movies = new ArrayList<>();
        menus = new ArrayList<>();
        adapter.setMovies(movies);
        menuAdapter.setMenus(menus);
        recyclerViewMenu.setAdapter(menuAdapter);
        recyclerView.setAdapter(adapter);
        menus.add("Popular now");
        menus.add("Top rated");
        menus.add("Favourite list");
        menus.add("Select genre");
        menuAdapter.setMenus(menus);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.contextUser(this);

        Intent intent = getIntent();
        if (intent.hasExtra("popular")) {
            numb = intent.getIntExtra("popular", 0);
            switchBox(numb);
        } else if (intent.hasExtra("rated")) {
            numb = intent.getIntExtra("rated", 1);
            switchBox(numb);
        }
        getData(0);


        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1) {
                    adapter.setMovies(movies);
                }
            }
        });

        menuAdapter.setItemClickListener(new HorizontalMenu.ItemClickListener() {
            @Override
            public void getClickedItem(int i) {
                switchBox(i);
            }
        });
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int i) {
                Movie movie = adapter.getMovies().get(i);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movieId", movie.getId());
                startActivity(intent);
            }
        });
        adapter.setGetEndListener(new MovieAdapter.OnGetEndListener() {
            @Override
            public void getEndListener() {
                if (!isLoading) {
                    Toast.makeText(MainActivity.this, "End", Toast.LENGTH_LONG).show();
                    getData(numb);
                }
            }
        });
    }


    public void getData(int i) {
        viewModel.loadData(i, page);
        viewModel.setOnStartLoadingListener(new MovieViewModel.onStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
            }
        });
        viewModel.setOnFinishLoadingLisneter(new MovieViewModel.onFinishLoadingLisneter() {
            @Override
            public void onFinishLoading(List<Movie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    if (page == 1) {
                        adapter.clearAdapter();
                        viewModel.deleteAllMovies();
                    }
                    viewModel.insertAllMovies(movies);
                    adapter.setMovies(movies);
                    ++page;
                }
                isLoading = false;
            }
        });
    }


    public void switchBox(int num) {
        page = 1;
        switch (num) {
            case 0:
            case 1:
                getData(num);
                break;
            case 2:
                Intent intent = new Intent(MainActivity.this, FavouriteList.class);
                startActivity(intent);
                break;
            case 3:
                break;
            default:
                getData(0);
                break;
        }
    }
}
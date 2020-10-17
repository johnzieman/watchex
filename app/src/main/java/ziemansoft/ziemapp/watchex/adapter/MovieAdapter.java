package ziemansoft.ziemapp.watchex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ziemansoft.ziemapp.watchex.R;
import ziemansoft.ziemapp.watchex.pojo.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_SIZE = "w185";
    private static final String BIG_SIZE = "w780";
    private List<Movie> movies = new ArrayList<>();
    private OnPosterClickListener onPosterClickListener;
    private OnGetEndListener getEndListener;

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public interface OnGetEndListener {
        void getEndListener();
    }

    public interface OnPosterClickListener {
        void onPosterClick(int i);
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public void setGetEndListener(OnGetEndListener getEndListener) {
        this.getEndListener = getEndListener;
    }

    public void clearAdapter() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.get().load(getImageLink(SMALL_SIZE) + movie.getPosterPath()).into(holder.imageViewSmallPoster);
        if (movies.size() >= 20 && position == movies.size() - 2 && getEndListener != null) {
            getEndListener.getEndListener();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewPosterSmall);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPosterClickListener != null) {
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private String getImageLink(String size) {
        return BASE_URL + size;
    }


}

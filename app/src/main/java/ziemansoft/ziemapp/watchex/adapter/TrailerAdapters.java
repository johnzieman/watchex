package ziemansoft.ziemapp.watchex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ziemansoft.ziemapp.watchex.R;
import ziemansoft.ziemapp.watchex.pojo.MovieTrailer;

public class TrailerAdapters extends RecyclerView.Adapter<TrailerAdapters.TrailerViewHolder>{
    private List<MovieTrailer> trailers;

    public TrailerAdapters(){
        trailers = new ArrayList<>();
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public List<MovieTrailer> getTrailers() {
        return trailers;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        MovieTrailer trailer = trailers.get(position);
        holder.textView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewName);
        }
    }
}
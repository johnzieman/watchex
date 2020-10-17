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
    private ItemTouchListener itemTouchListener;
    private MovieTrailer trailer;

    public TrailerAdapters(){
        trailers = new ArrayList<>();
    }

    public interface ItemTouchListener{
        void itemTouch(String link);
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public ItemTouchListener getItemTouchListener() {
        return itemTouchListener;
    }

    public void setItemTouchListener(ItemTouchListener itemTouchListener) {
        this.itemTouchListener = itemTouchListener;
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
        trailer = trailers.get(position);
        holder.textView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemTouchListener!=null){
                        itemTouchListener.itemTouch(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }
}
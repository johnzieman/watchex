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
import ziemansoft.ziemapp.watchex.pojo.Movie;

public class HorizontalMenu extends RecyclerView.Adapter<HorizontalMenu.viewHolderMenu>{
    private List<String> menus= new ArrayList<>();
    private ItemClickListener itemClickListener;


    public interface ItemClickListener{
        void getClickedItem(int i);
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public viewHolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_horizontal, parent, false);
        return new viewHolderMenu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderMenu holder, int position) {
        String item = menus.get(position);
        holder.textViewItem.setText(item);


    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class viewHolderMenu extends RecyclerView.ViewHolder{
        private TextView textViewItem;
        public viewHolderMenu(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewHorizontal);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null){
                        itemClickListener.getClickedItem(getAdapterPosition());
                    }
                }
            });
        }
    }
}

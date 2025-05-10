package com.example.yp_mobile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.yp_mobile.Dishes;
import com.example.yp_mobile.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class adapterRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DISH = 0;
    private static final int VIEW_TYPE_SELECTED_ITEM = 1;
    public List<Dishes> dishList;
    private Dishes selectedDish;

    public adapterRecyclerView(List<Dishes> dishList) {
        this.dishList = dishList;
    }

    @Override
    public int getItemViewType(int position) {
        if (selectedDish != null) {
            return VIEW_TYPE_SELECTED_ITEM;
        } else {
            return VIEW_TYPE_DISH;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DISH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new DishViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item, parent, false);
            return new SelectedItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DishViewHolder) {
            DishViewHolder dishViewHolder = (DishViewHolder) holder;
            Dishes dish = dishList.get(position);
            String dishName = dish.getNameDish();
            if (dishName.length() > 20) {
                dishName = dishName.substring(0, 20) + "...";
            }
            dishViewHolder.nameDish.setText(dishName);
            dishViewHolder.price.setText(dish.getPrice() + ".00₽");
            Glide.with(dishViewHolder.itemView.getContext())
                    .load(dish.getIcon())
                    .into(dishViewHolder.icon);
        } else if (holder instanceof SelectedItemViewHolder) {
            SelectedItemViewHolder selectedItemViewHolder = (SelectedItemViewHolder) holder;
            selectedItemViewHolder.nameDishOneItem.setText(selectedDish.getNameDish());
            selectedItemViewHolder.priceOneItem.setText(selectedDish.getPrice() + ".00₽");
            Glide.with(selectedItemViewHolder.itemView.getContext())
                    .load(selectedDish.getIcon())
                    .into(selectedItemViewHolder.iconOneItem);
        }
    }

    @Override
    public int getItemCount() {
        if (selectedDish != null) {
            return 1;
        } else {
            return dishList.size();
        }
    }

    public void setSelectedDish(Dishes dish) {
        this.selectedDish = dish;
        notifyDataSetChanged();
    }

    public void clearSelectedDish() {
        this.selectedDish = null;
        notifyDataSetChanged();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView nameDish;
        TextView price;
        ShapeableImageView icon;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDish = itemView.findViewById(R.id.nameDish);
            price = itemView.findViewById(R.id.price);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    public static class SelectedItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameDishOneItem;
        TextView priceOneItem;
        ShapeableImageView iconOneItem;

        public SelectedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDishOneItem = itemView.findViewById(R.id.nameDishOneItem);
            priceOneItem = itemView.findViewById(R.id.priceOneItem);
            iconOneItem = itemView.findViewById(R.id.iconOneItem);
            mainscreen._mainscreen.category.setVisibility(View.GONE);
        }
    }
}
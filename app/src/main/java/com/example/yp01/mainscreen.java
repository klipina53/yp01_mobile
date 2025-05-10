package com.example.yp01;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.InitializationListener;
import com.yandex.mobile.ads.common.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mainscreen extends AppCompatActivity {

    private BannerAdView adView;
    LinearLayout address;
    LinearLayout search;
    LinearLayout category;
    EditText addressEditText;
    EditText searchEditText;
    ImageView imageSearch;

    TextView result;
    private RecyclerView recyclerView;
    private adapterRecyclerView dishAdapter;
    private List<Dishes> dishList = new ArrayList<>();
    private List<String> categoryInDB = new ArrayList<>();
    private String selectedCategory = "Выбрать";
    String addressString;
    String addressStringContainer;
    private TextView countItem;
    private int itemCount = 1;
    LinearLayout addCountItem;
    LinearLayout twoButtons;
    private boolean isInitialized = false;
    Intent intentOneItemScreen;
    Timer timer;
    public static mainscreen _mainscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        address = findViewById(R.id.address);
        search = findViewById(R.id.search);
        addressEditText = findViewById(R.id.addressEditText);
        searchEditText = findViewById(R.id.searchEditText);
        result = findViewById(R.id.result);
        imageSearch = findViewById(R.id.imageSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dishAdapter = new adapterRecyclerView(dishList);
        recyclerView.setAdapter(dishAdapter);

        _mainscreen = this;

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!isInitialized) {
                    Dishes selectedDish = dishList.get(position);
                    dishAdapter.setSelectedDish(selectedDish);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mainscreen.this));
                    countItem = view.findViewById(R.id.countItem);
                    addCountItem = view.findViewById(R.id.addCountItem);
                    twoButtons = view.findViewById(R.id.twoButtons);
                    isInitialized = true;

                    intentOneItemScreen = new Intent(mainscreen.this, oneItemScreen.class);
                    intentOneItemScreen.putExtra("NameDish", selectedDish.getNameDish());
                    intentOneItemScreen.putExtra("Price", selectedDish.getPrice());
                    intentOneItemScreen.putExtra("Icon", selectedDish.getIcon());
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                countItem = view.findViewById(R.id.countItem);
                addCountItem = view.findViewById(R.id.addCountItem);
                twoButtons = view.findViewById(R.id.twoButtons);
            }
        }));

        category = findViewById(R.id.categoryElements);
        category.removeAllViews();

        MobileAds.initialize(this, new InitializationListener() {
            @Override
            public void onInitializationCompleted() {
                Log.d("MobileAds", "Initialization completed");
            }
        });

        adView = findViewById(R.id.adView);
        adView.setAdUnitId("demo-appopenad-yandex");
        adView.setAdSize(BannerAdSize.fixedSize(this, 365, 160));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    adView.setVisibility(View.GONE);
                }
                else{
                    startTimer();
                }
            }
        });

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Category>> categoryCall = apiService.getCategorys();
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    for (Category category : categories) {
                        categoryInDB.add(category.getId() + ":" + category.getName());
                    }
                    addCategoryElements();
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                return;
            }
        });

        Call<List<Dishes>> dishCall = apiService.getDishes();
        dishCall.enqueue(new Callback<List<Dishes>>() {
            @Override
            public void onResponse(Call<List<Dishes>> call, Response<List<Dishes>> response) {
                if (response.isSuccessful()) {
                    dishList.addAll(response.body());
                    filterDishes(selectedCategory);
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Dishes>> call, Throwable t) {
                return;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDishesBySearch(s.toString());
                if (s.toString().isEmpty()) {
                    category.setVisibility(View.VISIBLE);
                    result.setVisibility(View.GONE);
                    category.removeAllViews();
                    categoryInDB.remove(0);
                    addCategoryElements();
                    selectCategoryItem((LinearLayout) category.getChildAt(0));
                } else {
                    category.setVisibility(View.GONE);
                    result.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) addressEditText.setText(addressStringContainer);
                else return;
            }
        });
    }

    private void startTimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.VISIBLE);
            }
        }, 5000);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(timer != null) timer.cancel();
    }

    private void filterDishesBySearch(String query) {
        List<Dishes> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(dishList);
        } else {
            for (Dishes dish : dishList) {
                if (dish.getNameDish().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(dish);
                }
            }
        }
        dishAdapter.dishList = filteredList;
        dishAdapter.notifyDataSetChanged();
    }

    private void addCategoryElements() {
        categoryInDB.add(0, "Выбрать");
        for (String categorys : categoryInDB) {
            LinearLayout categoryItem = createCategoryItem(categorys);
            category.addView(categoryItem);
        }
    }

    private LinearLayout createCategoryItem(String categorys) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout categoryItem = (LinearLayout) inflater.inflate(R.layout.category_item, category, false);

        TextView categoryText = categoryItem.findViewById(R.id.category_text);
        LinearLayout categoryLine = categoryItem.findViewById(R.id.category_line);

        String categoryName;
        final int categoryId;

        if (categorys.equals("Выбрать")) {
            categoryName = categorys;
            categoryId = -1;
        } else {
            String[] parts = categorys.split(":");
            categoryId = Integer.parseInt(parts[0]);
            categoryName = parts[1];
        }

        categoryText.setText(categoryName);
        categoryText.setTextColor(Color.BLACK);
        categoryLine.setBackgroundColor(Color.TRANSPARENT);

        if (categoryName.equals(selectedCategory)) {
            selectCategoryItem(categoryItem);
        }

        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = categoryName;
                filterDishes(categoryId == -1 ? "Выбрать" : categoryId + ":" + categoryName);
                selectCategoryItem(categoryItem);
            }
        });

        return categoryItem;
    }

    private void selectCategoryItem(LinearLayout categoryItem) {
        for (int i = 0; i < category.getChildCount(); i++) {
            LinearLayout item = (LinearLayout) category.getChildAt(i);
            TextView textView = item.findViewById(R.id.category_text);
            LinearLayout line = item.findViewById(R.id.category_line);

            textView.setTextColor(Color.BLACK);
            line.setBackgroundColor(Color.TRANSPARENT);
        }

        TextView selectedTextView = categoryItem.findViewById(R.id.category_text);
        LinearLayout selectedLine = categoryItem.findViewById(R.id.category_line);

        selectedTextView.setTextColor(Color.parseColor("#FA4A0C"));
        selectedLine.setBackgroundColor(Color.parseColor("#FA4A0C"));
    }

    private void filterDishes(String category) {
        List<Dishes> filteredList = new ArrayList<>();
        int categoryId = -1;

        if (!category.equals("Выбрать")) {
            String[] parts = category.split(":");
            categoryId = Integer.parseInt(parts[0]);
        }

        if (categoryId == -1) {
            filteredList.addAll(dishList);
        } else {
            for (Dishes dish : dishList) {
                if (dish.getCategory() == categoryId) {
                    filteredList.add(dish);
                }
            }
        }
        dishAdapter.dishList = filteredList;
        dishAdapter.notifyDataSetChanged();
    }

    public void openSearch(View view){
        address.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
    }

    public void openAddress(View view){
        search.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
        address.setVisibility(View.VISIBLE);
        category.setVisibility(View.VISIBLE);
    }

    public void AlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainscreen.this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addressCheck(View view){
        AlertDialog("Уведомление", "Адрес указан!");
    }

    public void onBackAllItems(View view) {
        itemCount = 1;
        countItem.setText(String.valueOf(itemCount));
        dishAdapter.clearSelectedDish();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        isInitialized = false;
        category.setVisibility(View.VISIBLE);
    }

    public void onPlusCount(View view) {
        if (itemCount < 10) {
            itemCount++;
            countItem.setText(String.valueOf(itemCount));
        }
    }

    public void onMinusCount(View view) {
        if (itemCount > 1) {
            itemCount--;
            countItem.setText(String.valueOf(itemCount));
        }
    }

    public void onAddToCart(View view){
        addCountItem.setVisibility(View.GONE);
        twoButtons.setVisibility(View.VISIBLE);
    }

    public void onBackAddCountItem(View view){
        twoButtons.setVisibility(View.GONE);
        addCountItem.setVisibility(View.VISIBLE);
    }

    public void onOneItemScreen(View view){
        startActivity(intentOneItemScreen);
    }
}
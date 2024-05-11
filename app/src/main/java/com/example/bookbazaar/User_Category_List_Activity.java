package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bookbazaar.Interface.SelectListener;
import com.example.bookbazaar.Model.CategoryText;
import com.example.bookbazaar.ViewHolder.CategoryTextAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class User_Category_List_Activity extends AppCompatActivity implements SelectListener {
    // For top toolbar
    Toolbar toolbar;

    // For bottom toolbar
    BottomNavigationView bottomNavigationView;

    // RecyclerView for displaying category list
    RecyclerView categoryListRecyclerView;
    List<CategoryText> categoryTextList;
    CategoryTextAdapter categoryTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category_list);

        // Initialize top toolbar
        toolbar = findViewById(R.id.user_top_category_toolbar);
        // Initialize bottom navigation bar
        bottomNavigationView = findViewById(R.id.user_bottom_category_bar);

        // Bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int btmItemId = item.getItemId();

                // Handling different navigation item selections
                if (btmItemId == R.id.user_cat_bottom_nav_home) {
                    Intent intent = new Intent(User_Category_List_Activity.this, User_Home_Content_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_cat_bottom_nav_categories) {
                    Toast.makeText(User_Category_List_Activity.this, "All Categories List", Toast.LENGTH_SHORT).show();
                } else if (btmItemId == R.id.user_cat_bottom_nav_cart) {
                    Intent intent = new Intent(User_Category_List_Activity.this, User_Cart_Activity.class);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_cat_bottom_nav_search) {
                    Toast.makeText(User_Category_List_Activity.this, "Search Here", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(User_Category_List_Activity.this, User_Search_Activity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        // Display category items
        displayItems();

    }////end on create method

    private void displayItems() {
        // Initialize RecyclerView and its layout manager
        categoryListRecyclerView = findViewById(R.id.category_item_recycler_view);
        categoryListRecyclerView.setHasFixedSize(true);
        categoryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryTextList = new ArrayList<>();

        // Add category items to the list
        categoryTextList.add(new CategoryText("Arts"));
        categoryTextList.add(new CategoryText("Astronomy"));
        categoryTextList.add(new CategoryText("Biography"));
        categoryTextList.add(new CategoryText("Biology"));
        categoryTextList.add(new CategoryText("Chemistry"));
        categoryTextList.add(new CategoryText("Comics and Cartoon"));
        categoryTextList.add(new CategoryText("Computer and Technology"));
        categoryTextList.add(new CategoryText("Dictionary"));
        categoryTextList.add(new CategoryText("Drama"));
        categoryTextList.add(new CategoryText("Drawing"));
        categoryTextList.add(new CategoryText("Engineering"));
        categoryTextList.add(new CategoryText("General Knowledge"));
        categoryTextList.add(new CategoryText("Geography"));
        categoryTextList.add(new CategoryText("History"));
        categoryTextList.add(new CategoryText("Horror Special"));
        categoryTextList.add(new CategoryText("Kids and Funny"));
        categoryTextList.add(new CategoryText("Mathematics"));
        categoryTextList.add(new CategoryText("Medical"));
        categoryTextList.add(new CategoryText("Motivation and Self-help"));
        categoryTextList.add(new CategoryText("Physics"));
        categoryTextList.add(new CategoryText("Religion and Spirituality"));
        categoryTextList.add(new CategoryText("Romantic"));
        categoryTextList.add(new CategoryText("Story"));
        categoryTextList.add(new CategoryText("Others"));
        // Add more categories...

        // Initialize adapter and set it to RecyclerView
        categoryTextAdapter = new CategoryTextAdapter(this, categoryTextList, this);
        categoryListRecyclerView.setAdapter(categoryTextAdapter);
    }

    // Method to handle item click on RecyclerView
    @Override
    public void onItemClicked(CategoryText categoryText) {
        Toast.makeText(this, categoryText.getCategory(), Toast.LENGTH_SHORT).show();

        // Navigate to category items activity
        Intent intent = new Intent(User_Category_List_Activity.this, UserCategoryItemsActivity.class);
        intent.putExtra("categoryName", categoryText.getCategory());
        startActivity(intent);
    }
}///// public class end
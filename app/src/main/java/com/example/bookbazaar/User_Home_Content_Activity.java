package com.example.bookbazaar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bdtopcoder.smart_slider.SliderAdapter;
import com.bdtopcoder.smart_slider.SliderItem;
//import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.example.bookbazaar.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
//import com.google.firebase.database.FirebaseRecyclerAdapter;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class User_Home_Content_Activity extends AppCompatActivity {
    // Declaration of variables

    // For drawer navigation action bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    // For bottom navigation bar
    private BottomNavigationView userBottomNavigationView;

    // Database reference
    private DatabaseReference ProductsRef;

    // For RecyclerView
    private RecyclerView recyclerView, newRecyclerView;

    // For layout managers
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager newLayoutManager;

    // Home to PDF button
    private Button pdfButton;

    // For admin maintain products
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_content);

        // Getting intent extras for admin check
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }


        // Initializing views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // for bottom nav bar
        userBottomNavigationView = findViewById(R.id.user_bottom_nav_bar);

        //for home to pdf button
        pdfButton = findViewById(R.id.home_to_pdf_btn);

        // Setting up Firebase reference
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Setting up RecyclerViews
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(false);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        // for horizontal recycler view
        newRecyclerView = findViewById(R.id.new_recycler_menu);
        newRecyclerView.setHasFixedSize(false);
        newLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true);
        newLayoutManager.setReverseLayout(true);
        newLayoutManager.setStackFromEnd(true);
        newRecyclerView.setLayoutManager(newLayoutManager);


        // Setting up toolbar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //toolbar title
        //toolbar.setTitle("Book Bazaar");

        // Setting up click listener for PDF button
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")) {
                    // Navigate to PDF activity for non-admin users
                    Toast.makeText(User_Home_Content_Activity.this, "All Books PDF", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(User_Home_Content_Activity.this, User_Pdf_Activity.class);
                    startActivity(intent);
                } else {
                    // Show message for admin
                    Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Setting up navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_home) {

                    Toast.makeText(User_Home_Content_Activity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_categories) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "All Categories", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Category_List_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_pdf) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "All Books PDF", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Pdf_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_user_account) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "Account Settings", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Setting_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_user_wishlist) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "Your Wishlist", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Wishlist_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_user_cart) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "Your Cart", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Cart_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.nav_user_order) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "Your Order List", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Order_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_user_sell) {
                    if (!type.equals("Admin")) {

                        Toast.makeText(User_Home_Content_Activity.this, "Become a Seller", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Sell_Book_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_user_termcon) {

                    Toast.makeText(User_Home_Content_Activity.this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(User_Home_Content_Activity.this, User_Term_Condition_Activity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_user_custocare) {

                    Toast.makeText(User_Home_Content_Activity.this, "Customer Service", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(User_Home_Content_Activity.this, User_Customer_Care_Activity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_user_share) {

                    Toast.makeText(User_Home_Content_Activity.this, "This feature will available soon", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(User_Home_Content_Activity.this, User_Share_Activity.class);
                    //startActivity(intent);
                } else if (id == R.id.nav_user_logout) {
                    // if login user, not admin
                    if (!type.equals("Admin")) {

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes", "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Home_Content_Activity.this);
                        builder.setTitle("Are you sure you want to Logout ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Paper.book().destroy();
                                    Toast.makeText(User_Home_Content_Activity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(User_Home_Content_Activity.this, Login_Signin_Page.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    //finish();
                                }
                            }
                        });

                        // for show the dialog box
                        builder.show();
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                }

                // for close drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        // Setting up bottom navigation item click listener
        userBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int btmItemId = item.getItemId();

                if (btmItemId == R.id.user_bottom_nav_home) {
                    Toast.makeText(User_Home_Content_Activity.this, "This is Home", Toast.LENGTH_SHORT).show();
                } else if (btmItemId == R.id.user_bottom_nav_categories) {
                    if (!type.equals("Admin")) {
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Category_List_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (btmItemId == R.id.user_bottom_nav_cart) {
                    if (!type.equals("Admin")) {
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Cart_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                } else if (btmItemId == R.id.user_bottom_nav_search) {
                    if (!type.equals("Admin")) {
                        Toast.makeText(User_Home_Content_Activity.this, "Search Here", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_Home_Content_Activity.this, User_Search_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        // Getting references to views in the navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        TextView userNumberTextView = headerView.findViewById(R.id.user_profile_number);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        // Checking if the user is not an admin
        if (!type.equals("Admin")) {
            // Setting name and number in the navigation header
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            userNumberTextView.setText(Prevalent.currentOnlineUser.getPhone());

            // Setting profile image in the navigation header
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile3).into(profileImageView);
        }


        // Setting up ViewPager2 for the slider
        ViewPager2 viewPager2 = findViewById(R.id.user_home_categories_slider);

        // Creating a list of slider items with images and titles
        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.ban_geo_ten, "Geography"));
        sliderItems.add(new SliderItem(R.drawable.ban_biology, "Biology"));
        sliderItems.add(new SliderItem(R.drawable.ban_chemistry, "Chemistry"));
        sliderItems.add(new SliderItem(R.drawable.ban_motive_nine, "Motivation and Self-help"));
        sliderItems.add(new SliderItem(R.drawable.ban_computer, "Computer and Technology"));
        sliderItems.add(new SliderItem(R.drawable.ban_dictionary, "Dictionary"));
        sliderItems.add(new SliderItem(R.drawable.ban_kids, "General Knowledge"));
        sliderItems.add(new SliderItem(R.drawable.ban_medical_books, "Medical"));
        sliderItems.add(new SliderItem(R.drawable.ban_physics, "Physics"));
        sliderItems.add(new SliderItem(R.drawable.ban_story, "History"));
        sliderItems.add(new SliderItem(R.drawable.ban_engenner, "Engineering"));
        sliderItems.add(new SliderItem(R.drawable.ban_drawing_ten, "Drawing"));
        sliderItems.add(new SliderItem(R.drawable.ban_bio_ten, "Biology"));
        sliderItems.add(new SliderItem(R.drawable.ban_motive_three, "Motivation and Self-help"));
        sliderItems.add(new SliderItem(R.drawable.ban_bio_three, "Biology"));
        sliderItems.add(new SliderItem(R.drawable.ban_che_one, "Chemistry"));
        sliderItems.add(new SliderItem(R.drawable.ban_engin, "Engineering"));
        sliderItems.add(new SliderItem(R.drawable.ban_che_two, "Chemistry"));
        sliderItems.add(new SliderItem(R.drawable.ban_comic, "Comics and Cartoon"));
        sliderItems.add(new SliderItem(R.drawable.ban_doctor_two, "Medical"));
        sliderItems.add(new SliderItem(R.drawable.ban_draw_one, "Drawing"));
        sliderItems.add(new SliderItem(R.drawable.ban_dictionary_ten, "Dictionary"));
        sliderItems.add(new SliderItem(R.drawable.ban_draw_two, "Drawing"));
        sliderItems.add(new SliderItem(R.drawable.ban_geo_one, "Geography"));
        sliderItems.add(new SliderItem(R.drawable.ban_medical_ten, "Medical"));
        sliderItems.add(new SliderItem(R.drawable.ban_geo_three, "Geography"));
        sliderItems.add(new SliderItem(R.drawable.ban_history_ten, "History"));
        sliderItems.add(new SliderItem(R.drawable.ban_his_two, "History"));
        sliderItems.add(new SliderItem(R.drawable.ban_hor_one, "Horror Special"));
        sliderItems.add(new SliderItem(R.drawable.ban_hor_two, "Horror Special"));
        sliderItems.add(new SliderItem(R.drawable.ban_math_one, "Mathematics"));
        sliderItems.add(new SliderItem(R.drawable.ban_phy_one, "Physics"));
        sliderItems.add(new SliderItem(R.drawable.ban_biography, "Biography"));
        sliderItems.add(new SliderItem(R.drawable.ban_phy_two, "Physics"));
        sliderItems.add(new SliderItem(R.drawable.ban_rom_one, "Romantic"));
        // Add more slider items here..

        // Setting up the slider with a delay time
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2, 5000));

        // Handling slider image clicks
        new SliderAdapter((position, title, views) ->
        {
            // Navigate to category items activity based on the clicked image title

            if (title == "Astronomy") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Astronomy", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Astronomy");
                    startActivity(intent1);
                }

            } else if (title == "Arts") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Arts", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Arts");
                    startActivity(intent1);
                }
            } else if (title == "Biography") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Biography", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Biography");
                    startActivity(intent1);
                }
            } else if (title == "Biology") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Biology", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Biology");
                    startActivity(intent1);
                }
            } else if (title == "Chemistry") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Chemistry", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Chemistry");
                    startActivity(intent1);
                }
            } else if (title == "Comics and Cartoon") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Comics and Cartoon", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Comics and Cartoon");
                    startActivity(intent1);
                }
            } else if (title == "Computer and Technology") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Computer and Technology", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Computer and Technology");
                    startActivity(intent1);
                }
            } else if (title == "Dictionary") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Dictionary", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Dictionary");
                    startActivity(intent1);
                }
            } else if (title == "Drama") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Drama", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Drama");
                    startActivity(intent1);
                }
            } else if (title == "Drawing") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Drawing", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Drawing");
                    startActivity(intent1);
                }
            } else if (title == "Engineering") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Engineering", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Engineering");
                    startActivity(intent1);
                }
            } else if (title == "General Knowledge") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "General Knowledge", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "General Knowledge");
                    startActivity(intent1);
                }
            } else if (title == "Geography") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Geography", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Geography");
                    startActivity(intent1);
                }
            } else if (title == "History") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "History");
                    startActivity(intent1);
                }
            } else if (title == "Horror Special") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Horror Special", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Horror Special");
                    startActivity(intent1);
                }
            } else if (title == "Kids and Funny") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Kids and Funny", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Kids and Funny");
                    startActivity(intent1);
                }
            } else if (title == "Mathematics") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Mathematics", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Mathematics");
                    startActivity(intent1);
                }
            } else if (title == "Medical") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Medical", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Medical");
                    startActivity(intent1);
                }
            } else if (title == "Motivation and Self-help") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Motivation and Self-help", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Motivation and Self-help");
                    startActivity(intent1);
                }

            } else if (title == "Physics") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Physics", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Physics");
                    startActivity(intent1);
                }
            } else if (title == "Religion and Spirituality") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Religion and Spirituality", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Religion and Spirituality");
                    startActivity(intent1);
                }
            } else if (title == "Romantic") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Romantic", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Romantic");
                    startActivity(intent1);
                }
            } else if (title == "Story") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Story", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Story");
                    startActivity(intent1);
                }
            } else if (title == "Others") {
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Others", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(User_Home_Content_Activity.this, UserCategoryItemsActivity.class);
                    intent1.putExtra("categoryName", "Others");
                    startActivity(intent1);
                }
            }

        });


    }///////////////end on create/////////////////////


    // for User home product recycle view
    @Override
    protected void onStart() {
        super.onStart();

        // Set up FirebaseRecyclerOptions for the main product RecyclerView
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .build();

        // Set up FirebaseRecyclerAdapter for the main product RecyclerView
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        // Bind data to views in the ViewHolder
                        holder.txtBookName.setText(model.getBookName());
                        holder.txtAuthorName.setText("by " + model.getAuthorName());
                        holder.txtBookPrice.setText("₹ " + model.getPrice());

                        // For get image from firebase
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        // Set up click listener for each item to navigate to product details activity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (type.equals("Admin")) {
                                    // Navigate to admin product maintenance activity
                                    Intent intent = new Intent(User_Home_Content_Activity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);
                                } else {
                                    // Navigate to user product detail activity
                                    Intent intent = new Intent(User_Home_Content_Activity.this, UserProductDetailActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);

                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Inflate layout for each item in the RecyclerView
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        // Create ViewHolder
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        // Set adapter to the main product RecyclerView and start listening for data changes
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        // Set up FirebaseRecyclerOptions for the new horizontal RecyclerView
        FirebaseRecyclerOptions<Products> newOptions =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .build();

        // Add firebase recycler adapter
        FirebaseRecyclerAdapter<Products, ProductViewHolder> newAdapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(newOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        // Bind data to views in the ViewHolder
                        holder.txtBookName.setText(model.getBookName());
                        holder.txtAuthorName.setText("by " + model.getAuthorName());
                        holder.txtBookPrice.setText("₹ " + model.getPrice());

                        // for get image from firebase
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        // Set up click listener for each item to navigate to product details activity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (type.equals("Admin")) {
                                    // Navigate to admin product maintenance activity
                                    Intent intent = new Intent(User_Home_Content_Activity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);
                                } else {
                                    // Navigate to user product detail activity
                                    Intent intent = new Intent(User_Home_Content_Activity.this, UserProductDetailActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Inflate layout for each item in the RecyclerView
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        // Create ViewHolder
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        // Set adapter to the new horizontal RecyclerView and start listening for data changes
        newRecyclerView.setAdapter(newAdapter);
        newAdapter.startListening();
    }

    // on back pressed
    @Override
    public void onBackPressed() {
        // Check if the navigation drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If open, close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If drawer is not open, perform default back button behavior
            super.onBackPressed();
            finish();
        }
    }

    // User home option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu layout
        new MenuInflater(this).inflate(R.menu.user_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // For option menu item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Get the ID of the selected option item
        int optionItemId = item.getItemId();

        // Handle different option menu items
        if (optionItemId == R.id.user_option_search) {
            // If "Search" option is selected
            if (!type.equals("Admin")) {
                // If user is not an admin, start User_Search_Activity
                Toast.makeText(User_Home_Content_Activity.this, "Search Books", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(User_Home_Content_Activity.this, User_Search_Activity.class);
                startActivity(intent);
            } else {
                // If user is an admin, show a message indicating it's not available
                Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
            }
        }
        // Handle other option menu items similarly
        else if (optionItemId == R.id.user_option_favourite) {
            // If "Wishlist" option is selected
            if (!type.equals("Admin")) {
                Toast.makeText(User_Home_Content_Activity.this, "Your Wishlist", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(User_Home_Content_Activity.this, User_Wishlist_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
            }
        }
        else if (optionItemId == R.id.user_option_notification) {
            // If "Notification" option is selected
            if (!type.equals("Admin")) {
                Toast.makeText(User_Home_Content_Activity.this, "You have no Notification", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(User_Home_Content_Activity.this, "This is not available for Admin", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}/////// end public class //////////


package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CustomerSideProductListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private DatabaseReference mProducts;

    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;

    int storeId;

    TextView txtFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_side_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Grocery");
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference("products");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(CustomerSideProductListActivity.this,CartActivity.class);
                startActivity(cartIntent);
            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getUsername());

        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent() != null){
            storeId = this.getIntent().getIntExtra("storeId", 0);
            loadProduct(storeId);
        }

    }

    private void loadProduct(int StoreId) {
        // Check if the product exists in firebase database by the product id
        //Query query = mProducts.orderByChild("productId").equalTo(curProductId);

        Query query = FirebaseDatabase.getInstance().getReference().child("products").orderByChild("storeId").equalTo(StoreId);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options){

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_recycler_row, parent, false);

                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, Product p) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference mStorageRef = storage.getReference();

                holder.productIdTxt.setText(String.valueOf(p.getProductId()));
                holder.txtProductName.setText(p.getProductName());
                holder.txtProductDescription.setText(p.getProductBrand());
                holder.txtProductPrice.setText(String.valueOf(p.getPrice()));

                // Show Picture that retrieved from Firebase Storage using Glide
                StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(p.getProductImageId()));
                Glide.with(getApplicationContext()).load(imagesStorageRef).into(holder.imgProduct);


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent CustomerSideProductDetailsActivity = new Intent(CustomerSideProductListActivity.this, CustomerSideProductDetailsActivity.class);

                        Bundle extras = new Bundle();
                        extras.putInt("productId", p.getProductId());
                        extras.putInt("storeId", p.getStoreId());
                        CustomerSideProductDetailsActivity.putExtras(extras);
                        startActivity(CustomerSideProductDetailsActivity);
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Intent orderStatusIntent = new Intent(CustomerSideProductListActivity.this,OrderStatusActivity.class);
            startActivity(orderStatusIntent);
        } else if (id == R.id.nav_log_out) {
            Intent singout = new Intent(CustomerSideProductListActivity.this,MainActivity.class);
            Toast.makeText(CustomerSideProductListActivity.this,"Sign Out",Toast.LENGTH_SHORT).show();
            startActivity(singout);
        } else if (id == R.id.nav_chat) {
            Intent chatIntent = new Intent(CustomerSideProductListActivity.this,ChatActivity.class);
            startActivity(chatIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
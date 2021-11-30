package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CustomerSideProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    EditText productIdTxt;

    private DatabaseReference mDatabase;
    private DatabaseReference mProducts;

    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_side_product_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProducts = mDatabase.child("products");

        RecyclerView recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent() != null){
            int curProductId = Integer.parseInt(productIdTxt.getText().toString());
            if(!productIdTxt.getText().toString().matches("")){
                loadProduct(curProductId);
            }
        }

    }

    private void loadProduct(int curProductId) {
        // Check if the product exists in firebase database by the product id
        Query query = mProducts.orderByChild("productId").equalTo(curProductId);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options){


            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_recycler_row, parent, false);

                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProductViewHolder holder, int position, Product p) {
                holder.productIdTxt.setText(String.valueOf(Objects.requireNonNull(p).getProductId()));
                holder.txtProductName.setText(p.getProductName());
                holder.txtProductDescription.setText(p.getProductBrand());
                holder.txtProductPrice.setText(String.valueOf(p.getPrice()));
                Picasso.get().load(p.getProductImageId())
                        .into(holder.imgProduct);
                final Product clickItem = p;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent productDetails = new Intent(CustomerSideProductListActivity.this, CustomerSideProductDetailsActivity.class);
                        productDetails.putExtra("productId",adapter.getRef(position).getKey());
                        startActivity(productDetails);
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
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
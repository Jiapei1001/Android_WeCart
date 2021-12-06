package edu.neu.firebase.wecart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * This is an InventoryAdapter class for setting inventory data to items of the inventory RecyclerView.
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    // Create a variable for ArrayList anc context
    private ArrayList<Product> productList;
    private final ProductCardClickListener productCardClickListener;
    private Context context;

    public InventoryAdapter(Context context, ArrayList<Product> productList, ProductCardClickListener productCardClickListener) {
        this.context = context;
        this.productList = productList;
        this.productCardClickListener = productCardClickListener;
    }

    // This is a method for filtering the inventory RecylerView items
    public void filterList(ArrayList<Product> filterList) {
        // Add the filterList in productList
        productList = filterList;
        // Notify the adapter as change in recyclerView DATA
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inventory_product_card,
                parent, false);
        return new ViewHolder(view, productCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.ViewHolder holder, int position) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference();
        // Set data to our views of recyclerView
        Product product = productList.get(position);
        holder.productNameTextView.setText(product.getProductName());
        String productUnit = product.getProductUnit();
        // There are more than 1 lb or 1 pc products
        int quantity = product.getQuantity();
        if (quantity > 1) {
            productUnit = productUnit + "s";
        }
        holder.productPriceAndQuantityTextView.setText(MessageFormat.format("${0} x {1} {2}",
                product.getPrice(), product.getQuantity(), productUnit));

        // Show Picture that retrieved from Firebase Storage using Glide
        StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(product.getProductImageId()));
        Glide.with(context.getApplicationContext()).load(imagesStorageRef).into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        // return the size of productList
        return productList == null ? 0 : productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // create variables for our view
        private final TextView productNameTextView, productPriceAndQuantityTextView;
        private final ImageView productImageView;
        private final Button editProductButton;

        public ViewHolder(@NonNull View itemView, final ProductCardClickListener listener) {
            super(itemView);
            // Initialize our views with their ids
            this.productNameTextView = itemView.findViewById(R.id.textViewProductName);
            this.productPriceAndQuantityTextView = itemView.findViewById(R.id.textViewProductPriceAndQuantity);
            this.productImageView = itemView.findViewById(R.id.imgViewProduct);
            this.editProductButton = itemView.findViewById(R.id.btnEditProduct);

            editProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        // Returns the position of the ViewHolder in terms of the latest layout pass
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditProductClick(position);
                        }
                    }
                }
            });
        }
    }
}

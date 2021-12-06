package edu.neu.firebase.wecart.market;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import edu.neu.firebase.wecart.GlideApp;
import edu.neu.firebase.wecart.R;

public class StoreActivity extends AppCompatActivity {

    int currStoreId;
    StorageReference storageRef;

    DatabaseReference stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        currStoreId = Integer.parseInt(getIntent().getStringExtra("storeId"));

        storageRef = FirebaseStorage.getInstance().getReference();

        stores = FirebaseDatabase.getInstance().getReference().child("stores");

        Query query = stores.orderByChild("storeId").equalTo(currStoreId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Store currStore = ds.getValue(Store.class);

                    // store btn
                    ImageView storeBtn = findViewById(R.id.storeDetailBtn);
                    StorageReference storeBtnOnline = storageRef.child(String.valueOf(currStore.getStoreBtn()));
                    GlideApp.with(getApplicationContext()).load(storeBtnOnline).into(storeBtn);

                    // name
                    TextView storeName = findViewById(R.id.storeDetailName);
                    storeName.setText(currStore.getStoreName());

                    // store image
                    ImageView storeImageView = findViewById(R.id.storeImage);
                    StorageReference storeImageOnline = storageRef.child(String.valueOf(currStore.getStoreImage()));
                    Glide.with(getApplicationContext()).load(storeImageOnline).into(storeImageView);

                    // description
                    TextView storeDesc = findViewById(R.id.storeDesc);
                    storeDesc.setText(currStore.getStoreDes());

                    // owner image
                    CircularImageView ownerImageView = findViewById(R.id.storeOwnerImage);
                    StorageReference ownerImageOnline = storageRef.child(String.valueOf(currStore.getOwnerImage()));
                    Glide.with(getApplicationContext()).load(ownerImageOnline).into(ownerImageView);

                    // owner name
                    TextView meetOwner = findViewById(R.id.storeOwner);
                    meetOwner.setText("meet:    " + currStore.getOwnerName());

                    // address
                    TextView storeAddress = findViewById(R.id.storeAddress);
                    storeAddress.setText(currStore.getAddress());

                    // phone
                    TextView storePhone = findViewById(R.id.storePhone);
                    storePhone.setText(currStore.getPhone());

                    // email
                    TextView storeEmail = findViewById(R.id.storeEmail);
                    storeEmail.setText(currStore.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
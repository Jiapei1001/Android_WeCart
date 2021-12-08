package edu.neu.firebase.wecart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.Objects;

import edu.neu.firebase.wecart.Common;
import edu.neu.firebase.wecart.GlideApp;
import edu.neu.firebase.wecart.R;
import edu.neu.firebase.wecart.User;
import edu.neu.firebase.wecart.market.Store;

public class SellerProfileFragment extends Fragment {

    User curLoginUser;

    int currStoreId;
    StorageReference storageRef;

    DatabaseReference mStores;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        // Get the current login user
        curLoginUser = Common.currentUser;

        // get current store Id
        currStoreId = curLoginUser.getStoreId();

        // Todo: Check if there is no store for the seller

        storageRef = FirebaseStorage.getInstance().getReference();

        mStores = FirebaseDatabase.getInstance().getReference().child("stores");

        Query query = mStores.orderByChild("storeId").equalTo(currStoreId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Store currStore = ds.getValue(Store.class);

                    // store btn
                    ImageView storeBtn = view.findViewById(R.id.storeDetailBtn);
                    StorageReference storeBtnOnline = storageRef.child(String.valueOf(currStore.getStoreBtn()));
                    GlideApp.with(requireActivity()).load(storeBtnOnline).into(storeBtn);

                    // name
                    TextView storeName = view.findViewById(R.id.storeDetailName);
                    storeName.setText(currStore.getStoreName());

                    // store image
                    ImageView storeImageView = view.findViewById(R.id.storeImage);
                    StorageReference storeImageOnline = storageRef.child(String.valueOf(currStore.getStoreImage()));
                    Glide.with(requireActivity()).load(storeImageOnline).into(storeImageView);

                    // description
                    TextView storeDesc = view.findViewById(R.id.storeDesc);
                    storeDesc.setText(currStore.getStoreDes());

                    // owner image
                    CircularImageView ownerImageView = view.findViewById(R.id.storeOwnerImage);
                    StorageReference ownerImageOnline = storageRef.child(String.valueOf(currStore.getOwnerImage()));
                    Glide.with(requireActivity()).load(ownerImageOnline).into(ownerImageView);

                    // owner name
                    TextView meetOwner = view.findViewById(R.id.storeOwner);
                    meetOwner.setText("meet:    " + currStore.getOwnerName());

                    // address
                    TextView storeAddress = view.findViewById(R.id.storeAddress);
                    storeAddress.setText(currStore.getAddress());

                    // phone
                    TextView storePhone = view.findViewById(R.id.storePhone);
                    storePhone.setText(currStore.getPhone());

                    // email
                    TextView storeEmail = view.findViewById(R.id.storeEmail);
                    storeEmail.setText(currStore.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}

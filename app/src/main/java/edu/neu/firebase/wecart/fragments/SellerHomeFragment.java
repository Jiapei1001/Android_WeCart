package edu.neu.firebase.wecart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import edu.neu.firebase.wecart.AddingProductActivity;
import edu.neu.firebase.wecart.Common;
import edu.neu.firebase.wecart.CreatingSellerStoreActivity;
import edu.neu.firebase.wecart.InventoryActivity;
import edu.neu.firebase.wecart.R;
import edu.neu.firebase.wecart.User;
import edu.neu.firebase.wecart.Utils;

public class SellerHomeFragment extends Fragment implements View.OnClickListener {

    private Animation myAnim;
    public SellerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        // Greeting sentence
        TextView greetingForCurrentUser = view.findViewById(R.id.currentUserGreetingTextView);

        // Get the greeting sentence according to the current time
        String greeting = Utils.showGreetingWordsByCurrentTime();
        StringBuilder greetingInfo = new StringBuilder();

        greetingInfo.append(greeting).append(", ").append(Common.currentUser.getUsername()).append("! ");

        // If seller is not the new one
        if (Common.currentUser.getStoreId() != 0) {
            greetingInfo.append("Welcome to your store- ").append(Common.currentUser.getStoreName());
        }

        greetingForCurrentUser.setText(greetingInfo);

        // Get the information concerning the store for the seller
        // If the seller is new, please provide the hint to create a new space.
        // Since id is defined as primitive int, it will be default initialized with 0 and it will never be null.
        TextView sellerInstruction = view.findViewById(R.id.sellerInstructionTextView);
        if (Common.currentUser.getStoreId() == 0) {
            sellerInstruction.setText(R.string.new_seller_instruction);
        } else {
            sellerInstruction.setText(R.string.oldSellerInstruction);
        }

        myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.milkshake);

        // Implement OnClickListener() for buttons
        Button creatingStoreButton = view.findViewById(R.id.btnCreateStore);
        creatingStoreButton.setOnClickListener(this);
        creatingStoreButton.setAnimation(myAnim);
        Button addingProductButton = view.findViewById(R.id.btnAddProduct);
        addingProductButton.setOnClickListener(this);
        addingProductButton.setAnimation(myAnim);
        Button inventoryButton = view.findViewById(R.id.btnInventory);
        inventoryButton.setOnClickListener(this);
        inventoryButton.setAnimation(myAnim);
        Button orderButton = view.findViewById(R.id.btnOrderCenter);
        orderButton.setOnClickListener(this);
        orderButton.setAnimation(myAnim);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCreateStore) {
            if (Common.currentUser.getStoreId() == 0) {
                // launch activity
                Intent intentCreatingStore = new Intent(getActivity(), CreatingSellerStoreActivity.class);
                requireActivity().startActivity(intentCreatingStore);
            } else {
                Toast.makeText(getActivity(), "One login seller cannot create more than one stores", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btnAddProduct) {
            // If the seller does not have a store
            if (Common.currentUser.getStoreId() == 0) {
                Toast.makeText(getActivity(), "Please create a new store :)", Toast.LENGTH_SHORT).show();
            } else {
                Intent intentAddingProduct = new Intent(getActivity(), AddingProductActivity.class);
                intentAddingProduct.putExtra("STORENAME", Common.currentUser.getStoreName());
                intentAddingProduct.putExtra("STOREID", Common.currentUser.getStoreId());
                requireActivity().startActivity(intentAddingProduct);
            }
        } else if (id == R.id.btnInventory) {
            if (Common.currentUser.getStoreId() == 0) {
                Toast.makeText(getActivity(), "Please create a new store :)", Toast.LENGTH_SHORT).show();
            } else {
                Intent intentInventory = new Intent(getActivity(), InventoryActivity.class);
                intentInventory.putExtra("STORENAME", Common.currentUser.getStoreName());
                intentInventory.putExtra("STOREID", Common.currentUser.getStoreId());
                requireActivity().startActivity(intentInventory);
            }
        } else if (id == R.id.btnOrderCenter) {
            if (Common.currentUser.getStoreId() == 0) {
                Toast.makeText(getActivity(), "Please create a new store :)", Toast.LENGTH_SHORT).show();
            } else {
                // Fragment Transactions: Ref - https://developer.android.com/guide/fragments/transactions?authuser=1
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, SellerOrdersFragment.class, null); //My second Fragment
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        v.startAnimation(myAnim);

    }
}
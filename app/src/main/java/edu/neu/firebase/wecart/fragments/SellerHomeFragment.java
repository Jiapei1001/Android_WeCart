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

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.firebase.wecart.AddingProductActivity;
import edu.neu.firebase.wecart.Common;
import edu.neu.firebase.wecart.InventoryActivity;
import edu.neu.firebase.wecart.R;
import edu.neu.firebase.wecart.User;
import edu.neu.firebase.wecart.Utils;

public class SellerHomeFragment extends Fragment implements View.OnClickListener {

    private Animation myAnim;
    private User curLoginUser;

    public SellerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        // Get the login user
        curLoginUser =  Common.currentUser;

        // Greeting sentence
        TextView greetingForCurrentUser = view.findViewById(R.id.currentUserGreetingTextView);

        // Get the greeting sentence according to the current time
        String greeting = Utils.showGreetingWordsByCurrentTime();
        StringBuilder greetingInfo = new StringBuilder();
        greetingInfo.append(greeting).append(", ").append(curLoginUser.getUsername());

        greetingForCurrentUser.setText(greetingInfo);

        // Get the information concerning the store for the seller
        // If the seller is new, please provide the hint to create a new space.
        // Since id is defined as primitive int, it will be default initialized with 0 and it will never be null.
        TextView sellerInstruction = view.findViewById(R.id.sellerInstructionTextView);
        if (curLoginUser.getStoreId() == 0) {
            sellerInstruction.setText(R.string.new_seller_instruction);
        } else {
            sellerInstruction.setText(R.string.oldSellerInstruction);
        }

        myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.milkshake);

        // Implement OnClickListener() for buttons
        Button addingProductButton = view.findViewById(R.id.btnAddProduct);
        addingProductButton.setOnClickListener(this);
        addingProductButton.setAnimation(myAnim);
        Button importingProductsButton = view.findViewById(R.id.btnImportProducts);
        importingProductsButton.setOnClickListener(this);
        importingProductsButton.setAnimation(myAnim);
        Button inventoryButton = view.findViewById(R.id.btnInventory);
        inventoryButton.setOnClickListener(this);
        inventoryButton.setAnimation(myAnim);
        Button orderButton = view.findViewById(R.id.btnOrderCenter);
        orderButton.setOnClickListener(this);
        orderButton.setAnimation(myAnim);
        Button revenueButton = view.findViewById(R.id.btnRevenue);
        revenueButton.setOnClickListener(this);
        revenueButton.setAnimation(myAnim);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRevenue) {
            // launch activity
//            Intent intentClicky = new Intent(this, ClickyClickyActivity.class);
//            startActivity(intentClicky);
        } else if (id == R.id.btnAddProduct) {
            Intent intentAddingProduct = new Intent(getActivity(), AddingProductActivity.class);
            getActivity().startActivity(intentAddingProduct);
        } else if (id == R.id.btnImportProducts) {
//            Intent intentLocator = new Intent(this, LocatorActivity.class);
//            startActivity(intentLocator);
        } else if (id == R.id.btnInventory) {
            Intent intentInventory = new Intent(getActivity(), InventoryActivity.class);
            getActivity().startActivity(intentInventory);
        } else if (id == R.id.btnOrderCenter) {
//            Intent intentInventory = new Intent(this, InventoryActivity.class);
//            startActivity(intentInventory);
        }
        v.startAnimation(myAnim);

    }
}
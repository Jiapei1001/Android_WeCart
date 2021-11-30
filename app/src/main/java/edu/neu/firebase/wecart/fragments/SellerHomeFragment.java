package edu.neu.firebase.wecart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import edu.neu.firebase.wecart.AddingProductActivity;
import edu.neu.firebase.wecart.InventoryActivity;
import edu.neu.firebase.wecart.InventoryAdapter;
import edu.neu.firebase.wecart.R;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SellerHomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SellerHomeFragment extends Fragment implements View.OnClickListener {

    private Animation myAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);

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
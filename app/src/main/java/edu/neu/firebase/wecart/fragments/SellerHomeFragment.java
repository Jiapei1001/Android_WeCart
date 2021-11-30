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

public class SellerHomeFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public SellerHomeFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SellerHomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SellerHomeFragment newInstance(String param1, String param2) {
//        SellerHomeFragment fragment = new SellerHomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home, container, false);
    }
}
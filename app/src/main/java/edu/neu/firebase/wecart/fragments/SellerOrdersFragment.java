package edu.neu.firebase.wecart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

import edu.neu.firebase.wecart.AddingProductActivity;
import edu.neu.firebase.wecart.Common;
import edu.neu.firebase.wecart.R;
import edu.neu.firebase.wecart.Request;
import edu.neu.firebase.wecart.SellerCheckOrderDetailActivity;
import edu.neu.firebase.wecart.SellerItemClickListener;
import edu.neu.firebase.wecart.SellerOrdersViewHolder;

public class SellerOrdersFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mRequest;
    private FirebaseUser mCurrUser;

    private RecyclerView sellerOrdersRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter adapter;

    private String storeName;
    private String orderName;

    public SellerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_orders, container, false);

        storeName = Common.currentUser.getStoreName();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRequest = mDatabase.child("request");

        sellerOrdersRecyclerView = view.findViewById(R.id.recyclerViewOrderCenter);
        sellerOrdersRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        sellerOrdersRecyclerView.setLayoutManager(layoutManager);

        loadOrders(storeName);

        return view;
    }

    private void loadOrders(String storeName) {

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("request")
                .orderByChild("storeName")
                .equalTo(storeName);

        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(query, Request.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, SellerOrdersViewHolder>(options){


            @Override
            protected void onBindViewHolder(@NonNull SellerOrdersViewHolder holder, int position, @NonNull Request model) {
                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderStatus.setText(convertCodeToStates(model.getStatus()));
                //final Request clickItem = model;
                holder.setSellerItemClickListener(new SellerItemClickListener() {

                    @Override
                    public void onCheckOrderClick(int position) {
                        Intent intentCheckOrder = new Intent(getActivity(), SellerCheckOrderDetailActivity.class);
                        intentCheckOrder.putExtra("ORDERKEY", adapter.getRef(position).getKey());
                        intentCheckOrder.putExtra("ORDERSTATUS", model.getStatus());
                        startActivity(intentCheckOrder);
                    }

                    @Override
                    public void onDeliverToPickupClick(int postions) {
                        // change order status to "delivered"
                        mRequest.child(Objects.requireNonNull(adapter.getRef(position).getKey())).child("status").setValue("shipped");
                        Toast.makeText(getActivity(), "The order has shipped.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public SellerOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_seller_orders_row, parent, false);

                return new SellerOrdersViewHolder(view);
            };

        };

        sellerOrdersRecyclerView.setAdapter(adapter);
    }

    private String convertCodeToStates(String status) {

        if ("ordered".equals(status)) {
            return "Order Placed";
        }else
            return "Pending Order";
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
package edu.neu.firebase.wecart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.neu.firebase.wecart.Common;
import edu.neu.firebase.wecart.ItemClickListener;
import edu.neu.firebase.wecart.OrderViewHolder;
import edu.neu.firebase.wecart.R;
import edu.neu.firebase.wecart.Request;
import edu.neu.firebase.wecart.SellerOrdersViewHolder;

public class SellerOrdersFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mRequest;
    private FirebaseUser mCurrUser;

    private RecyclerView sellerOrdersRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter adapter;

    public SellerOrdersFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SellerOrdersFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SellerOrdersFragment newInstance(String param1, String param2) {
//        SellerOrdersFragment fragment = new SellerOrdersFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_orders, container, false);

        // TODO： mCurrentUser
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        // TODO：getStoreName or Id
        String storeName = "Super QQ Fruit Store";

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
                holder.setItemClickListener(new ItemClickListener() {
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
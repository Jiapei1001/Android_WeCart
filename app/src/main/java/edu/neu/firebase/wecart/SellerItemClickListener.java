package edu.neu.firebase.wecart;

import android.view.View;

public interface SellerItemClickListener {
    void onCheckOrderClick(int position);
    void onDeliverToPickupClick(int position);
    void onClick(View view, int position, boolean isLongClick);
}

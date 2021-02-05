package com.sh.homestaymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.domain.Booking;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.util.List;

public class DatLichPhongAdapter extends RecyclerView.Adapter<DatLichPhongAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<Booking> arrRooms;

    public DatLichPhongAdapter(Context mContext,
                               List<Booking> arrRooms) {
        this.mContext = mContext;
        this.arrRooms = arrRooms;
    }

    @NonNull
    @Override
    public DatLichPhongAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_khachhang_room, parent, false);
        return new DatLichPhongAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DatLichPhongAdapter.ItemViewHolder holder, int position) {
        Booking item = arrRooms.get(position);
        holder.tvNameCustomer.setText("Khách hàng: " + item.getCustomerEntity().getName());
        holder.tvPhoneCustomer.setText("Liên hệ: " + item.getCustomerEntity().getPhoneNumber());
        holder.tvDateCustomer.setText("Thời gian: từ " + item.getFromDate() + " - đến " + item.getToDate());
        holder.tvPriceCustomer.setText("Chi phí: " + StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        if (arrRooms != null) {
            return arrRooms.size();
        } else {
            return 0;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNameCustomer, tvPhoneCustomer, tvDateCustomer, tvPriceCustomer;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCustomer = itemView.findViewById(R.id.tvNameCustomer);
            tvPhoneCustomer = itemView.findViewById(R.id.tvPhoneCustomer);
            tvDateCustomer = itemView.findViewById(R.id.tvDateCustomer);
            tvPriceCustomer = itemView.findViewById(R.id.tvPriceCustomer);
        }
    }
}
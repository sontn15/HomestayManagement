package com.sh.homestaymanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.domain.RoomDomain;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ItemCartViewHolder> {

    private Context mContext;
    private List<RoomDomain> arrItems;

    public GioHangAdapter(Context mContext, List<RoomDomain> arrItems) {
        this.mContext = mContext;
        this.arrItems = arrItems;
    }

    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_room_cart, parent, false);
        return new ItemCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCartViewHolder holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        RoomDomain item = arrItems.get(position);
        holder.tvItemNameCart.setText(item.getRoomEntity().getName());
        holder.tvPrice.setText(StringFormatUtils.convertToStringMoneyVND(item.getRoomEntity().getPrice()) + "/ngày");
        holder.tvOutPriceCart.setText("Tổng: " + StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
        holder.tvFromToDateItemCart.setText("Từ " + df.format(item.getFromDate()) + " - đến " + df.format(item.getToDate()) + " (" + item.getDay() + " ngày)");
        holder.imvImage.setImageBitmap(BitmapFactory.decodeByteArray(item.getRoomEntity().getImage(), 0, item.getRoomEntity().getImage().length));
    }

    @Override
    public int getItemCount() {
        return arrItems.size();
    }

    public static class ItemCartViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemNameCart, tvOutPriceCart, tvFromToDateItemCart, tvPrice;
        protected ImageView imvImage;

        public ItemCartViewHolder(@NonNull View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImageCart);
            tvItemNameCart = itemView.findViewById(R.id.tvNameItemCart);
            tvOutPriceCart = itemView.findViewById(R.id.tvOutPriceItemCart);
            tvPrice = itemView.findViewById(R.id.tvPriceGocItemCart);
            tvFromToDateItemCart = itemView.findViewById(R.id.tvFromToDateItemCart);
        }
    }
}
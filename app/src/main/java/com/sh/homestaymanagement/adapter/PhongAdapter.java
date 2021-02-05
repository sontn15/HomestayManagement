package com.sh.homestaymanagement.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.entity.RoomEntity;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.util.List;

public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<RoomEntity> arrRooms;
    private onClickSanPhamListener listener;

    public interface onClickSanPhamListener {
        void onClickViewDetail(RoomEntity item);

        void onClickDatPhong(RoomEntity item);
    }

    public PhongAdapter(Context mContext,
                        List<RoomEntity> arrRooms,
                        onClickSanPhamListener listener) {
        this.mContext = mContext;
        this.arrRooms = arrRooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_phong, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        RoomEntity item = arrRooms.get(position);
        holder.tvTen.setText(item.getName());
        holder.tvType.setText(item.getType());
        holder.tvGia.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
        holder.imvImage.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        holder.bind(item, listener);
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
        protected TextView tvTen, tvGia, tvType;
        protected Button btnViewDetail, btnAddCart;
        protected ImageView imvImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvNameItem);
            tvGia = itemView.findViewById(R.id.tvPriceItem);
            tvType = itemView.findViewById(R.id.tvTypeItem);
            btnAddCart = itemView.findViewById(R.id.btnCartItem);
            btnViewDetail = itemView.findViewById(R.id.btnDetailItem);
            imvImage = itemView.findViewById(R.id.imvImageItem);
        }

        public void bind(final RoomEntity itemInCart, final onClickSanPhamListener listener) {
            btnViewDetail.setOnClickListener(v -> {
                listener.onClickViewDetail(itemInCart);
            });

            btnAddCart.setOnClickListener(v -> {
                listener.onClickDatPhong(itemInCart);
            });
        }

    }
}
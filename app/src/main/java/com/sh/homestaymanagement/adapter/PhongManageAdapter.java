package com.sh.homestaymanagement.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.entity.RoomEntity;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.text.ParseException;
import java.util.List;

public class PhongManageAdapter extends RecyclerView.Adapter<PhongManageAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<RoomEntity> arrSanPham;
    private final onClickSanPhamListener listener;

    public interface onClickSanPhamListener {
        void onClickDelete(RoomEntity item);

        void onClickEdit(RoomEntity item);

        void onClickViewBooking(RoomEntity item) throws ParseException;
    }

    public PhongManageAdapter(Context mContext,
                              List<RoomEntity> arrSanPham,
                              onClickSanPhamListener listener) {
        this.mContext = mContext;
        this.arrSanPham = arrSanPham;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhongManageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_phong_manage, parent, false);
        return new PhongManageAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongManageAdapter.ItemViewHolder holder, int position) {
        RoomEntity item = arrSanPham.get(position);
        holder.tvTen.setText(item.getName());
        holder.tvType.setText(item.getType());
        holder.tvGia.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
        holder.imvImage.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        if (arrSanPham != null) {
            return arrSanPham.size();
        } else {
            return 0;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvTen, tvGia, tvType;
        protected Button btnEdit, btnDelete;
        protected ImageView imvImage;
        protected RelativeLayout rlRoom;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvNameItem);
            tvGia = itemView.findViewById(R.id.tvPriceItem);
            tvType = itemView.findViewById(R.id.tvTypeItem);
            btnEdit = itemView.findViewById(R.id.btnDetailItem);
            btnDelete = itemView.findViewById(R.id.btnXoaItem);
            imvImage = itemView.findViewById(R.id.imvImageItem);
            rlRoom = itemView.findViewById(R.id.rlPhongManage);
        }

        public void bind(final RoomEntity itemInCart, final PhongManageAdapter.onClickSanPhamListener listener) {
            btnDelete.setOnClickListener(v -> listener.onClickDelete(itemInCart));

            btnEdit.setOnClickListener(v -> listener.onClickEdit(itemInCart));

            itemView.setOnClickListener(v -> {
                try {
                    listener.onClickViewBooking(itemInCart);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}

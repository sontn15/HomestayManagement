package com.sh.homestaymanagement.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.adapter.GioHangAdapter;
import com.sh.homestaymanagement.database.HomestayDatabase;
import com.sh.homestaymanagement.database.MySharedPreferences;
import com.sh.homestaymanagement.domain.RoomDomain;
import com.sh.homestaymanagement.entity.BookingEntity;
import com.sh.homestaymanagement.entity.CustomerEntity;
import com.sh.homestaymanagement.utils.Const;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class HoaDonPhongActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtNameCustomer, edtPhoneCustomer;
    private TextView tvTotalPrice;
    private RecyclerView rcvItems;
    private List<RoomDomain> lstItemCart;
    private GioHangAdapter itemInCartAdapter;

    private Long totalPrice = 0L;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_phong);
        getSupportActionBar().setTitle("Xác nhận đặt phòng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();
        initData();
        initAdapter();
        calculatorCurrentTotalPrice();
    }

    private void initView() {
        edtNameCustomer = this.findViewById(R.id.edtNameCustomer);
        edtPhoneCustomer = this.findViewById(R.id.edtPhoneCustomer);
        rcvItems = findViewById(R.id.rcvItemsCart);
        Button btnOrder = findViewById(R.id.btnOrderCart);
        tvTotalPrice = findViewById(R.id.tvTotalPriceCart);

        LinearLayoutManager layoutManager = new LinearLayoutManager(HoaDonPhongActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvItems.setItemAnimator(new DefaultItemAnimator());
        rcvItems.setLayoutManager(layoutManager);

        btnOrder.setOnClickListener(this);
    }

    private void initData() {
        lstItemCart = new ArrayList<>();
        preferences = new MySharedPreferences(HoaDonPhongActivity.this);
        if (preferences.getListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            lstItemCart.addAll(preferences.getListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART));
        }
    }

    private void initAdapter() {
        itemInCartAdapter = new GioHangAdapter(HoaDonPhongActivity.this, lstItemCart);
        rcvItems.setAdapter(itemInCartAdapter);
    }

    private void calculatorCurrentTotalPrice() {
        totalPrice = 0L;
        if (lstItemCart != null && !lstItemCart.isEmpty()) {
            for (RoomDomain obj : lstItemCart) {
                totalPrice += obj.getPrice();
            }
        }
        tvTotalPrice.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOrderCart) {
            onClickButtonOrder();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickButtonOrder() {
        if (lstItemCart == null || lstItemCart.isEmpty()) {
            Toast.makeText(HoaDonPhongActivity.this, "Cần chọn phòng trước", Toast.LENGTH_SHORT).show();
            return;
        }
        String nameCustomer = edtNameCustomer.getText().toString();
        String phoneCustomer = edtPhoneCustomer.getText().toString();
        if (nameCustomer.isEmpty() || phoneCustomer.isEmpty()) {
            Toast.makeText(HoaDonPhongActivity.this, "Nhập thông tin khách hàng", Toast.LENGTH_SHORT).show();
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(null);
            customerEntity.setAge(10L);
            customerEntity.setName(nameCustomer);
            customerEntity.setPhoneNumber(phoneCustomer);
            long idCustomer = HomestayDatabase.getInstance(HoaDonPhongActivity.this).homestayDAO().insertCustomer(customerEntity);

            lstItemCart.forEach(roomDomain -> {
                BookingEntity bookingEntity = new BookingEntity();
                bookingEntity.setId(null);
                bookingEntity.setFromDate(roomDomain.getFromDate());
                bookingEntity.setToDate(roomDomain.getToDate());
                bookingEntity.setRoomId(roomDomain.getRoomEntity().getId());
                bookingEntity.setCustomerId(idCustomer);
                bookingEntity.setPrice(roomDomain.getPrice());
                bookingEntity.setDescription("Thành công");

                HomestayDatabase.getInstance(HoaDonPhongActivity.this).homestayDAO().insertBooking(bookingEntity);
            });
            preferences.clearDataByKey(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART);
            Toast.makeText(HoaDonPhongActivity.this, "Đặt phòng thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HoaDonPhongActivity.this, DatPhongActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(HoaDonPhongActivity.this, DatPhongActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HoaDonPhongActivity.this, DatPhongActivity.class));
        finish();
        super.onBackPressed();
    }
}

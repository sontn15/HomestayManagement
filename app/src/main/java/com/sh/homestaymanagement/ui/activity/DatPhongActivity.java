package com.sh.homestaymanagement.ui.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.adapter.PhongAdapter;
import com.sh.homestaymanagement.database.HomestayDatabase;
import com.sh.homestaymanagement.database.MySharedPreferences;
import com.sh.homestaymanagement.domain.RoomDomain;
import com.sh.homestaymanagement.entity.BookingEntity;
import com.sh.homestaymanagement.entity.RoomEntity;
import com.sh.homestaymanagement.utils.Const;
import com.sh.homestaymanagement.utils.DateConverter;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

public class DatPhongActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rcvRoom;
    private PhongAdapter phongAdapter;
    private List<RoomEntity> listRoom;
    private TextView tvFromDate, tvToDate;
    private AlertDialog detailRoomDlg;
    private EditText edtNameDlg, edtGiaDlg, edtTypeDlg;
    private ImageView imvDlg;
    private Date fromDateSearch, toDateSearch;
    private String fromDateStr, toDateStr;
    private MySharedPreferences mySharedPreferences;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @SneakyThrows
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_phong);
        getSupportActionBar().setTitle("Danh sách phòng");
        mySharedPreferences = new MySharedPreferences(DatPhongActivity.this);
        initView();
        initialDate();
        initAdapter();
        initDialog();
    }

    private void initView() {
        tvFromDate = this.findViewById(R.id.tvFromDateSearch);
        tvToDate = this.findViewById(R.id.tvToDateSearch);
        Button btnThanhToan = this.findViewById(R.id.btnThanhToan);
        Button btnSearch = this.findViewById(R.id.btnSearch);
        rcvRoom = this.findViewById(R.id.rcvRoomDatPhong);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rcvRoom.setItemAnimator(new DefaultItemAnimator());
        rcvRoom.setLayoutManager(manager);
        btnSearch.setOnClickListener(this);
        btnThanhToan.setOnClickListener(this);

        tvFromDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(DatPhongActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        fromDateStr = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                                + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                        tvFromDate.setText(fromDateStr);
                        try {
                            fromDateSearch = df.parse(fromDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });

        tvToDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(DatPhongActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        toDateStr = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                                + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                        tvToDate.setText(toDateStr);
                        try {
                            toDateSearch = df.parse(toDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void initialDate() throws ParseException {
        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String toDate = df.format(c1.getTime());
        toDateStr = toDate;

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH, -3);
        @SuppressLint("SimpleDateFormat") String fromDate = df.format(c2.getTime());
        fromDateStr = fromDate;

        tvFromDate.setText(fromDateStr);
        tvToDate.setText(toDateStr);

        fromDateSearch = df.parse(fromDateStr);
        toDateSearch = df.parse(toDateStr);
    }

    private void initAdapter() {
        listRoom = new ArrayList<>();
        listRoom.addAll(
                HomestayDatabase.getInstance(DatPhongActivity.this).homestayDAO()
                        .findAllRoomFree(
                                DateConverter.dateToTimestamp(fromDateSearch), DateConverter.dateToTimestamp(toDateSearch)));
        if (listRoom.size() == 0) {
            Toast.makeText(DatPhongActivity.this, "Hết phòng trong thời gian trên", Toast.LENGTH_SHORT).show();
        }
        phongAdapter = new PhongAdapter(DatPhongActivity.this, listRoom, new PhongAdapter.onClickSanPhamListener() {
            @Override
            public void onClickViewDetail(RoomEntity item) {
                edtNameDlg.setTextColor(getResources().getColor(R.color.black));
                edtGiaDlg.setTextColor(getResources().getColor(R.color.black));
                edtTypeDlg.setTextColor(getResources().getColor(R.color.black));
                edtNameDlg.setText(item.getName());
                edtTypeDlg.setText(item.getType());
                edtGiaDlg.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                imvDlg.setImageBitmap(bitmap);
                showDialog();
            }

            @Override
            public void onClickDatPhong(RoomEntity item) {
                List<RoomDomain> lstItemCart = new ArrayList<>();
                if (mySharedPreferences.getListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
                    lstItemCart.addAll(mySharedPreferences.getListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART));
                }
                long diffInMillies = Math.abs(toDateSearch.getTime() - fromDateSearch.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                RoomDomain newItem = new RoomDomain();
                newItem.setRoomEntity(item);
                newItem.setDay(diff);
                newItem.setToDate(DateConverter.dateToTimestamp(toDateSearch));
                newItem.setFromDate(DateConverter.dateToTimestamp(fromDateSearch));
                newItem.setPrice(diff * item.getPrice());
                if (lstItemCart.size() > 0) {
                    RoomDomain itemRemove = null;
                    for (RoomDomain obj : lstItemCart) {
                        if (obj != null) {
                            if (item.getId().equals(obj.getRoomEntity().getId())) {
                                itemRemove = obj;
                            }
                        }
                    }
                    if (itemRemove != null) {
                        lstItemCart.remove(itemRemove);
                    }
                }
                lstItemCart.add(newItem);
                mySharedPreferences.putListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);
            }
        });
        rcvRoom.setAdapter(phongAdapter);
    }

    private void initDialog() {
        detailRoomDlg = new AlertDialog.Builder(DatPhongActivity.this, R.style.CustomAlertDialog).create();
        View mViewDialog = getLayoutInflater().inflate(R.layout.dialog_phong, null);
        edtNameDlg = mViewDialog.findViewById(R.id.edtTenDlg);
        edtGiaDlg = mViewDialog.findViewById(R.id.edtGiaDlg);
        edtTypeDlg = mViewDialog.findViewById(R.id.edtTypeDlg);
        imvDlg = mViewDialog.findViewById(R.id.imvImageDlg);
        TextView tvTitleDlg = mViewDialog.findViewById(R.id.tvTitleDlg);
        LinearLayout llButton = mViewDialog.findViewById(R.id.llButtonDlg);
        llButton.setVisibility(View.GONE);
        edtGiaDlg.setEnabled(false);
        edtNameDlg.setEnabled(false);
        edtTypeDlg.setEnabled(false);
        tvTitleDlg.setText("Chi tiết phòng");
        imvDlg.setEnabled(false);
        detailRoomDlg.setView(mViewDialog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSearch) {
            listRoom.clear();
            listRoom.addAll(HomestayDatabase.getInstance(DatPhongActivity.this).homestayDAO()
                    .findAllRoomFree(
                            DateConverter.dateToTimestamp(fromDateSearch), DateConverter.dateToTimestamp(toDateSearch)));
            phongAdapter.notifyDataSetChanged();
        } else if (view.getId() == R.id.btnThanhToan) {
            if (mySharedPreferences.getListRoomCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
                startActivity(new Intent(DatPhongActivity.this, HoaDonPhongActivity.class));
                finish();
            } else {
                Toast.makeText(DatPhongActivity.this, "Chọn phòng trước", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_quan_ly) {
            startActivity(new Intent(DatPhongActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        if (detailRoomDlg != null && !detailRoomDlg.isShowing()) {
            detailRoomDlg.show();
        }
    }
}

package com.sh.homestaymanagement.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sh.homestaymanagement.R;
import com.sh.homestaymanagement.adapter.DatLichPhongAdapter;
import com.sh.homestaymanagement.adapter.PhongManageAdapter;
import com.sh.homestaymanagement.database.HomestayDatabase;
import com.sh.homestaymanagement.domain.Booking;
import com.sh.homestaymanagement.entity.BookingEntity;
import com.sh.homestaymanagement.entity.CustomerEntity;
import com.sh.homestaymanagement.entity.RoomEntity;
import com.sh.homestaymanagement.utils.DateConverter;
import com.sh.homestaymanagement.utils.StringFormatUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuanLyPhongActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMG = 1900;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private RecyclerView rcvRoomManage;
    private PhongManageAdapter sanPhamAdapter;
    private List<RoomEntity> listSanPham;

    private AlertDialog addSanPhamDialog;
    private EditText edtNameDlg, edtGiaDlg, edtTypeDlg;
    private TextView tvTitleDlg;
    private ImageView imvDlg;
    private Button btnConfirmDlg;

    private List<Booking> listBookingOfRoom;
    private RecyclerView rcvLichDatPhong;
    private DatLichPhongAdapter datLichPhongAdapter;
    private AlertDialog detailRoomDlg;
    private TextView tvNameRoomDP, tvPriceRoomDP, tvStatusRoomDP;

    private RoomEntity sanPhamEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phong);
        getSupportActionBar().setTitle("Quản lý phòng");
        initView();
        initAdapter();
        initDialog();
    }

    private void initView() {
        FloatingActionButton fabRoom = this.findViewById(R.id.fabRoom);
        rcvRoomManage = this.findViewById(R.id.rcvRoomManage);
        GridLayoutManager manager = new GridLayoutManager(QuanLyPhongActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rcvRoomManage.setItemAnimator(new DefaultItemAnimator());
        rcvRoomManage.setLayoutManager(manager);

        fabRoom.setOnClickListener(this);
    }

    private void initAdapter() {
        listSanPham = new ArrayList<>();
        listSanPham.addAll(HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findAllRoom());
        sanPhamAdapter = new PhongManageAdapter(QuanLyPhongActivity.this, listSanPham, new PhongManageAdapter.onClickSanPhamListener() {
            @Override
            public void onClickDelete(RoomEntity item) {
                HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().deleteRoom(item);
                listSanPham.clear();
                listSanPham.addAll(HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findAllRoom());
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickEdit(RoomEntity item) {
                tvTitleDlg.setText("Sửa thông tin phòng");
                edtNameDlg.setText(item.getName());
                edtTypeDlg.setText(item.getType());
                edtGiaDlg.setText(item.getPrice().toString());
                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                imvDlg.setImageBitmap(bitmap);
                imvDlg.setEnabled(false);
                btnConfirmDlg.setText("Cập nhật");
                edtNameDlg.setSelection(item.getName().length());
                sanPhamEntity = item;
                showDialog();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClickViewBooking(RoomEntity item) throws ParseException {
                tvNameRoomDP.setText(item.getName());
                tvPriceRoomDP.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
                List<BookingEntity> allBookingEntity = HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findAllBookingOfRoom(item.getId());
                if (allBookingEntity == null || allBookingEntity.isEmpty()) {
                    tvStatusRoomDP.setText("Trống");
                } else {
                    String currentDate = dateFormat.format(new Date());
                    Long currentTime = DateConverter.dateToTimestamp(dateFormat.parse(currentDate));
                    BookingEntity bookingCurrent = HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findBookingEntityByDate(item.getId(), currentTime);
                    if (bookingCurrent != null) {
                        tvStatusRoomDP.setText("Đầy");
                    } else {
                        tvStatusRoomDP.setText("Trống");
                    }

                    List<CustomerEntity> allCustomer = HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findAllCustomer();
                    Map<Long, CustomerEntity> mapCustomer = allCustomer.stream().collect(Collectors.toMap(CustomerEntity::getId, obj -> obj));
                    listBookingOfRoom.clear();
                    allBookingEntity.forEach(bookingEntity -> {
                        Booking b = new Booking();
                        b.setId(bookingEntity.getId());
                        b.setRoomEntity(item);
                        b.setCustomerEntity(mapCustomer.get(bookingEntity.getCustomerId()));
                        b.setPrice(bookingEntity.getPrice());
                        b.setFromDate(dateFormat.format(DateConverter.fromTimestamp(bookingEntity.getFromDate())));
                        b.setToDate(dateFormat.format(DateConverter.fromTimestamp(bookingEntity.getToDate())));

                        listBookingOfRoom.add(b);
                    });
                    datLichPhongAdapter.notifyDataSetChanged();
                }
                showDialogDetail();
            }
        });
        rcvRoomManage.setAdapter(sanPhamAdapter);
    }


    private void initDialog() {
        //manage san pham dialog
        addSanPhamDialog = new AlertDialog.Builder(QuanLyPhongActivity.this, R.style.CustomAlertDialog).create();
        View mViewDialog = getLayoutInflater().inflate(R.layout.dialog_phong, null);
        edtNameDlg = mViewDialog.findViewById(R.id.edtTenDlg);
        edtGiaDlg = mViewDialog.findViewById(R.id.edtGiaDlg);
        edtTypeDlg = mViewDialog.findViewById(R.id.edtTypeDlg);
        imvDlg = mViewDialog.findViewById(R.id.imvImageDlg);
        tvTitleDlg = mViewDialog.findViewById(R.id.tvTitleDlg);
        Button btnCancelDlg = mViewDialog.findViewById(R.id.btnCancelDlg);
        btnConfirmDlg = mViewDialog.findViewById(R.id.btnConfirmDlg);
        addSanPhamDialog.setView(mViewDialog);
        imvDlg.setOnClickListener(this);
        btnCancelDlg.setOnClickListener(this);
        btnConfirmDlg.setOnClickListener(this);
        btnCancelDlg.setTransformationMethod(null);
        btnConfirmDlg.setTransformationMethod(null);

        //manage thong tin dat phong dialog
        detailRoomDlg = new AlertDialog.Builder(QuanLyPhongActivity.this, R.style.CustomAlertDialog).create();
        View detailDialog = getLayoutInflater().inflate(R.layout.dialog_don_dat_phong, null);
        tvNameRoomDP = detailDialog.findViewById(R.id.tvNameRoomDP);
        tvPriceRoomDP = detailDialog.findViewById(R.id.tvPriceRoomDP);
        tvStatusRoomDP = detailDialog.findViewById(R.id.tvStatusRoomDP);
        rcvLichDatPhong = detailDialog.findViewById(R.id.rcvDatPhongDP);
        detailRoomDlg.setView(detailDialog);

        listBookingOfRoom = new ArrayList<>();
        datLichPhongAdapter = new DatLichPhongAdapter(QuanLyPhongActivity.this, listBookingOfRoom);
        LinearLayoutManager layoutManager = new LinearLayoutManager(QuanLyPhongActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvLichDatPhong.setItemAnimator(new DefaultItemAnimator());
        rcvLichDatPhong.setLayoutManager(layoutManager);
        rcvLichDatPhong.setAdapter(datLichPhongAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabRoom: {
                clearDataDialog();
                showDialog();
                break;
            }
            case R.id.btnCancelDlg: {
                clearDataDialog();
                hiddenDialog();
                break;
            }
            case R.id.imvImageDlg: {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
            case R.id.btnConfirmDlg: {
                String type = edtTypeDlg.getText().toString();
                String giaBan = edtGiaDlg.getText().toString();
                String tenSanPham = edtNameDlg.getText().toString();
                if (StringFormatUtils.isNullOrEmpty(tenSanPham) || StringFormatUtils.isNullOrEmpty(giaBan)
                        || StringFormatUtils.isNullOrEmpty(type)) {
                    Toast.makeText(QuanLyPhongActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    RoomEntity newSanPham = new RoomEntity();
                    newSanPham.setType(type);
                    newSanPham.setName(tenSanPham);
                    newSanPham.setPrice(Long.valueOf(giaBan));
                    newSanPham.setImage(convertImageToByteArray());
                    if (sanPhamEntity != null) {
                        newSanPham.setId(sanPhamEntity.getId());
                        HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().updateRoom(newSanPham);
                    } else {
                        HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().insertRoom(newSanPham);
                    }
                    listSanPham.clear();
                    listSanPham.addAll(HomestayDatabase.getInstance(QuanLyPhongActivity.this).homestayDAO().findAllRoom());
                    sanPhamAdapter.notifyDataSetChanged();
                    hiddenDialog();
                    Toast.makeText(QuanLyPhongActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imvDlg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                Toast.makeText(QuanLyPhongActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(QuanLyPhongActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuanLyPhongActivity.this, DatPhongActivity.class));
        finish();
    }

    private byte[] convertImageToByteArray() {
        Bitmap bitmap = ((BitmapDrawable) imvDlg.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        return baos.toByteArray();
    }

    private void clearDataDialog() {
        sanPhamEntity = null;
        imvDlg.setEnabled(true);
        edtNameDlg.requestFocus();
        edtNameDlg.setText("");
        edtTypeDlg.setText("");
        edtGiaDlg.setText("");
        btnConfirmDlg.setText("Thêm mới");
        imvDlg.setImageDrawable(getResources().getDrawable(R.drawable.camera));
    }

    private void showDialog() {
        if (addSanPhamDialog != null && !addSanPhamDialog.isShowing()) {
            addSanPhamDialog.show();
        }
    }

    private void hiddenDialog() {
        if (addSanPhamDialog != null && addSanPhamDialog.isShowing()) {
            addSanPhamDialog.dismiss();
        }
    }

    private void showDialogDetail() {
        if (detailRoomDlg != null && !detailRoomDlg.isShowing()) {
            detailRoomDlg.show();
        }
    }

    private void hiddenDialogDetail() {
        if (detailRoomDlg != null && detailRoomDlg.isShowing()) {
            detailRoomDlg.dismiss();
        }
    }
}

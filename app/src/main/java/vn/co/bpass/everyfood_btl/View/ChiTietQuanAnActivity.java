package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterBinhLuan;
import vn.co.bpass.everyfood_btl.Controller.ChiTietQuanAnController;
import vn.co.bpass.everyfood_btl.Controller.ThucDonController;
import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.Model.ChiNhanhQuanAnModel;
import vn.co.bpass.everyfood_btl.Model.QuanAnModel;
import vn.co.bpass.everyfood_btl.Model.ThanhVienModel;
import vn.co.bpass.everyfood_btl.Model.TienIchModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/25/17.
 */

public class ChiTietQuanAnActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    TextView txtTenQuanAn, txtDiaChiQuanAn, txtThoiGianHoatDong, txtTrangThaiHoatDong, txtTongSoHinhAnh;
    TextView txtTongSoCheckIn, txtTongSoComment, txtTongSoLuuLai, txtTieuDeToolBar, txtGioiHanGia, txtTenWifi, txtMatKhauWifi, txtNgayDangWifi;
    ImageView imageViewHinhQuanAn;
    LinearLayout linearLayoutListDiaChi;
    QuanAnModel quanAnModel;
    /* Chú ý : mặc dù đã khai báo ToolBar ở trong file layout, nhưng ta vẫn cần phải biến ToolBar trở thành ActionBar ở trong
    Java Code */
    Toolbar toolbar;

    RecyclerView recyclerChiTietBinhLuan;
    AdapterBinhLuan adapterBinhLuan;
    NestedScrollView nestedScrollViewChiTietQuanAn;
    GoogleMap googleMap;
    MapFragment mapFragment;

    LinearLayout linearKhungTienIch;
    LinearLayout linearKhungWifi;

    View viewKhungBanDoNho;

    Button btnBinhLuan;

    ChiTietQuanAnController chiTietQuanAnController;

    VideoView videoViewTrailer;
    ImageView imageViewPlayTrailer;

    ThucDonController thucDonController;
    RecyclerView recyclerThucDon;

    Button btnDatMonOdau;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_chitietquanan);

        this.quanAnModel = getIntent().getParcelableExtra("quanan");

        this.addControls();

        this.hienThiChiTietQuanAn();

        //Log.d("tenquanan",quanAnModel.getTenquanan());
    }

    public void addControls() {
        this.linearLayoutListDiaChi = (LinearLayout) findViewById(R.id.linearLayoutListDiaChi);

        this.txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        this.txtDiaChiQuanAn = (TextView) findViewById(R.id.txtDiaChiQuanAnMap);
        this.txtThoiGianHoatDong = (TextView) findViewById(R.id.txtThoiGianHoatDong);
        this.txtTrangThaiHoatDong = (TextView) findViewById(R.id.txtTrangThaiHoatDong);
        this.txtTongSoHinhAnh = (TextView) findViewById(R.id.txtTongSoHinhAnh);
        this.txtTongSoCheckIn = (TextView) findViewById(R.id.txtTongSoCheckIn);
        this.txtTongSoComment = (TextView) findViewById(R.id.txtTongSoComment);
        this.txtTongSoLuuLai = (TextView) findViewById(R.id.txtTongSoLuuLai);
        this.imageViewHinhQuanAn = (ImageView) findViewById(R.id.imageViewHinhQuanAn);
        this.txtTieuDeToolBar = (TextView) findViewById(R.id.txtTieuDeToolBar);

        this.recyclerChiTietBinhLuan = (RecyclerView) findViewById(R.id.recyclerChiTietBinhLuan);
        this.nestedScrollViewChiTietQuanAn = (NestedScrollView) findViewById(R.id.nestedScrollViewChiTietQuanAn);

        this.mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);
        this.txtGioiHanGia = (TextView) findViewById(R.id.txtGioiHanGia);

        this.linearKhungTienIch = (LinearLayout) findViewById(R.id.linearKhungTienIch);

        this.chiTietQuanAnController = new ChiTietQuanAnController();

        this.linearKhungWifi = (LinearLayout) findViewById(R.id.linearKhungWifi);
        this.txtTenWifi = (TextView) findViewById(R.id.txtTenWifi);
        this.txtMatKhauWifi = (TextView) findViewById(R.id.txtMatKhauWifi);
        this.txtNgayDangWifi = (TextView) findViewById(R.id.txtNgayDangWifi);

        this.viewKhungBanDoNho = findViewById(R.id.viewKhungBanDoNho);
        this.viewKhungBanDoNho.setOnClickListener(ChiTietQuanAnActivity.this);

        this.btnBinhLuan = (Button) findViewById(R.id.btnBinhLuan);
        this.btnBinhLuan.setOnClickListener(ChiTietQuanAnActivity.this);


        this.videoViewTrailer = (VideoView) findViewById(R.id.videoViewTrailer);


        this.imageViewPlayTrailer = (ImageView) findViewById(R.id.imageViewPlayTrailer);

        this.thucDonController = new ThucDonController();

        this.recyclerThucDon = (RecyclerView) findViewById(R.id.recyclerThucDon);

        this.btnDatMonOdau = (Button) findViewById(R.id.btnDatMonOdau);
        if (this.quanAnModel.isGiaohang() == true) {
            this.btnDatMonOdau.setVisibility(View.VISIBLE);
        } else {
            this.btnDatMonOdau.setVisibility(View.GONE);
        }
        this.btnDatMonOdau.setOnClickListener(ChiTietQuanAnActivity.this);

        /* Chú ý : mặc dù đã khai báo ToolBar ở trong file layout, nhưng ta vẫn cần phải biến ToolBar trở thành ActionBar ở trong
    Java Code */
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle(""); // Xóa đi title mặc định của Toolbar
        setSupportActionBar(toolbar);
        /* - Hiển thị nút Back hình mũi tên  ở trên Toolbar - Actionbar */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TrangChuActivity.class));
            }
        });
    }

    // Bắt sự kiện click vào các View nằm trong Toolbar - Actionbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Bắt sự kiện click vào nút Back hình mũi tên ở trên ToolBar - ActionBar
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.viewKhungBanDoNho:
                Intent iDanDuong = new Intent(ChiTietQuanAnActivity.this, DanDuongToiQuanAnActivity.class);
                iDanDuong.putExtra("latitude", this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLatitude());
                iDanDuong.putExtra("longitude", this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLongitude());
                iDanDuong.putExtra("tenquanan", this.quanAnModel.getTenquanan());
                //Log.d("kiemtraGui",this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLatitude() + " - " + this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLongitude());
                startActivity(iDanDuong);
                break;
            case R.id.btnBinhLuan:
                Intent iBinhLuan = new Intent(ChiTietQuanAnActivity.this, BinhLuanActivity.class);
                iBinhLuan.putExtra("tenquanan", this.quanAnModel.getTenquanan());
                iBinhLuan.putExtra("diachi", this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getDiachi());
                iBinhLuan.putExtra("maquanan", this.quanAnModel.getMaquanan());
                startActivity(iBinhLuan);
                break;
            case R.id.btnDatMonOdau:
                Intent iDatMonQuaEmail = new Intent(ChiTietQuanAnActivity.this, DatMonQuaEmailActivity.class);
                startActivity(iDatMonQuaEmail);
                break;
        }
    }

    ChiNhanhQuanAnModel chiNhanhQuanAnModelTam;

    private void hienThiChiTietQuanAn() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); // Chỉ lấy Giờ và Phút

        String gioHienTai = dateFormat.format(calendar.getTime());
        String gioMoCua = this.quanAnModel.getGiomocua();
        String gioDongCua = this.quanAnModel.getGiodongcua();

        try {
            Date dateGioHienTai = dateFormat.parse(gioHienTai);
            Date dateGioMoCua = dateFormat.parse(gioMoCua);
            Date dateGioDongCua = dateFormat.parse(gioDongCua);

            // Giờ mở cửa
            if (dateGioHienTai.after(dateGioMoCua) && dateGioHienTai.before(dateGioDongCua)) {
                this.txtTrangThaiHoatDong.setText(getString(R.string.dangmocua));
                this.txtTrangThaiHoatDong.setTextColor(getResources().getColor(R.color.dangmocua));
            }
            // Giờ đóng cửa
            else {
                this.txtTrangThaiHoatDong.setText(getString(R.string.dadongcua));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Log.d("kiemtra",gioHienTai);


        this.txtTenQuanAn.setText(this.quanAnModel.getTenquanan());

        //this.txtDiaChiQuanAn.setText(this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getDiachi());
        //this.linearLayoutListDiaChi.removeAllViews();
        for (ChiNhanhQuanAnModel chiNhanhQuanAnModel : this.quanAnModel.getListChiNhanhQuanAnModel()) {
            // Add textview 1
            TextView txtDiaChiQuanAn = new TextView(this);
            txtDiaChiQuanAn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            txtDiaChiQuanAn.setText(chiNhanhQuanAnModel.getDiachi());
            //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
            txtDiaChiQuanAn.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            this.linearLayoutListDiaChi.addView(txtDiaChiQuanAn);
        }


        this.txtThoiGianHoatDong.setText(this.quanAnModel.getGiomocua() + " - " + this.quanAnModel.getGiodongcua());
        this.txtTongSoHinhAnh.setText(this.quanAnModel.getHinhanhquanan().size() + "");
        this.txtTongSoComment.setText(this.quanAnModel.getListBinhLuanModel().size() + "");
        this.txtTieuDeToolBar.setText(this.quanAnModel.getTenquanan());
        this.txtThoiGianHoatDong.setText(gioMoCua + " - " + gioDongCua);
        //Log.d("kiemtra",quanAnModel.getListBinhLuanModel().size()+" - " + quanAnModel.getListBinhLuanModel().get(0).getThanhVienModel().getHoten());


        // Lấy ra đối tượng Chi Nhánh Quán Ăn, hiển thị địa chỉ gần nhất, và số km để tới địa chỉ đó
        if (this.quanAnModel.getListChiNhanhQuanAnModel().size() > 0) { // Quán Ăn có nhiều địa chỉ
            // Biến tạm. luôn giữ khoảng cách tới địa chỉ đầu tiên trong mảng các địa chỉ quán ăn của 1 Quán Ăn
            // Biến tạm sau khi kết thúc vòng lặp so sánh, sẽ chứa giá trị khoảng cách nhỏ nhất
            //double khoangCachTam = quanAnModel.getListChiNhanhQuanAnModel().get(0).getKhoangcach();
            this.chiNhanhQuanAnModelTam = this.quanAnModel.getListChiNhanhQuanAnModel().get(0);
            for (ChiNhanhQuanAnModel chiNhanhQuanAnModel : this.quanAnModel.getListChiNhanhQuanAnModel()) {
                if (this.chiNhanhQuanAnModelTam.getKhoangcach() > chiNhanhQuanAnModel.getKhoangcach()) {
                    this.chiNhanhQuanAnModelTam = chiNhanhQuanAnModel;
                }
            }
//            holder.txtDiaChiQuanAnOdau.setText(chiNhanhQuanAnModelTam.getDiachi());
//            holder.txtKhoangCachQuanAnOdau.setText(String.format("%.1f",chiNhanhQuanAnModelTam.getKhoangcach()) + " km");
//            this.txtDiaChiQuanAn.setText(this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getDiachi());
            this.txtDiaChiQuanAn.setText(this.chiNhanhQuanAnModelTam.getDiachi());
        }


        //this.linearKhungTienIch.removeAllViews();
        this.downloadHinhTienIch(this.linearKhungTienIch);

        long giaToiThieu = this.quanAnModel.getGiatoithieu();
        long giaToiDa = this.quanAnModel.getGiatoida();
        if (giaToiThieu != 0 && giaToiDa != 0) {
            NumberFormat numberFormat = new DecimalFormat("###,###");
            String sGiaToiThieu = numberFormat.format(giaToiThieu) + "đ";
            String sGiaToiDa = numberFormat.format(giaToiDa) + "đ";
            this.txtGioiHanGia.setText(sGiaToiThieu + " - " + sGiaToiDa);
        } else {
            this.txtGioiHanGia.setVisibility(View.INVISIBLE);
        }

        StorageReference storageHinhQuanAn = FirebaseStorage.getInstance().getReference().child("hinhanh").child(this.quanAnModel.getHinhanhquanan().get(0));
        long ONE_MEGABYTE = 1024 * 1024;
        storageHinhQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageViewHinhQuanAn.setImageBitmap(bitmap);
            }
        });

        if (this.quanAnModel.getVideogioithieu() != null) {
            this.videoViewTrailer.setVisibility(View.VISIBLE);
            this.imageViewHinhQuanAn.setVisibility(View.GONE);
            this.imageViewPlayTrailer.setVisibility(View.VISIBLE);
            StorageReference storageVideoTrailer = FirebaseStorage.getInstance().getReference().child("video").child(this.quanAnModel.getVideogioithieu());
            storageVideoTrailer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    videoViewTrailer.setVideoURI(uri);
                    videoViewTrailer.seekTo(1); // Tua video 1 mili giây, để tận dụng, không cần ảnh video
                    //videoViewTrailer.start();
                }
            });
            this.imageViewPlayTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoViewTrailer.start();
                    // Gán bảng điều khiển Video ở phía dưới cho player
                    MediaController mediaController = new MediaController(ChiTietQuanAnActivity.this);
                    videoViewTrailer.setMediaController(mediaController);
                    imageViewPlayTrailer.setVisibility(View.GONE);
                }
            });
        } else {
            this.videoViewTrailer.setVisibility(View.GONE);
            this.imageViewHinhQuanAn.setVisibility(View.VISIBLE);
            this.imageViewPlayTrailer.setVisibility(View.GONE);

        }


        // Load danh sách bình luận của Quán Ăn
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChiTietQuanAnActivity.this);
        this.recyclerChiTietBinhLuan.setLayoutManager(layoutManager);

        this.adapterBinhLuan = new AdapterBinhLuan(ChiTietQuanAnActivity.this, R.layout.custom_layout_binhluan_chitiet_quanan, this.quanAnModel.getListBinhLuanModel());
        this.recyclerChiTietBinhLuan.setAdapter(adapterBinhLuan);
        this.adapterBinhLuan.notifyDataSetChanged();
        this.nestedScrollViewChiTietQuanAn.smoothScrollTo(0, 0);

        this.chiTietQuanAnController.hienThiDanhSachWifiQuanAn(this.quanAnModel.getMaquanan(), this.txtTenWifi, this.txtMatKhauWifi, this.txtNgayDangWifi);
        this.linearKhungWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDanhSachWifi = new Intent(ChiTietQuanAnActivity.this, CapNhatDanhSachWifiActivity.class);
                iDanhSachWifi.putExtra("maquanan", quanAnModel.getMaquanan());
                startActivity(iDanhSachWifi);
            }
        });

        this.thucDonController.getDanhSachThucDonQuanAnTheoMaQuanAn(ChiTietQuanAnActivity.this, this.quanAnModel.getMaquanan(), this.recyclerThucDon);
    }


    // onCreate() sẽ chạy trước onStart()
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        final List<BinhLuanModel> listBinhLuanModel = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("kiemtra",dataSnapshot + "");
                //for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                //Log.d("kiemtra",snapshot+"");
//                    BinhLuanModel binhLuanModel = snapshot.getValue(BinhLuanModel.class);
//                    listBinhLuanModel.add(binhLuanModel);
//                    adapterBinhLuan = new AdapterBinhLuan(ChiTietQuanAnActivity.this,R.layout.custom_layout_binhluan_chitiet_quanan,listBinhLuanModel);
//                    recyclerChiTietBinhLuan.setAdapter(adapterBinhLuan);
//                    adapterBinhLuan.notifyDataSetChanged();
//                    nestedScrollViewChiTietQuanAn.smoothScrollTo(0,0);
                //}

                // Lấy danh sách các bình luận của QUÁN ĂN thông qua MÃ QUÁN ĂN
                DataSnapshot dataSnapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
                List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
                for (DataSnapshot valueBinhLuan : dataSnapshotBinhLuan.getChildren()) {
                    BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                    binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                    ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                    binhLuanModel.setThanhVienModel(thanhVienModel);

                    // Lấy danh sách hình ảnh , của 1 bình luận, dựa vào mã bình luận
                    List<String> listHinhAnhBinhLuan = new ArrayList<>();
                    DataSnapshot dataSnapshotHinhAnhBinhLuan = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
                    for (DataSnapshot valueHinhAnhBinhLuan : dataSnapshotHinhAnhBinhLuan.getChildren()) { // 1 Mã bình luận -> có nhiều Hình Ảnh
                        listHinhAnhBinhLuan.add(valueHinhAnhBinhLuan.getValue(String.class));
                    }
                    binhLuanModel.setListHinhAnhBinhLuan(listHinhAnhBinhLuan);
                    listBinhLuanModel.add(binhLuanModel);
                }

                adapterBinhLuan = new AdapterBinhLuan(ChiTietQuanAnActivity.this, R.layout.custom_layout_binhluan_chitiet_quanan, listBinhLuanModel);
                recyclerChiTietBinhLuan.setAdapter(adapterBinhLuan);
                adapterBinhLuan.notifyDataSetChanged();
                nestedScrollViewChiTietQuanAn.smoothScrollTo(0, 0);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
//        double latitude = this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLatitude();
//        double longitude = this.quanAnModel.getListChiNhanhQuanAnModel().get(0).getLongitude();
        double latitude = this.chiNhanhQuanAnModelTam.getLatitude();
        double longitude = this.chiNhanhQuanAnModelTam.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        markerOptions.position(latLng);
        markerOptions.title(this.quanAnModel.getTenquanan());
        this.googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        this.googleMap.moveCamera(cameraUpdate);
    }

    private void downloadHinhTienIch(final LinearLayout linearKhungTienIch) {
        for (String maTienIch : quanAnModel.getTienich()) {
            DatabaseReference nodeTienIchRef = FirebaseDatabase.getInstance().getReference().child("quanlytienichs").child(maTienIch);
            nodeTienIchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TienIchModel tienIchModel = dataSnapshot.getValue(TienIchModel.class);
                    //Log.d("kiemtra",tienIchModel.getTentienich());
                    StorageReference storageHinhQuanAn = FirebaseStorage.getInstance().getReference().child("hinhtienich").child(tienIchModel.getHinhtienich());
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageHinhQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView imageViewTienIch = new ImageView(ChiTietQuanAnActivity.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(64, 64); // width và height của ImageView
                            layoutParams.setMargins(15, 15, 15, 15);
                            imageViewTienIch.setLayoutParams(layoutParams);
                            imageViewTienIch.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageViewTienIch.setPadding(5, 5, 5, 5);
                            imageViewTienIch.setImageBitmap(bitmap);
                            linearKhungTienIch.addView(imageViewTienIch);

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }
}

package vn.co.bpass.everyfood_btl.View;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.co.bpass.everyfood_btl.Model.ChiNhanhQuanAnModel;
import vn.co.bpass.everyfood_btl.Model.MonAnModel;
import vn.co.bpass.everyfood_btl.Model.QuanAnModel;
import vn.co.bpass.everyfood_btl.Model.ThemThucDonModel;
import vn.co.bpass.everyfood_btl.Model.ThucDonModel;
import vn.co.bpass.everyfood_btl.Model.TienIchModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 10/1/17.
 */

public class ThemQuanAnActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button btnGioMoCua, btnGioDongCua;
    Spinner spinnerKhuVuc;

    LinearLayout linearKhungTienIchHorizontal, linearLayoutKhungChiNhanh, linearKhungThemChiNhanh;
    LinearLayout linearKhungThucDon;

    ImageView imageViewTam;

    ImageView imgHinhQuan1, imgHinhQuan2, imgHinhQuan3, imgHinhQuan4, imgHinhQuan5, imgHinhQuan6;
    final int RESULT_IMG1 = 111;
    final int RESULT_IMG2 = 112;
    final int RESULT_IMG3 = 113;
    final int RESULT_IMG4 = 114;
    final int RESULT_IMG5 = 115;
    final int RESULT_IMG6 = 116;
    final int RESULT_IMG_THUCDON = 117;
    final int RESULT_VIDEO = 200;


    VideoView videoView;
    ImageView imgVideo;
    Uri videoSelectedUri; // Lưu lại đường dẫn Video Quán Ăn

    Button btnThemQuanAn;

    RadioGroup rdGroupTrangThai;
    EditText edTenQuanAn, edGiaToiDa, edGiaToiThieu;
    String khuvuc;


    String gioMoCua, gioDongCua;

    List<ThucDonModel> listThucDonModel;
    List<String> listKhuVuc, listThucDon;
    List<String> listMaTienIchSelected;
    List<String> listChiNhanh;
    List<ThemThucDonModel> listThemThucDonModel;
    List<Bitmap> listHinhDaChup;
    List<Uri> listHinhQuanAn;

    String maquanan;

    ArrayAdapter<String> adapterKhuVuc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_them_quanan);

        this.btnGioMoCua = (Button) findViewById(R.id.btnGioMoCua);
        this.btnGioDongCua = (Button) findViewById(R.id.btnGioDongCua);
        this.spinnerKhuVuc = (Spinner) findViewById(R.id.spinnerKhuVuc);
        this.linearKhungTienIchHorizontal = (LinearLayout) findViewById(R.id.linearKhungTienIchHorizontal);
        this.linearLayoutKhungChiNhanh = (LinearLayout) findViewById(R.id.linearLayoutKhungChiNhanh);

        // 2 Linear Layout khung để thực hiện clone view ( Add động các view bằng Java code )
        this.linearKhungThemChiNhanh = (LinearLayout) findViewById(R.id.linearKhungThemChiNhanh);
        this.linearKhungThucDon = (LinearLayout) findViewById(R.id.linearKhungThucDon);

        this.imgHinhQuan1 = (ImageView) findViewById(R.id.imgHinhQuan1);
        this.imgHinhQuan2 = (ImageView) findViewById(R.id.imgHinhQuan2);
        this.imgHinhQuan3 = (ImageView) findViewById(R.id.imgHinhQuan3);
        this.imgHinhQuan4 = (ImageView) findViewById(R.id.imgHinhQuan4);
        this.imgHinhQuan5 = (ImageView) findViewById(R.id.imgHinhQuan5);
        this.imgHinhQuan6 = (ImageView) findViewById(R.id.imgHinhQuan6);

        this.imgVideo = (ImageView) findViewById(R.id.imgVideo);
        this.videoView = (VideoView) findViewById(R.id.videoView);

        this.btnThemQuanAn = (Button) findViewById(R.id.btnThemQuanAn);

        this.edTenQuanAn = (EditText) findViewById(R.id.edTenQuanAn);
        this.edGiaToiThieu = (EditText) findViewById(R.id.edGiaToiThieu);
        this.edGiaToiDa = (EditText) findViewById(R.id.edGiaToiDa);
        this.rdGroupTrangThai = (RadioGroup) findViewById(R.id.rdGroupTrangThai);


        this.listThucDonModel = new ArrayList<>();
        this.listKhuVuc = new ArrayList<>();
        this.listThucDon = new ArrayList<>();
        this.listMaTienIchSelected = new ArrayList<>();
        this.listChiNhanh = new ArrayList<>();
        this.listThemThucDonModel = new ArrayList<>();
        this.listHinhDaChup = new ArrayList<>();
        this.listHinhQuanAn = new ArrayList<>();

        this.adapterKhuVuc = new ArrayAdapter<String>(ThemQuanAnActivity.this, android.R.layout.simple_list_item_1, listKhuVuc);
        this.spinnerKhuVuc.setAdapter(adapterKhuVuc);
        this.adapterKhuVuc.notifyDataSetChanged();


        cloneViewThemChiNhanh();
        cloneThucDon();


        layDanhSachKhuVuc();
        layDanhSachTienIch();


        this.btnGioMoCua.setOnClickListener(ThemQuanAnActivity.this);
        this.btnGioDongCua.setOnClickListener(ThemQuanAnActivity.this);

        this.spinnerKhuVuc.setOnItemSelectedListener(ThemQuanAnActivity.this);

        this.imgHinhQuan1.setOnClickListener(ThemQuanAnActivity.this);
        this.imgHinhQuan2.setOnClickListener(ThemQuanAnActivity.this);
        this.imgHinhQuan3.setOnClickListener(ThemQuanAnActivity.this);
        this.imgHinhQuan4.setOnClickListener(ThemQuanAnActivity.this);
        this.imgHinhQuan5.setOnClickListener(ThemQuanAnActivity.this);
        this.imgHinhQuan6.setOnClickListener(ThemQuanAnActivity.this);

        this.imgVideo.setOnClickListener(ThemQuanAnActivity.this);

        this.btnThemQuanAn.setOnClickListener(ThemQuanAnActivity.this);

    }

    /* - CHÚ Ý :
        + Khi chụp hình, máy ảnh sẽ trả về luôn cho ta dữ liệu ảnh dạng Bitmap
        + Khi chọn ảnh từ Gallery, ta chỉ lấy được đường dẫn hình ở dạng uri, sau đó mới parse sang Bitmap */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_IMG1:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan1.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG2:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan2.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG3:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan3.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG4:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan4.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG5:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan5.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG6:
                if (resultCode == RESULT_OK) {
                    // lấy đường dẫn hình
                    Uri uri = data.getData();
                    if(uri != null){
                        imgHinhQuan6.setImageURI(uri);
                        this.listHinhQuanAn.add(uri);
                    }
                }
                break;
            case RESULT_IMG_THUCDON:
                // Khi chụp ảnh xong, máy sẽ trả về ảnh kiểu Bitmap
                // getExtras() : Lấy được bundle
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if(bitmap != null){
                    this.imageViewTam.setImageBitmap(bitmap);
                    this.listHinhDaChup.add(bitmap);
                }

                break;
            case RESULT_VIDEO:
                if (resultCode == RESULT_OK) {
                    imgVideo.setVisibility(View.GONE);
                    // lấy đường dẫn video
                    Uri uri = data.getData();
                    if(uri != null){
                        videoSelectedUri = uri;
                        videoView.setVideoURI(uri);
                        videoView.start();
                    }
                }
                break;
        }


    }

    private void cloneViewThemChiNhanh() {
        // Thủ thuật clone view
        final View cloneView = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_chinhanh, null);
        ImageButton btnThemChiNhanh = (ImageButton) cloneView.findViewById(R.id.btnThemChiNhanh);
        final ImageButton btnXoaChiNhanh = (ImageButton) cloneView.findViewById(R.id.btnXoaChiNhanh);
        btnThemChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edTenChiNhanh = (EditText) cloneView.findViewById(R.id.edTenChiNhanh);
                String tenChiNhanh = edTenChiNhanh.getText().toString();
                if (tenChiNhanh.trim().length() > 0) {
                    v.setVisibility(View.GONE);
                    btnXoaChiNhanh.setVisibility(View.VISIBLE);
                    btnXoaChiNhanh.setTag(tenChiNhanh);
                    listChiNhanh.add(tenChiNhanh);
                    Log.d("size", listChiNhanh.size() + "");
                    for (String ten : listChiNhanh) {
                        Log.d("ten", ten);
                    }
                    cloneViewThemChiNhanh();
                } else {
                    return;
                }
            }
        });

        btnXoaChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenChiNhanh = v.getTag().toString();
                listChiNhanh.remove(tenChiNhanh);
                Log.d("size", listChiNhanh.size() + "");
                for (String ten : listChiNhanh) {
                    Log.d("ten", ten);
                }
                linearKhungThemChiNhanh.removeView(cloneView);
            }
        });


        linearKhungThemChiNhanh.addView(cloneView);
    }

    private void cloneThucDon() {
        View cloneView = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_thucdon, null);
        final Spinner spinnerThucDon = (Spinner) cloneView.findViewById(R.id.spinnerThucDon);
        Button btnThemThucDon = (Button) cloneView.findViewById(R.id.btnThemThucDon);
        final EditText edTenMon = (EditText) cloneView.findViewById(R.id.edTenMon);
        final EditText edGiaTien = (EditText) cloneView.findViewById(R.id.edGiaTien);
        ImageView imgChupHinh = (ImageView) cloneView.findViewById(R.id.imgChupHinh);
        this.imageViewTam = imgChupHinh;

        ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(ThemQuanAnActivity.this, android.R.layout.simple_list_item_1, listThucDon);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();
        if (listThucDon.size() == 0) {
            this.layDanhSachThucDon(adapterThucDon);
        }

        imgChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_IMG_THUCDON);
            }
        });


        btnThemThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                /* - Để tên của các hình chụp món ăn không trùng nhau, thì ta lấy ngày giờ hiện tại theo milisecond
                  để đặt tên hình */
                long thoigian = Calendar.getInstance().getTimeInMillis();
                String tenhinh = String.valueOf(thoigian) + ".jpg";

                int position = spinnerThucDon.getSelectedItemPosition();
                String mathucdon = listThucDonModel.get(position).getMathucdon();

                MonAnModel monAnModel = new MonAnModel();
                monAnModel.setTenmon(edTenMon.getText().toString());
                monAnModel.setGiatien(Long.parseLong(edGiaTien.getText().toString()));
                monAnModel.setHinhanh(tenhinh);

                ThemThucDonModel themThucDonModel = new ThemThucDonModel();
                themThucDonModel.setMathucdon(mathucdon);
                themThucDonModel.setMonAnModel(monAnModel);

                /* - CHÚ Ý : listThemThucDonModel có cùng chiều dài với listHinhDaChup ở trong hàm onActivityResult()
                   2 mảng này tuy rằng riêng rẽ nhau, nhưng lại có cùng chiều dài, có các dữ liệu nằm ở các index
                   tương ứng với nhau
                   => Lúc up dữ liệu lên Firebase, sẽ phải duyệt riêng rẽ từng mảng, 1 mảng dùng để up dữ liệu lên
                   realtim db, 1 mảng dùng để up hình bitmap lên trên Storage */
                listThemThucDonModel.add(themThucDonModel);

                Log.d("kiemtra", mathucdon);
                cloneThucDon();
            }
        });
        this.linearKhungThucDon.addView(cloneView);
    }

    /* - parent : Chính là Spinner View
       - view : là customview của Adapter dành cho Spinner
       - id : id của giá trị nằm trên Spinner, ko phải id của Spinner View */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerKhuVuc:
                //Log.d("kiemtrakhuvuc", listKhuVuc.get(position) + "");
                khuvuc = listKhuVuc.get(position);
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void layDanhSachThucDon(final ArrayAdapter<String> adapterThucDon) {
        FirebaseDatabase.getInstance().getReference().child("thucdons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("kiemtra",dataSnapshot+"");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThucDonModel thucDonModel = new ThucDonModel();
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);

                    thucDonModel.setMathucdon(key);
                    thucDonModel.setTenthucdon(value);
                    listThucDonModel.add(thucDonModel);
                    listThucDon.add(value);

                }
                adapterThucDon.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void layDanhSachKhuVuc() {
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("kiemtra",dataSnapshot+"");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tenKhuVuc = snapshot.getKey();
                    listKhuVuc.add(tenKhuVuc);
                }
                adapterKhuVuc.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void layDanhSachTienIch() {
        FirebaseDatabase.getInstance().getReference().child("quanlytienichs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String matienich = snapshot.getKey();
//                    Log.d("snap",snapshot+"");
                    TienIchModel tienIchModel = snapshot.getValue(TienIchModel.class);
                    tienIchModel.setMatienich(matienich);
                    //listTienIchModel.add(tienIchModel);
                    CheckBox checkBox = new CheckBox(ThemQuanAnActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    checkBox.setText(tienIchModel.getTentienich() + "");
                    checkBox.setTag(matienich);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // Tham số thứ 1 : chính là đại diện cho từng Check Box
                            String maTienIch = buttonView.getTag().toString();
                            //Log.d("matienich",maTienIch);
                            if (isChecked) { // Nếu CheckBox được người dùng check vào
                                listMaTienIchSelected.add(maTienIch);
                            } else { // Nếu CheckBox không được check
                                listMaTienIchSelected.remove(maTienIch);
                            }
                            Log.d("matienichlistsize", listMaTienIchSelected.size() + "");
                        }
                    });
                    linearKhungTienIchHorizontal.addView(checkBox);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(final View v) {
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        int id = v.getId();
        switch (id) {
            case R.id.btnGioMoCua:
                TimePickerDialog timePickerDialog = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Log.d("mocua", hourOfDay + " - " + minute);
                        gioMoCua = hourOfDay + ":" + minute;
                        ((Button) v).setText(gioMoCua);
                    }
                }, gio, phut, true);
                // Muốn hiển thị 1 Fragment, thì phải gọi hàm show()
                timePickerDialog.show();
                break;

            case R.id.btnGioDongCua:
                TimePickerDialog timePickerDialogDongCua = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Log.d("dongcua", hourOfDay + " - " + minute);
                        gioDongCua = hourOfDay + ":" + minute;
                        ((Button) v).setText(gioDongCua);
                    }
                }, gio, phut, true);
                // Muốn hiển thị 1 Fragment, thì phải gọi hàm show()
                timePickerDialogDongCua.show();
                break;

            case R.id.imgHinhQuan1:
                this.chonHinhTuGallery(RESULT_IMG1);
                break;
            case R.id.imgHinhQuan2:
                this.chonHinhTuGallery(RESULT_IMG2);
                break;
            case R.id.imgHinhQuan3:
                this.chonHinhTuGallery(RESULT_IMG3);
                break;
            case R.id.imgHinhQuan4:
                this.chonHinhTuGallery(RESULT_IMG4);
                break;
            case R.id.imgHinhQuan5:
                this.chonHinhTuGallery(RESULT_IMG5);
                break;
            case R.id.imgHinhQuan6:
                this.chonHinhTuGallery(RESULT_IMG6);
                break;
            case R.id.imgVideo:
                Intent intent = new Intent();
                intent.setType("video/*");
                // Liệt kê ra cho người dùng chọn tất cả các Action, mà có thể mở được tất cả các đuôi file hình ảnh
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Mở ra Popup với title được set
                startActivityForResult(Intent.createChooser(intent, "Chọn video..."), RESULT_VIDEO);
                break;
            case R.id.btnThemQuanAn:
                themQuanAn();
                break;
        }
    }

    private void themQuanAn() {
        String tenQuanAn = this.edTenQuanAn.getText().toString();
        long giatoithieu = Long.parseLong(this.edGiaToiThieu.getText().toString());
        long giatoida = Long.parseLong(this.edGiaToiDa.getText().toString());

        int idRadioGiaoHangSelected = this.rdGroupTrangThai.getCheckedRadioButtonId();
        boolean giaohang = false;
        if (idRadioGiaoHangSelected == R.id.rdGiaoHang) {
            giaohang = true;
        } else {
            giaohang = false;
        }

        // Tên video giới thiệu được phát sinh động dựa vào Firebase
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nodeQuanAn = nodeRoot.child("quanans");
        maquanan = nodeQuanAn.push().getKey(); // Phát sinh động mã quán ăn
        // .push() là để tạo ra Key động, sau đó mới setValue();
        nodeRoot.child("khuvucs").child(khuvuc).push().setValue(maquanan);


        for (String tenchinhanh : listChiNhanh) {
            String urlGeoCoding = "https://maps.googleapis.com/maps/api/geocode/json?address=" + tenchinhanh + "&key=AIzaSyBeLcS6-NAyQvRFTXV3am2iSUsYQoTGRXU";
            DownloadToaDo downloadToaDo = new DownloadToaDo();
            downloadToaDo.execute(urlGeoCoding);
        }

        //Log.d("kiemtra",giaohang+"");
        QuanAnModel quanAnModel = new QuanAnModel();
        quanAnModel.setTenquanan(tenQuanAn);
        quanAnModel.setGiatoithieu(giatoithieu);
        quanAnModel.setGiatoida(giatoida);
        quanAnModel.setGiaohang(giaohang);
        quanAnModel.setGiomocua(gioMoCua);
        quanAnModel.setGiodongcua(gioDongCua);
        if(this.videoSelectedUri != null){
            quanAnModel.setVideogioithieu(videoSelectedUri.getLastPathSegment());
        }
        quanAnModel.setTienich(listMaTienIchSelected);
        quanAnModel.setLuotthich((long) 0);

        nodeQuanAn.child(maquanan).setValue(quanAnModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(ThemQuanAnActivity.this,"Thêm quán ăn thành công",Toast.LENGTH_SHORT).show();
            }
        });

        if(this.videoSelectedUri != null){
            FirebaseStorage.getInstance().getReference().child("video/"+videoSelectedUri.getLastPathSegment()+".mp4").putFile(videoSelectedUri);
        }
        if(listHinhQuanAn.size() > 0){
            for(Uri uriHinhQuanAn:listHinhQuanAn){
                FirebaseStorage.getInstance().getReference().child("hinhanh/"+uriHinhQuanAn.getLastPathSegment()+".jpg").putFile(uriHinhQuanAn);
                nodeRoot.child("hinhanhquanans").child(maquanan).push().setValue(uriHinhQuanAn.getLastPathSegment()+".jpg");
            }
        } else {
            Toast.makeText(ThemQuanAnActivity.this,"Bạn cần chọn ít nhất 1 tấm ảnh cho Quán Ăn",Toast.LENGTH_SHORT).show();
            return;
        }


        /* - listHinhDaChup và listThemThucDonModel có cùng kích thước, hơn nữa mỗi phần tử tại các index
           tương ứng của 2 bên đều có quan hệ một cặp với nhau */
        for(int i=0;i<listThemThucDonModel.size();i++){
            nodeRoot.child("thucdonquanans").child(maquanan).child(listThemThucDonModel.get(i).getMathucdon()).push().setValue(listThemThucDonModel.get(i).getMonAnModel());

            if(listHinhDaChup.size() > 0){
                // Duyệt các hình đã chụp của từng món ăn
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmapAnhMonAnDaChup = listHinhDaChup.get(i);
                bitmapAnhMonAnDaChup.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();
                FirebaseStorage.getInstance().getReference().child("hinhanh/"+listThemThucDonModel.get(i).getMonAnModel().getHinhanh()).putBytes(data);
            }
        }

        finish();
    }

    // doi so 1 : url cua gg Geocoding API, doi so 3 : chuoi JSON server tra ve
    class DownloadToaDo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(params[0]); // Tạo URL
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect(); // Mở kết nối tới URL
                // Khi đã mở kết nối thành công, 1 InputStream sẽ được tạo ra, chứa dữ liệu Server trả về cho ta
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }


        // Dùng để lấy ra kết quả
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("kiemtra",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObjectTong = results.getJSONObject(i);
                    String formatted_address = jsonObjectTong.getString("formatted_address");
                    JSONObject geometry = jsonObjectTong.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = (double) location.get("lat");
                    double longitude = (double) location.get("lng");
                    ChiNhanhQuanAnModel chiNhanhQuanAnModel = new ChiNhanhQuanAnModel();
                    chiNhanhQuanAnModel.setDiachi(formatted_address);
                    chiNhanhQuanAnModel.setLatitude(latitude);
                    chiNhanhQuanAnModel.setLongitude(longitude);
                    //Log.d("kiemtra","lat : " + latitude + " - " + "longitude : " + longitude + " - address : " + formatted_address);

                    /* - Nếu dữ liệu truyền lên Firebase là 1 List, thì Firebase sẽ tự biểu diễn kiểu dữ liệu này thành 1 mảng
                    Key - Value */
                    FirebaseDatabase.getInstance().getReference().child("chinhanhquanans").child(maquanan).push().setValue(chiNhanhQuanAnModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void chonHinhTuGallery(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        // Liệt kê ra cho người dùng chọn tất cả các Action, mà có thể mở được tất cả các đuôi file hình ảnh
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Mở ra Popup với title được set
        startActivityForResult(Intent.createChooser(intent, "Chọn hình..."), requestCode);
    }


}


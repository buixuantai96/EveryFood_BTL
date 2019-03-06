package vn.co.bpass.everyfood_btl.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.co.bpass.everyfood_btl.Adapters.AdapterRecyclerHinhBinhLuan;
import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.R;

import static vn.co.bpass.everyfood_btl.R.id.txtNoiDungBinhLuan2;
import static vn.co.bpass.everyfood_btl.R.id.txtTieuDeBinhLuan2;

/**
 * Created by VietDung-KMA-AT11D on 9/26/17.
 */

public class HienThiChiTietBinhLuanActivity extends AppCompatActivity {
    CircleImageView hinhUser;
    RecyclerView recyclerViewHinhBinhLuan;
    TextView txtTieuDeBinhLuan,txtNoiDungBinhLuan,txtSoDiem;
    List<Bitmap> listBitmapHinhBinhLuan;
    BinhLuanModel binhLuanModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout_binhluan_chitiet_quanan);

        this.hinhUser = (CircleImageView) findViewById(R.id.circleImageUser2);
        this.txtTieuDeBinhLuan = (TextView) findViewById(txtTieuDeBinhLuan2);
        this.txtNoiDungBinhLuan = (TextView) findViewById(txtNoiDungBinhLuan2);
        this.txtSoDiem = (TextView) findViewById(R.id.txtChamDiemBinhLuan2);
        this.recyclerViewHinhBinhLuan = (RecyclerView) findViewById(R.id.recyclerHinhBinhLuan);
        this.listBitmapHinhBinhLuan = new ArrayList<>();

        this.binhLuanModel = getIntent().getParcelableExtra("binhluanmodel");

        txtTieuDeBinhLuan.setText(binhLuanModel.getTieude());
        txtNoiDungBinhLuan.setText(binhLuanModel.getNoidung());
        txtSoDiem.setText(binhLuanModel.getChamdiem()+"");
        this.setHinhAnhBinhLuan(this.hinhUser,binhLuanModel.getThanhVienModel().getHinhanh());

        for(String linkHinhAnh:binhLuanModel.getListHinhAnhBinhLuan()){
            // Download hình ảnh của bình luận, từ Storage
            /* - User nào cũng có hinhanh, bởi vì khi đăng ký ta đã gán một cái hình mặc định là user.png cho mỗi User */
            // Download hình mặc định cho user về
            StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkHinhAnh);
            long ONE_MEGABYTE = 1024 * 1024;
            storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    listBitmapHinhBinhLuan.add(bitmap);
                    if(listBitmapHinhBinhLuan.size() == binhLuanModel.getListHinhAnhBinhLuan().size()){
                        AdapterRecyclerHinhBinhLuan adapterRecyclerHinhBinhLuan = new AdapterRecyclerHinhBinhLuan(HienThiChiTietBinhLuanActivity.this,R.layout.custom_layout_hinhbinhluan,listBitmapHinhBinhLuan,binhLuanModel,true);
                        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(HienThiChiTietBinhLuanActivity.this,2); // Đối số thứ 2 : số cột của GridView
                        recyclerViewHinhBinhLuan.setLayoutManager(gridLayoutManager);
                        recyclerViewHinhBinhLuan.setAdapter(adapterRecyclerHinhBinhLuan);
                        adapterRecyclerHinhBinhLuan.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    // Lấy dữ liệu hình ảnh bình luận của Quán Ăn từ Firebase, set lên giao diện
    private void setHinhAnhBinhLuan(final CircleImageView circleImageView, String linkHinhAnh){
        /* - User nào cũng có hinhanh, bởi vì khi đăng ký ta đã gán một cái hình mặc định là user.png cho mỗi User */
        // Download hình mặc định cho user về
        StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("thanhvien").child(linkHinhAnh);
        long ONE_MEGABYTE = 1024 * 1024;
        storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                circleImageView.setImageBitmap(bitmap);
            }
        });
    }
}

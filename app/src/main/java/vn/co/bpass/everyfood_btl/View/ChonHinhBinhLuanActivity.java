package vn.co.bpass.everyfood_btl.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterChonHinhBinhLuan;
import vn.co.bpass.everyfood_btl.Model.ChonHinhBinhLuanModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

public class ChonHinhBinhLuanActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerChonHinhBinhLuan;
    List<ChonHinhBinhLuanModel> listChonHinhBinhLuanModel;
    List<String> listHinhDuocChon;
    AdapterChonHinhBinhLuan adapterChonHinhBinhLuan;

    TextView txtXong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chonhinh_binhluan);

        this.listChonHinhBinhLuanModel = new ArrayList<>();
        this.listHinhDuocChon = new ArrayList<>();

        this.recyclerChonHinhBinhLuan = (RecyclerView) findViewById(R.id.recyclerChonHinhBinhLuan);
        // RecyclerView hiển thị theo kiểu Grid, với 2 cột
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChonHinhBinhLuanActivity.this, 2);
        this.recyclerChonHinhBinhLuan.setLayoutManager(layoutManager);
        adapterChonHinhBinhLuan = new AdapterChonHinhBinhLuan(ChonHinhBinhLuanActivity.this, R.layout.custom_layout_chonhinh_binhluan, this.listChonHinhBinhLuanModel);
        this.recyclerChonHinhBinhLuan.setAdapter(adapterChonHinhBinhLuan);

        this.txtXong = (TextView) findViewById(R.id.txtXong);
        this.txtXong.setOnClickListener(ChonHinhBinhLuanActivity.this);

        int checkReadExternalStoragePermission = ContextCompat.checkSelfPermission(ChonHinhBinhLuanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        // Nếu chưa được cấp quyền => Xin cấp quyền
        if (checkReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChonHinhBinhLuanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            /* - Đã được cấp quyền rồi => get tất cả hình bên trong thẻ nhớ */
            this.getTatCaHinhAnhTrongTheNho();
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtXong:
                for (ChonHinhBinhLuanModel chonHinhBinhLuanModel : this.listChonHinhBinhLuanModel) {
                    if (chonHinhBinhLuanModel.isChecked() == true) {
                        this.listHinhDuocChon.add(chonHinhBinhLuanModel.getDuongdan());
                        //Log.d("kiemtra",chonHinhBinhLuanModel.getDuongdan()+"");
                    }
                }
                Intent dataAnhBinhLuan = getIntent();
                dataAnhBinhLuan.putStringArrayListExtra("listhinhduocchon", (ArrayList<String>) this.listHinhDuocChon);
                setResult(RESULT_OK,dataAnhBinhLuan);
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // Xin quyền đọc thẻ nhớ
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.getTatCaHinhAnhTrongTheNho();
            }
        }
    }

    /* - Từ các phiên bản Android trên 5.0 trở lên, ngoài xin quyền ở trong file Manifest, thì cần phải xin quyền ở Run Time nữa */
    public void getTatCaHinhAnhTrongTheNho() {
        // Các cột muốn lấy dữ liệu
        String[] projection = {MediaStore.Images.Media.DATA};
        // Lấy ảnh trong bộ nhớ SD
        /* - Đường dẫn này sẽ trả về dữ liệu kiểu cấu trúc SQLite, có thể hiểu là 1 table Images trong SQLite */
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // Đường dẫn tới các Ảnh trong bộ nhớ SD
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Lấy dữ liệu đường dẫn của hình ảnh từ cột MediaStore.Images.Media.DATA
            String duongDan = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            ChonHinhBinhLuanModel chonHinhBinhLuanModel = new ChonHinhBinhLuanModel(duongDan, false);
            //Log.d("kiemtra", duongDan);
            this.listChonHinhBinhLuanModel.add(chonHinhBinhLuanModel);
            this.adapterChonHinhBinhLuan.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }
}

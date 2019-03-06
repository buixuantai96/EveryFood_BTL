package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterHienThiHinhBinhLuanDuocChon;
import vn.co.bpass.everyfood_btl.Controller.BinhLuanController;
import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

public class BinhLuanActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txtTenQuanAn,txtDiaChiQuanAn,txtDangBinhLuan;
    Toolbar toolBarBinhLuan;

    EditText edTieuDeBinhLuan,edNoiDungBinhLuan,edDiemQuanAn;

    ImageButton btnChonHinhComment;

    final int REQUEST_CHONHINH_BINHLUAN = 28;

    RecyclerView recyclerHinhBinhLuanDaChon;
    AdapterHienThiHinhBinhLuanDuocChon adapterHienThiHinhBinhLuanDuocChon;
    String maQuanAn;

    SharedPreferences sharedPreferences;
    BinhLuanController binhLuanController;
    List<String> listHinhDuocChon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_binhluan);

        this.sharedPreferences = getSharedPreferences("luudangnhap",MODE_PRIVATE);


        String tenQuanAn = getIntent().getStringExtra("tenquanan");
        String diaChi = getIntent().getStringExtra("diachi");
        this.maQuanAn = getIntent().getStringExtra("maquanan");



        this.toolBarBinhLuan = (Toolbar) findViewById(R.id.toolBarBinhLuan);
        this.toolBarBinhLuan.setTitle("");
        setSupportActionBar(this.toolBarBinhLuan);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        this.txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        this.txtDiaChiQuanAn = (TextView) findViewById(R.id.txtDiaChiQuanAn);
        this.btnChonHinhComment = (ImageButton) findViewById(R.id.btnChonHinhComment);
        this.txtDangBinhLuan = (TextView) findViewById(R.id.txtDangBinhLuan);
        this.edTieuDeBinhLuan = (EditText) findViewById(R.id.edTieuDeBinhLuan);
        this.edNoiDungBinhLuan = (EditText) findViewById(R.id.edNoiDungBinhLuan);
        this.edDiemQuanAn = (EditText) findViewById(R.id.edDiemQuanAn);

        this.txtTenQuanAn.setText(tenQuanAn);
        this.txtDiaChiQuanAn.setText(diaChi);

        this.recyclerHinhBinhLuanDaChon = (RecyclerView) findViewById(R.id.recyclerHinhBinhLuanDaChon);

        this.btnChonHinhComment.setOnClickListener(BinhLuanActivity.this);
        this.txtDangBinhLuan.setOnClickListener(BinhLuanActivity.this);

        this.listHinhDuocChon = new ArrayList<>();
        this.binhLuanController = new BinhLuanController();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //finish();
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnChonHinhComment:
                Intent iChonHinhBinhLuan = new Intent(BinhLuanActivity.this,ChonHinhBinhLuanActivity.class);
                startActivityForResult(iChonHinhBinhLuan,this.REQUEST_CHONHINH_BINHLUAN);

                break;
            case R.id.txtDangBinhLuan:
                BinhLuanModel binhLuanModel = new BinhLuanModel();
                String tieuDe = this.edTieuDeBinhLuan.getText().toString();
                String noiDung = this.edNoiDungBinhLuan.getText().toString();
                int diemQuanAn = Integer.parseInt(this.edDiemQuanAn.getText().toString());
                String uid = this.sharedPreferences.getString("uid","");
                if(tieuDe.trim().length() > 0 && noiDung.trim().length() >0){
                    //Log.d("uid",uid);
                    binhLuanModel.setTieude(tieuDe);
                    binhLuanModel.setNoidung(noiDung);
                    binhLuanModel.setChamdiem(diemQuanAn);
                    binhLuanModel.setLuotthich(0);
                    binhLuanModel.setMauser(uid);
                    this.binhLuanController.themBinhLuan(binhLuanModel,this.listHinhDuocChon,this.maQuanAn);
                    finish();
                }
                else{
                    Toast.makeText(BinhLuanActivity.this,getString(R.string.bancannhapduthongtin),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHONHINH_BINHLUAN){
            if(resultCode == RESULT_OK){
                this.listHinhDuocChon = data.getStringArrayListExtra("listhinhduocchon");
                this.adapterHienThiHinhBinhLuanDuocChon = new AdapterHienThiHinhBinhLuanDuocChon(BinhLuanActivity.this,R.layout.custom_layout_hienthi_hinhbinhluan_duocchon,listHinhDuocChon);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BinhLuanActivity.this,LinearLayoutManager.HORIZONTAL,false);
                this.recyclerHinhBinhLuanDaChon.setLayoutManager(layoutManager);
                this.recyclerHinhBinhLuanDaChon.setAdapter(this.adapterHienThiHinhBinhLuanDuocChon);
                this.adapterHienThiHinhBinhLuanDuocChon.notifyDataSetChanged();
            }
        }
    }
}

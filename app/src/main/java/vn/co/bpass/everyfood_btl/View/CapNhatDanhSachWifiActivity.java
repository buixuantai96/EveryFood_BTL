package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import vn.co.bpass.everyfood_btl.Controller.CapNhatWifiController;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class CapNhatDanhSachWifiActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerDanhSachWifi;
    Button btnCapNhatWifi;
    CapNhatWifiController capNhatWifiController;
    String maQuanAn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_capnhat_danhsachwifi);

        this.recyclerDanhSachWifi = (RecyclerView) findViewById(R.id.recyclerDanhSachWifi);
        this.btnCapNhatWifi = (Button) findViewById(R.id.btnCapNhatWifi);

        this.maQuanAn = getIntent().getStringExtra("maquanan");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CapNhatDanhSachWifiActivity.this,LinearLayoutManager.VERTICAL,true);
        this.recyclerDanhSachWifi.setLayoutManager(layoutManager);

        this.capNhatWifiController = new CapNhatWifiController(CapNhatDanhSachWifiActivity.this);
        this.capNhatWifiController.hienThiDanhSachWifi(this.maQuanAn,this.recyclerDanhSachWifi);

        this.btnCapNhatWifi.setOnClickListener(CapNhatDanhSachWifiActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent iDialogCapNhatWifi = new Intent(CapNhatDanhSachWifiActivity.this,DialogCapNhatWifiActivity.class);
        iDialogCapNhatWifi.putExtra("maquanan",this.maQuanAn);
        startActivity(iDialogCapNhatWifi);
    }
}

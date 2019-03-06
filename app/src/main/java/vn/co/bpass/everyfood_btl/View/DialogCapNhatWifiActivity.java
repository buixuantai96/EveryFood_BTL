package vn.co.bpass.everyfood_btl.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.co.bpass.everyfood_btl.Controller.CapNhatWifiController;
import vn.co.bpass.everyfood_btl.Model.WifiQuanAnModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class DialogCapNhatWifiActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edTenWifi,edMatKhauWifi;
    Button btnDongYCapNhatWifi;
    CapNhatWifiController capNhatWifiController;
    String maQuanAn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_capnhatwifi);

        this.maQuanAn = getIntent().getStringExtra("maquanan");
        Log.d("maquanan",maQuanAn);

        this.edMatKhauWifi = (EditText) findViewById(R.id.edMatKhauWifi);
        this.edTenWifi = (EditText) findViewById(R.id.edTenWifi);
        this.btnDongYCapNhatWifi = (Button) findViewById(R.id.btnDongYCapNhatWifi);
        this.btnDongYCapNhatWifi.setOnClickListener(DialogCapNhatWifiActivity.this);

        this.capNhatWifiController = new CapNhatWifiController(DialogCapNhatWifiActivity.this);

    }

    @Override
    public void onClick(View v) {
        String tenWifi = this.edTenWifi.getText().toString();
        String matKhauWifi = this.edMatKhauWifi.getText().toString();
        if(tenWifi.trim().length() > 0 && matKhauWifi.trim().length() >0){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String ngayDang = simpleDateFormat.format(calendar.getTime());

            WifiQuanAnModel wifiQuanAnModel = new WifiQuanAnModel();
            wifiQuanAnModel.setTen(this.edTenWifi.getText().toString().trim());
            wifiQuanAnModel.setMatkhau(this.edMatKhauWifi.getText().toString().trim());
            wifiQuanAnModel.setNgaydang(ngayDang);
            this.capNhatWifiController.themWifi(DialogCapNhatWifiActivity.this,wifiQuanAnModel,this.maQuanAn);
        }
        else{
            Toast.makeText(DialogCapNhatWifiActivity.this,getString(R.string.bancannhapduthongtin),Toast.LENGTH_LONG).show();
        }
    }
}

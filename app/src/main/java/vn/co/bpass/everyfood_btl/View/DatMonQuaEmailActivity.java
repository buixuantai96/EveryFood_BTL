package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterMonAn;
import vn.co.bpass.everyfood_btl.Model.DatMon;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 10/5/17.
 */

public class DatMonQuaEmailActivity extends AppCompatActivity {
    EditText editTextTo,editTextSubject,editTextMessage;
    Button butSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datmon_qua_email);

        editTextTo = (EditText) findViewById(R.id.editText1);
        editTextSubject = (EditText) findViewById(R.id.editText2);
        editTextMessage = (EditText) findViewById(R.id.editText3);
        butSend = (Button) findViewById(R.id.but);

        editTextTo.setText("meotolon@gmail.com");




//        for(DatMon datMon: listDatMonTam){
//            Log.d("datmon",datMon.getTenmonan() + " - " + datMon.getSoluong());
//
//        }



        butSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = editTextTo.getText().toString();
                String sub = editTextSubject.getText().toString();
                String mes = editTextMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT,sub);
                email.putExtra(Intent.EXTRA_TEXT,mes);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Choose an email client"));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<DatMon> listDatMonTam = new ArrayList<>();
        String danhSachMonAnMuonDat = "";
        for(DatMon datMon: AdapterMonAn.listDatMon){
            listDatMonTam.add(datMon);
            danhSachMonAnMuonDat += datMon.getTenmonan() + " - " + datMon.getSoluong() + " phần \n";
        }
        AdapterMonAn.listDatMon.clear();

        editTextMessage.setText("Đặt món : \n " + danhSachMonAnMuonDat);
    }
}

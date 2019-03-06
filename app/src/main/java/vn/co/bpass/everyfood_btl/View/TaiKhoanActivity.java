package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 10/6/17.
 */

public class TaiKhoanActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnDangXuat;

    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    String uid = "";
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_taikhoan);

        this.sharedPreferences = getSharedPreferences("luudangnhap",MODE_PRIVATE);
        this.uid = this.sharedPreferences.getString("uid","");
        Log.d("uidtaikhoan",uid);
        this.firebaseAuth = FirebaseAuth.getInstance();

        

        this.btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        this.btnDangXuat.setOnClickListener(TaiKhoanActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDangXuat:
                this.firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent iDangXuat = new Intent(TaiKhoanActivity.this,DangNhapActivity.class);
                startActivity(iDangXuat);
                break;
        }
    }
}

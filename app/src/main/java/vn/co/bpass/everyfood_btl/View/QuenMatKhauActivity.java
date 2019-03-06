package vn.co.bpass.everyfood_btl.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/21/17.
 */

public class QuenMatKhauActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edEmailKP;
    Button btnGuiEmailKP;
    TextView txtDangKyKP;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quenmatkhau);

        this.addControls();
    }

    public void addControls(){
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(QuenMatKhauActivity.this);

        this.edEmailKP = (EditText) findViewById(R.id.edEmailKP);
        this.btnGuiEmailKP = (Button) findViewById(R.id.btnGuiEmailKP);
        this.txtDangKyKP = (TextView) findViewById(R.id.txtDangKyKP);

        this.btnGuiEmailKP.setOnClickListener(QuenMatKhauActivity.this);
        this.txtDangKyKP.setOnClickListener(QuenMatKhauActivity.this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnGuiEmailKP:
                String email = this.edEmailKP.getText().toString();
                boolean kiemTraEmail = this.kiemTraKhuonDangEmail(email);
                // Email đúng khuôn dạng => Gửi một Email giúp reset lại password của tài khoản có email tương ứng
                if(kiemTraEmail == true){
                    this.progressDialog.setMessage(getString(R.string.dangxuly));
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.show();
                    this.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Gửi email giúp Reset Password thành công
                            if(task.isSuccessful()){
                                Toast.makeText(QuenMatKhauActivity.this,getString(R.string.thongbaoguiemailthanhcong),Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(QuenMatKhauActivity.this,getString(R.string.thongbaoemailkhonghople),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                break;
            case R.id.txtDangKyKP:
                Intent iDangKy = new Intent(QuenMatKhauActivity.this,DangKyActivity.class);
                startActivity(iDangKy);
                break;
        }
    }

    private boolean kiemTraKhuonDangEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}

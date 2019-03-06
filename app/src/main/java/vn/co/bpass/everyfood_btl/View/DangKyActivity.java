package vn.co.bpass.everyfood_btl.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.co.bpass.everyfood_btl.Controller.DangKyController;
import vn.co.bpass.everyfood_btl.Model.ThanhVienModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/21/17.
 */

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edEmailDK,edPasswordDK,edNhapLaiPasswordDK;
    Button btnDangKy;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    DangKyController dangKyController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.addControls();
    }

    public void addControls(){
        this.progressDialog = new ProgressDialog(DangKyActivity.this);

        this.edEmailDK = (EditText) findViewById(R.id.edEmailDK);
        this.edPasswordDK = (EditText) findViewById(R.id.edPasswordDK);
        this.edNhapLaiPasswordDK = (EditText) findViewById(R.id.edNhapLaiPasswordDK);

        this.btnDangKy = (Button) findViewById(R.id.btnDangKy);
        this.btnDangKy.setOnClickListener(DangKyActivity.this);

    }

    @Override
    public void onClick(View v) {
        this.progressDialog.setMessage(getString(R.string.dangxuly));
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.show();

        final String emailDK = this.edEmailDK.getText().toString();
        String matKhauDK = this.edPasswordDK.getText().toString();
        String matKhauNhapLaiDK = this.edNhapLaiPasswordDK.getText().toString();

        if(emailDK.trim().length() == 0 ){ // Nếu người dùng không nhập vào email
            Toast.makeText(DangKyActivity.this,getString(R.string.thongbaonhapemail),Toast.LENGTH_SHORT).show();
        }
        else if(matKhauDK.trim().length() == 0){
            Toast.makeText(DangKyActivity.this,getString(R.string.thongbaonhappassword),Toast.LENGTH_SHORT).show();
        }
        else if(matKhauDK.trim().length() < 6){
            Toast.makeText(DangKyActivity.this,getString(R.string.thongbaomatkhau6kytu),Toast.LENGTH_SHORT).show();
        }
        else if(!matKhauNhapLaiDK.equals(matKhauDK)){
            Toast.makeText(DangKyActivity.this,getString(R.string.thongbaonhaplaimatkhau),Toast.LENGTH_SHORT).show();
        }
        else{
            this.firebaseAuth.createUserWithEmailAndPassword(emailDK,matKhauNhapLaiDK).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        // Lấy email, và gán hình mặc định cho người dùng vừa đăng ký thành công
                        ThanhVienModel thanhVienModel = new ThanhVienModel();
                        thanhVienModel.setHoten(emailDK);
                        thanhVienModel.setHinhanh("user.png");
                        // Lấy ra uid của user :
                        String uid = task.getResult().getUser().getUid();
                        dangKyController = new DangKyController();
                        dangKyController.themThongTinThanhVienController(thanhVienModel,uid);
                        Toast.makeText(DangKyActivity.this,getString(R.string.dangkythanhcong),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

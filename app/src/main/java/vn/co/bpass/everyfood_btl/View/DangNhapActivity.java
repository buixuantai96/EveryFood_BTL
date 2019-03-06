package vn.co.bpass.everyfood_btl.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/20/17.
 */

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
FirebaseAuth.AuthStateListener {
    Button btnDangNhap,btnDangNhapGoogle;
    Button btnDangNhapFacebook;
    EditText edEmailDN,edPasswordDN;
    TextView txtDangKyMoi,txtQuenMatKhau;

    FirebaseAuth firebaseAuth;
    GoogleApiClient apiClient;

    LoginManager loginManager; // Dùng để đăng nhập Facebook mà không cần dùng tới LoginButton của Facebook
    List<String> listPermissonFacebook;

    SharedPreferences sharedPreferences;

    public static int REQUEST_CODE_DANGNHAP_GOOGLE = 99;

    // 1 : Đăng nhập bằng Google, 2 : Đăng nhập bằng Facebook
    public static int KIEMTRA_PROVIDER_DANGNHAP = 0;

    // Hàm onSuccess khi đăng nhập Facebook thành công chỉ được chạy, khi mà biến này được gán giá trị trong hàm onActivityResult
    // đối với đăng nhập bằng Facebook
    CallbackManager callbackManagerFacebook;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo Facebook SDK dùng để đăng nhập Firebase bằng Facebook
        FacebookSdk.sdkInitialize(DangNhapActivity.this);
        setContentView(R.layout.layout_dangnhap);

        this.firebaseAuth = FirebaseAuth.getInstance();


        //this.firebaseAuth.signOut();
        //LoginManager.getInstance().logOut();

        this.addControls();
        this.taoClientDangNhapGoogle();
    }

    public void addControls(){
        this.progressDialog = new ProgressDialog(DangNhapActivity.this);

        this.btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        this.btnDangNhapGoogle = (Button) findViewById(R.id.btnDangNhapGoogle);
        this.btnDangNhapFacebook = (Button) findViewById(R.id.btnDangNhapFacebook);
        this.edEmailDN = (EditText) findViewById(R.id.edEmailDN);
        this.edPasswordDN = (EditText) findViewById(R.id.edPasswordDN);

        this.btnDangNhapGoogle.setOnClickListener(DangNhapActivity.this);
        this.btnDangNhapFacebook.setOnClickListener(DangNhapActivity.this);
        this.btnDangNhap.setOnClickListener(DangNhapActivity.this);

        this.txtDangKyMoi = (TextView) findViewById(R.id.txtDangKyMoi);
        this.txtDangKyMoi.setOnClickListener(DangNhapActivity.this);

        this.txtQuenMatKhau = (TextView) findViewById(R.id.txtQuenMatKhau);
        this.txtQuenMatKhau.setOnClickListener(DangNhapActivity.this);

        this.sharedPreferences = getSharedPreferences("luudangnhap",MODE_PRIVATE);


    }

    private void dangNhapEmail(String emailDN,String passwordDN){
        this.progressDialog.setMessage(getString(R.string.dangxuly));
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.show();
        this.firebaseAuth.signInWithEmailAndPassword(emailDN,passwordDN).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(DangNhapActivity.this,getString(R.string.thongbaodangnhapthatbai),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dangNhapFacebook(){
        // btnDangNhapFacebook đã được Facebook SDK hỗ trợ sẵn lắng nghe sự kiện onClick, ta chỉ việc setReadPermission cho button này
        // Lấy thông tin email người dùng, public_profile dùng để lấy ra token_id của người dùng
//        this.btnDangNhapFacebook.setReadPermissions("email","public_profile");
        this.callbackManagerFacebook = CallbackManager.Factory.create();

        this.loginManager = LoginManager.getInstance(); // Dùng để đăng nhập Facebook mà không cần dùng tới loginButton của FB
        this.listPermissonFacebook = Arrays.asList("email","public_profile");
        this.loginManager.logInWithReadPermissions(DangNhapActivity.this,this.listPermissonFacebook);

        /*
          - Chú ý : Hàm onSuccess ( Đăng nhập Facebook thành công ) chỉ được chạy khi và chỉ khi tham số đầu tiên callbackManagerFacebook
          trong hàm registerCallback được gán giá trị.
          - Vì vậy ta đưa biến callbackManagerFacebook trở thành biến toàn cục
          - Và giá trị của biến toàn cục callbackManagerFacebook sẽ được lấy ở trong onActivityResult của Đăng nhập facebook.
          - Tức là, sau khi Intent đăng nhập Facebook mở ra, người dùng nhập username và password Facebook xong rồi ấn đăng nhập,
          nếu đăng nhập thành công, Intent đăng nhập Facebook đóng lại, Kết quả đăng nhập thành công sẽ được trả về hàm
          onActivityResult, và ta lấy được giá trị của biến toàn cục callbackManagerFacebook, để có thể kích hoạt được
          hàm onSuccess() khi đăng nhập Facebook */
        this.loginManager.registerCallback(this.callbackManagerFacebook, new FacebookCallback<LoginResult>() {
            // Đăng nhập Facebook thành công
            @Override
            public void onSuccess(LoginResult loginResult) {
                KIEMTRA_PROVIDER_DANGNHAP = 2;
                String tokenId = loginResult.getAccessToken().getToken();
                chungThucDangNhapFireBase(tokenId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    // Khởi tạo client cho đăng nhập Google
    private void taoClientDangNhapGoogle(){
        /* - Chứa các thông tin mà ứng dụng muốn lấy của người dùng
             + IdToken : dùng cho việc đăng nhập thông qua Credential Authentication */
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        this.apiClient = new GoogleApiClient.Builder(DangNhapActivity.this)
                .enableAutoManage(DangNhapActivity.this,DangNhapActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
    }
    // end Khởi tạo client cho đăng nhập Google

    // Mở form đăng nhập bằng Google
    private void dangNhapGoogle(GoogleApiClient apiClient){
        KIEMTRA_PROVIDER_DANGNHAP = 1; // Đăng nhập bằng Google
        Intent iDangNhapGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iDangNhapGoogle,REQUEST_CODE_DANGNHAP_GOOGLE);
    }
    // end Mở form đăng nhập bằng Google

    private void chungThucDangNhapFireBase(String tokenId){
        if(KIEMTRA_PROVIDER_DANGNHAP == 1){ // Đăng nhập bằng Google
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenId,null);
            this.firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });
        }
        // Đăng nhập bằng Facebook
        else if(KIEMTRA_PROVIDER_DANGNHAP == 2){
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenId);
            this.firebaseAuth.signInWithCredential(authCredential);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DANGNHAP_GOOGLE){ // Nếu đăng nhập bằng Google
            if( resultCode == RESULT_OK) { // Nếu quá trình đăng nhập bằng Google thành công
                /* - Lấy ra các thông tin về kết quả đăng nhập Google thành công */
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                // Lấy ra Account vừa mới đăng nhập Google thành công xong
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                // Lấy ra tokenId của tài khoản đã đăng nhập Google thành công
                String tokenId = googleSignInAccount.getIdToken();
                this.chungThucDangNhapFireBase(tokenId); // Xác thực việc đăng nhập Google thành công của người dùng cho Firebase biết
            }
        }
        else {
            // Gọi hàm onActivityResult của Facebook SDK (return boolean value), chứ ko phải của Android
            this.callbackManagerFacebook.onActivityResult(requestCode,resultCode,data);
        }
    }

    /* - Hàm lắng nghe sự kiện về sự thay đổi trạng thái đăng nhập / đăng xuất của User */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
        if(firebaseUser != null){ // Đăng nhập thành công
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putString("uid",firebaseUser.getUid());
            editor.commit();
            this.progressDialog.dismiss();
            Toast.makeText(DangNhapActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
            Intent iTrangChu = new Intent(DangNhapActivity.this,TrangChuActivity.class);
            startActivity(iTrangChu);
        }
        else{ // Đăng nhập thất bại
            //Toast.makeText(DangNhapActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* - Đăng ký sự kiện lắng nghe sự thay đổi trạng thái đăng nhập / đăng xuất của User cho Firebase Authentication */
        this.firebaseAuth.addAuthStateListener(DangNhapActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* - HỦY sự kiện lắng nghe sự thay đổi trạng thái đăng nhập / đăng xuất của User cho Firebase Authentication */
        this.firebaseAuth.removeAuthStateListener(DangNhapActivity.this);
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Lắng nghe sự kiện User click vào button Đăng nhập google, facebook, email
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnDangNhapGoogle:
                // Gọi Intent để mở
                this.dangNhapGoogle(this.apiClient);
                break;
            case R.id.btnDangNhapFacebook:
                this.dangNhapFacebook();
                break;
            case R.id.txtDangKyMoi:
                Intent iDangKy = new Intent(DangNhapActivity.this,DangKyActivity.class);
                startActivity(iDangKy);
                break;
            case R.id.btnDangNhap:
                if(this.edEmailDN.getText().toString().trim().length() == 0){
                    Toast.makeText(DangNhapActivity.this,getString(R.string.thongbaonhapemail),Toast.LENGTH_SHORT).show();
                }
                else if(this.edPasswordDN.getText().toString().trim().length() == 0){
                    Toast.makeText(DangNhapActivity.this,getString(R.string.thongbaonhappassword),Toast.LENGTH_SHORT).show();
                }
                else{
                    this.dangNhapEmail(this.edEmailDN.getText().toString(),this.edPasswordDN.getText().toString());
                }
                break;
            case R.id.txtQuenMatKhau:
                Intent iQuenMatKhau = new Intent(DangNhapActivity.this,QuenMatKhauActivity.class);
                startActivity(iQuenMatKhau);
                break;
        }

    }
    // end Lắng nghe sự kiện User click vào button Đăng nhập google, facebook, email
}

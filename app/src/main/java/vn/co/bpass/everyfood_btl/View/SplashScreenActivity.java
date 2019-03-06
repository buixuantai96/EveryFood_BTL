package vn.co.bpass.everyfood_btl.View;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import vn.co.bpass.everyfood_btl.R;

public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextView txtPhienBan;
    PackageInfo packageInfo;
    GoogleApiClient googleApiClient;
    public static final int REQUEST_PERMISSION_LOCATION = 1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splashscreen);

        this.addControls();
        this.sharedPreferences = getSharedPreferences("toado",MODE_PRIVATE);

        this.googleApiClient = new GoogleApiClient.Builder(SplashScreenActivity.this)
                .addConnectionCallbacks(SplashScreenActivity.this) // Mở kết nối tới CH Play, và kiểm tra trạng th kết nối có thành công hay không
                .addOnConnectionFailedListener(SplashScreenActivity.this)
                .addApi(LocationServices.API).build(); // Add API dùng để xác định Location

        // Xin quyền Runtime Permission ( Từ Android 5.0 trở lên thường có để tăng bảo mật )
        int checkPermissionCoarseLocation = ContextCompat.checkSelfPermission(SplashScreenActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFineLocation = ContextCompat.checkSelfPermission(SplashScreenActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
        // Kiểm tra xem quyền CoarseLocation Permission đã được xin và cho phép hay chưa
        if(checkPermissionCoarseLocation != PackageManager.PERMISSION_GRANTED && checkPermissionFineLocation != PackageManager.PERMISSION_GRANTED){
            // Chưa được cấp quyền => xin quyền
            // Mở Pop-up để xin quyền Location
            ActivityCompat.requestPermissions(SplashScreenActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION);
        }
        // Đã xin quyền rồi -> Mở kết nối tới GG play services
        else{
            this.googleApiClient.connect();
        }



    }

    // Hàm lắng nghe trạng thái của các quyền đã được xin, lắng nghe có xin quyền thành công hay không ?
    // Lắng nghe dựa trên requestCode của các quyền khác nhau
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION_LOCATION:
                // length của mảng > 0, tham số 2 : Quyền (RUNTIME PERMISSION) nằm ở index 0 của mảng
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Nếu quyền đã được chấp nhận, mở kết nối tới google play service
                    this.googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.googleApiClient.connect(); // Connect đến Google play services
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.googleApiClient.disconnect();
    }

    /* - CHÚ Ý QUAN TRỌNG : Khi muốn xin một quyền RUNTIME PERMISSION, thì quyền đó trước tiên phải được khai báo ở bên trong
       file manifests */

    /* - Khi QUYỀN LOCATION đã được xin thành công, và hàm this.googleApiClient.connect(); được gọi, tức là kết nối tới GG play
      services thành công. Hàm onConnected() sẽ chạy ( bắt sự kiện kết nối thành công tới gg play services ) */
    // Đã kết nối thành công tới CH Play
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /* - CHÚ Ý : Lệnh này phải luôn luôn được khai báo NGAY Ở PHÍA DƯỚI của các câu lệnh XIN QUYỀN RUNTIME PERMISSION. Nếu
          không thì IDE sẽ báo lỗi */
        Location viTriHienTai = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
        if(viTriHienTai != null){
            // Tạo Editor để đẩy dữ liệu vào
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putString("latitude", String.valueOf(viTriHienTai.getLatitude()));
            editor.putString("longitude", String.valueOf(viTriHienTai.getLongitude()));
            editor.commit(); // Ghi dữ liệu tọa độ latitude, longitude vào SharedPreference
            Log.d("kiemtravitrihientai",viTriHienTai.getLatitude()+ " - " + viTriHienTai.getLongitude() );
        }
        this.delayAndPushToDangNhapActivity();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void addControls(){
        /* - Lấy ra PackageInfo để truy xuất ra thông tin versionName của ứng dụng hiện tại trong file gradle module app*/
        try {
            this.packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            this.txtPhienBan = (TextView) findViewById(R.id.txtPhienBan);
            txtPhienBan.setText(getString(R.string.phienban) + " " + this.packageInfo.versionName);
        }
    }

    public void delayAndPushToDangNhapActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển sang màn hình DangNhapActivity
                Intent iDangNhap = new Intent(SplashScreenActivity.this,DangNhapActivity.class);
                startActivity(iDangNhap);
                /* - Sau khi đã chuyển sang màn hình DangNhapActivity, màn hình SpalashScreenActivity sẽ bị finish(), tức là bị
                  xóa khỏi cái Array mà lưu trữ các Activity đã từng mở
                   - Do vậy, khi ở trong màn hình DangNhapActivity ta ấn nút Back, thì sẽ thoát App luôn, chứ không phải back
                   về màn hình SplashScreen nữa */
                finish();
            }
        },2000);
    }
}



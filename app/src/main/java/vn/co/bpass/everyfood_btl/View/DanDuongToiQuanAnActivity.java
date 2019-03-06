package vn.co.bpass.everyfood_btl.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import vn.co.bpass.everyfood_btl.Controller.DanDuongToiQuanAnController;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

public class DanDuongToiQuanAnActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap googleMap;
    MapFragment mapFragment;
    double latitudeQuanAn = 0;
    double longitudeQuanAn = 0;
    String tenQuanAn;
    SharedPreferences sharedPreferences;
    Location viTriHienTai;
    String url = "";

    DanDuongToiQuanAnController danDuongToiQuanAnController;

    // https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=YOUR_API_KEY

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danduong);

        this.latitudeQuanAn = getIntent().getDoubleExtra("latitude",0);
        this.longitudeQuanAn = getIntent().getDoubleExtra("longitude",0);
        this.tenQuanAn = getIntent().getStringExtra("tenquanan");
        //Log.d("kiemtraNhan",latitudeQuanAn + " - " + longitudeQuanAn);

        // Lấy ra sharedPreferences chứa dữ liệu current location ( latitude, longitude )
        this.sharedPreferences = getSharedPreferences("toado", Context.MODE_PRIVATE);
        this.viTriHienTai = new Location(""); // Khởi tạo đối tượng Location rỗng
        this.viTriHienTai.setLatitude(Double.parseDouble(this.sharedPreferences.getString("latitude","0")));
        this.viTriHienTai.setLongitude(Double.parseDouble(this.sharedPreferences.getString("longitude","0")));

        this.mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapDanDuongQuanAn);
        this.mapFragment.getMapAsync(DanDuongToiQuanAnActivity.this);

        this.url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + this.viTriHienTai.getLatitude() + "," + this.viTriHienTai.getLongitude() + "&destination=" + this.latitudeQuanAn + "," + this.longitudeQuanAn+ "&key=AIzaSyCljY4S1QSbG3YzXfWn_kwsRWY3NxNnig8";
        this.danDuongToiQuanAnController = new DanDuongToiQuanAnController();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Khi Google Map được load lên xong, ta giữ lại tham chiếu tới đối tượng Goolge Map
        this.googleMap = googleMap;
        // Clear hết các ký hiệu cũ trên bản đồ, Làm sạch google map, trước khi đánh dấu Marker trên bản đồ
        this.googleMap.clear();

        /* Đánh dấu Vị trí hiện tại ta đang đứng trên bản đồ */
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(this.viTriHienTai.getLatitude(),this.viTriHienTai.getLongitude()); // Tọa độ của nơi ta đang đứng
        markerOptions.position(latLng);
        markerOptions.title("Vị trí hiện tại của bạn");
        this.googleMap.addMarker(markerOptions);

        /* Zoom tới tọa độ của quán ăn */
        MarkerOptions markerOptionsQuanAn = new MarkerOptions();
        LatLng viTriQuanAn = new LatLng(this.latitudeQuanAn,this.longitudeQuanAn); // Tọa độ của Quán Ăn
        markerOptionsQuanAn.position(viTriQuanAn);
        markerOptionsQuanAn.title(this.tenQuanAn);
        this.googleMap.addMarker(markerOptionsQuanAn);

        // Zoom tới vị trí ta đang đứng trên bản đồ
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,14);
        this.googleMap.moveCamera(cameraUpdate);

        // Sau khi MAP đã được Load xong lên giao diện
        this.danDuongToiQuanAnController.hienThiDanDuongToiQuanAn(this.googleMap,this.url);
    }
}

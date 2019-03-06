package vn.co.bpass.everyfood_btl.View.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import vn.co.bpass.everyfood_btl.Controller.OdauController;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/22/17.
 */

public class OdauFragment extends Fragment {

    OdauController odauController;
    RecyclerView recyclerOdau;
    ProgressBar progressBarOdauFragment;
    NestedScrollView nestedScrollViewOdau;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.firebaseAuth = FirebaseAuth.getInstance();


        //this.firebaseAuth.signOut();
        //LoginManager.getInstance().logOut();

        /* - Khởi tạo giao diện cho Fragment */
        View layout = inflater.inflate(R.layout.layout_fragment_odau,container,false);
        this.recyclerOdau = (RecyclerView) layout.findViewById(R.id.recyclerOdau);
        this.nestedScrollViewOdau = (NestedScrollView) layout.findViewById(R.id.nestedScrollViewOdau);
        // Khởi tạo ProgressBar để hiển thị việc loading dữ liệu từ Firebase về RecyclerView
        this.progressBarOdauFragment = (ProgressBar) layout.findViewById(R.id.progressBarOdauFragment);





        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Lấy ra sharedPreferences chứa dữ liệu current location ( latitude, longitude )
        this.sharedPreferences = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);
        Location viTriHienTai = new Location(""); // Khởi tạo đối tượng Location rỗng
        viTriHienTai.setLatitude(Double.parseDouble(this.sharedPreferences.getString("latitude","0")));
        viTriHienTai.setLongitude(Double.parseDouble(this.sharedPreferences.getString("longitude","0")));

        //Log.d("kiemtraodau",sharedPreferences.getString("latitude","0") + " ");

        this.odauController = new OdauController(getContext());
        /* - CHÚ Ý : ProgressBar sẽ được tắt, khi dữ liệu bắt đầu được đổ vào trong RecyclerView, tức là , ProgressNar sẽ được
          tắt, khi hàm getDanhSachQuanAnModel() của OdauInterface nằm bên OdauController được kích hoạt */
        /* - Truyền nestedScrollViewOdau sang OdauController để xử lý */
        this.odauController.getDanhSachQuanAnController(getContext(),this.nestedScrollViewOdau,this.recyclerOdau,this.progressBarOdauFragment,viTriHienTai);
    }
}

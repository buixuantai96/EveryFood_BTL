package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.co.bpass.everyfood_btl.Adapters.AdapterViewPagerTrangChu;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/20/17.
 */

public class TrangChuActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    ViewPager viewPagerTrangChu;
    RadioButton rdOdau,rdAnGi;
    RadioGroup radioGroupTrangChu;
    ImageView imgThemQuanAn;

    Button btnHome;
    Button btnTaiKhoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);

        this.addControls();
    }

    public void addControls(){
        this.btnHome = (Button) findViewById(R.id.btnHome);
        this.btnHome.setOnClickListener(TrangChuActivity.this);

        this.btnTaiKhoan = (Button) findViewById(R.id.btnTaiKhoan);
        this.btnTaiKhoan.setOnClickListener(TrangChuActivity.this);

        this.imgThemQuanAn = (ImageView) findViewById(R.id.imgThemQuanAn);
        this.imgThemQuanAn.setOnClickListener(TrangChuActivity.this);

        this.viewPagerTrangChu = (ViewPager) findViewById(R.id.viewpager_trangchu);
        FragmentManager fragmentManager = getSupportFragmentManager();
        AdapterViewPagerTrangChu adapterViewPagerTrangChu = new AdapterViewPagerTrangChu(fragmentManager);
        this.viewPagerTrangChu.setAdapter(adapterViewPagerTrangChu);
        /* - Đăng ký lắng nghe sự kiện swipe của ViewPager */
        this.viewPagerTrangChu.addOnPageChangeListener(TrangChuActivity.this);


        /* - Đăng ký lắng nghe sự kiện CheckedChange cho RadioButtonGroup
           - Dễ hơn là bắt sự kiện cho từng RadioButton */
        this.radioGroupTrangChu = (RadioGroup) findViewById(R.id.radioGroup_odau_angi);
        this.radioGroupTrangChu.setOnCheckedChangeListener(TrangChuActivity.this);

        this.rdOdau = (RadioButton) findViewById(R.id.rdODau);
        this.rdAnGi = (RadioButton) findViewById(R.id.rdAnGi);
    }


    /* - Các hàm bắt sự kiện khi swipe ViewPager để chọn các Page khác nhau ở trong ViewPager */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //Log.d("kiemtra",position+"");
        switch (position){
            case 0:
                this.rdOdau.setChecked(true);
                break;
            case 1:
                this.rdAnGi.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /* - Lắng nghe sự kiện click vào các RadioButton nằm trong RadioButtonGroup */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rdODau:
                // Nhảy tới Page hiện tại của ViewPager với Index do mình chỉ định
                this.viewPagerTrangChu.setCurrentItem(0);
                break;
            case R.id.rdAnGi:
                this.viewPagerTrangChu.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnHome:
                // Nhảy tới Page hiện tại của ViewPager với Index do mình chỉ định
                viewPagerTrangChu.setCurrentItem(0);
                break;

            case R.id.imgThemQuanAn:
                Intent iThemQuanAn = new Intent(TrangChuActivity.this,ThemQuanAnActivity.class);
                startActivity(iThemQuanAn);
                break;
            case R.id.btnTaiKhoan:
                Intent iTaiKhoan = new Intent(TrangChuActivity.this,TaiKhoanActivity.class);
                startActivity(iTaiKhoan);
                break;
        }
    }
}

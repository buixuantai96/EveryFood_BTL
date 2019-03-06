package vn.co.bpass.everyfood_btl.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.co.bpass.everyfood_btl.View.Fragments.AnGiFragment;
import vn.co.bpass.everyfood_btl.View.Fragments.OdauFragment;

/**
 * Created by VietDung-KMA-AT11D on 9/22/17.
 */

/* - Cho Adapter của ViewPager extends FragmentStatePagerAdapter hoặc FragmentPagerAdapter đều được */
public class AdapterViewPagerTrangChu extends FragmentStatePagerAdapter {
    OdauFragment odauFragment;
    AnGiFragment anGiFragment;

    public AdapterViewPagerTrangChu(FragmentManager fm) {
        super(fm);
        /* - Khởi tạo 2 Fragment để thêm vào trong ViewPager */
        odauFragment = new OdauFragment();
        anGiFragment = new AnGiFragment();
    }

    @Override
    public Fragment getItem(int position) {
        /* - Tương ứng với Position nào, thì trả ra Fragment tương ứng */
        switch (position){
            case 0:
                return this.odauFragment;
            case 1:
                return this.anGiFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

package vn.co.bpass.everyfood_btl.Controller;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterThucDon;
import vn.co.bpass.everyfood_btl.Controller.interfaces.ThucDonInterface;
import vn.co.bpass.everyfood_btl.Model.ThucDonModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class ThucDonController {
    ThucDonModel thucDonModel;

    public ThucDonController() {
        this.thucDonModel = new ThucDonModel();
    }

    public void getDanhSachThucDonQuanAnTheoMaQuanAn(final Context context, String maquanan, final RecyclerView recyclerThucDon){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerThucDon.setLayoutManager(layoutManager);
        ThucDonInterface thucDonInterface = new ThucDonInterface() {
            @Override
            public void downloadThucDonThanhCong(List<ThucDonModel> listThucDonModel) {
                //Log.d("kiemtra",listThucDonModel.size()+"");
                AdapterThucDon adapterThucDon = new AdapterThucDon(context, R.layout.custom_layout_thucdon,listThucDonModel);
                recyclerThucDon.setAdapter(adapterThucDon);
                adapterThucDon.notifyDataSetChanged();
            }
        };

        this.thucDonModel.getDanhSachThucDonQuanAnTheoMaQuanAn(maquanan,thucDonInterface);
    }
}

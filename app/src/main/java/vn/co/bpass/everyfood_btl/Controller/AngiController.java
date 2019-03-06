package vn.co.bpass.everyfood_btl.Controller;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterRecyclerAngi;
import vn.co.bpass.everyfood_btl.Controller.interfaces.AnGiInterface;
import vn.co.bpass.everyfood_btl.Model.MonAnAnGiModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 10/3/17.
 */

public class AngiController {
    Context context;
    MonAnAnGiModel monAnAnGiModel;
    AdapterRecyclerAngi adapterRecyclerAngi;

    public AngiController(Context context){
        this.context = context;
        this.monAnAnGiModel = new MonAnAnGiModel();
    }

    public void getDanhSachMonAnAnGi(RecyclerView recyclerAnGi){
        final List<MonAnAnGiModel> listMonAnAngiModel = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.context,2);
        recyclerAnGi.setLayoutManager(layoutManager);
        this.adapterRecyclerAngi = new AdapterRecyclerAngi(context,R.layout.custom_layout_recyclerview_angi,listMonAnAngiModel);
        recyclerAnGi.setAdapter(adapterRecyclerAngi);

        AnGiInterface anGiInterface = new AnGiInterface() {
            @Override
            public void getDanhSachMonAnAnGi(MonAnAnGiModel monAnAnGiModel) {
                //Log.d("tenmonan",monAnAnGiModel.getTenmonan());
                listMonAnAngiModel.add(monAnAnGiModel);
                adapterRecyclerAngi.notifyDataSetChanged();
            }
        };

        this.monAnAnGiModel.getDanhSachMonAn(anGiInterface);
    }
}

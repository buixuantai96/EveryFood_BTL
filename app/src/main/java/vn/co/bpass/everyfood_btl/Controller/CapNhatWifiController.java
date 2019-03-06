package vn.co.bpass.everyfood_btl.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterDanhSachWifi;
import vn.co.bpass.everyfood_btl.Controller.interfaces.ChiTietQuanAnInterface;
import vn.co.bpass.everyfood_btl.Model.WifiQuanAnModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class CapNhatWifiController {
    WifiQuanAnModel wifiQuanAnModel;
    List<WifiQuanAnModel> listWifiQuanAnModel;
    Context context;

    public CapNhatWifiController(Context context){
        this.wifiQuanAnModel = new WifiQuanAnModel();
        this.listWifiQuanAnModel = new ArrayList<>();
        this.context = context;
    }

    public void hienThiDanhSachWifi(String maQuanAn, final RecyclerView recyclerDanhSachWifi){
        ChiTietQuanAnInterface chiTietQuanAnInterface = new ChiTietQuanAnInterface() {
            @Override
            public void hienThiDanhSachWifi(WifiQuanAnModel wifiQuanAnModel) {
                if(wifiQuanAnModel.getTen().equals("Wifi chưa có, mời bạn click vào để cập nhật")){
                    return;
                }
                else {
                    listWifiQuanAnModel.add(wifiQuanAnModel);
                    AdapterDanhSachWifi adapterDanhSachWifi = new AdapterDanhSachWifi(context, R.layout.layout_wifi_chitietquanan,listWifiQuanAnModel);
                    recyclerDanhSachWifi.setAdapter(adapterDanhSachWifi);
                    adapterDanhSachWifi.notifyDataSetChanged();
                }
            }
        };

        this.wifiQuanAnModel.layDanhSachWifiQuanAn(maQuanAn,chiTietQuanAnInterface);
    }

    public void themWifi(Context context,WifiQuanAnModel wifiQuanAnModel,String maQuanAn){
        this.wifiQuanAnModel.themWifiQuanAn(context,wifiQuanAnModel,maQuanAn);
    }

}

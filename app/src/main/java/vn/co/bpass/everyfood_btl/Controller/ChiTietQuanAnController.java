package vn.co.bpass.everyfood_btl.Controller;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Controller.interfaces.ChiTietQuanAnInterface;
import vn.co.bpass.everyfood_btl.Model.WifiQuanAnModel;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class ChiTietQuanAnController {
    WifiQuanAnModel wifiQuanAnModel;
    List<WifiQuanAnModel> listWifiQuanAnModel;

    public ChiTietQuanAnController() {
        this.wifiQuanAnModel = new WifiQuanAnModel();
        this.listWifiQuanAnModel = new ArrayList<>();
    }

    public void hienThiDanhSachWifiQuanAn(String maQuanAn, final TextView txtTenWifi, final TextView txtMatKhauWifi, final TextView txtNgayDangWifi){
        ChiTietQuanAnInterface chiTietQuanAnInterface = new ChiTietQuanAnInterface() {
            @Override
            public void hienThiDanhSachWifi(WifiQuanAnModel wifiQuanAnModel) {
                listWifiQuanAnModel.add(wifiQuanAnModel);
                txtTenWifi.setText(wifiQuanAnModel.getTen());
                txtMatKhauWifi.setText(wifiQuanAnModel.getMatkhau());
                txtNgayDangWifi.setText(wifiQuanAnModel.getNgaydang());

            }
        };

        this.wifiQuanAnModel.layDanhSachWifiQuanAn(maQuanAn,chiTietQuanAnInterface);
    }
}

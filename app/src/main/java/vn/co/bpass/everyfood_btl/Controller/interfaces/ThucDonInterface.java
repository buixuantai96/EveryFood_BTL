package vn.co.bpass.everyfood_btl.Controller.interfaces;

import java.util.List;

import vn.co.bpass.everyfood_btl.Model.ThucDonModel;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public interface ThucDonInterface {
    public void downloadThucDonThanhCong(List<ThucDonModel> listThucDonModel);
}

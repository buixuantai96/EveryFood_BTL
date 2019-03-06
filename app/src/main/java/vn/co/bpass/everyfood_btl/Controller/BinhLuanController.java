package vn.co.bpass.everyfood_btl.Controller;

import java.util.List;

import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;

/**
 * Created by VietDung-KMA-AT11D on 9/29/17.
 */

public class BinhLuanController {
    BinhLuanModel binhLuanModel;

    public BinhLuanController() {
        this.binhLuanModel = new BinhLuanModel();
    }

    public void themBinhLuan(BinhLuanModel binhLuanModel, final List<String> listDuongDanHinhAnh, String maQuanAn){
        this.binhLuanModel.themBinhLuan(binhLuanModel,listDuongDanHinhAnh,maQuanAn);
    }
}

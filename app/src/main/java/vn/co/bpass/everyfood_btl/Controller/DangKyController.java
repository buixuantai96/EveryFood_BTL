package vn.co.bpass.everyfood_btl.Controller;

import vn.co.bpass.everyfood_btl.Model.ThanhVienModel;

/**
 * Created by VietDung-KMA-AT11D on 9/24/17.
 */

public class DangKyController {
    ThanhVienModel thanhVienModel;

    public DangKyController() {
        this.thanhVienModel = new ThanhVienModel();
    }

        public void themThongTinThanhVienController(ThanhVienModel thanhVienModel,String uid){
            this.thanhVienModel.themThongTinThanhVien(thanhVienModel,uid);
        }
}

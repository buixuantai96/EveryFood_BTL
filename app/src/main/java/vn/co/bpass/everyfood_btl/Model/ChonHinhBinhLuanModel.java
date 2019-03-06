package vn.co.bpass.everyfood_btl.Model;

/**
 * Created by VietDung-KMA-AT11D on 9/29/17.
 */

public class ChonHinhBinhLuanModel {
    String duongdan;
    boolean isChecked;

    public ChonHinhBinhLuanModel() {
    }

    public ChonHinhBinhLuanModel(String duongdan, boolean isChecked) {
        this.duongdan = duongdan;
        this.isChecked = isChecked;
    }

    public String getDuongdan() {
        return duongdan;
    }

    public void setDuongdan(String duongdan) {
        this.duongdan = duongdan;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

package vn.co.bpass.everyfood_btl.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VietDung-KMA-AT11D on 9/25/17.
 */

public class ChiNhanhQuanAnModel implements Parcelable{
    String diachi;
    double latitude,longitude;
    double khoangcach; // Lưu khoảng cách giữa vị trí hiện tại ta đang đứng, tới vị trí của chi nhanh quán ăn

    public ChiNhanhQuanAnModel() {
    }

    protected ChiNhanhQuanAnModel(Parcel in) {
        diachi = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        khoangcach = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(diachi);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(khoangcach);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChiNhanhQuanAnModel> CREATOR = new Creator<ChiNhanhQuanAnModel>() {
        @Override
        public ChiNhanhQuanAnModel createFromParcel(Parcel in) {
            return new ChiNhanhQuanAnModel(in);
        }

        @Override
        public ChiNhanhQuanAnModel[] newArray(int size) {
            return new ChiNhanhQuanAnModel[size];
        }
    };

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(double khoangcach) {
        this.khoangcach = khoangcach;
    }
}

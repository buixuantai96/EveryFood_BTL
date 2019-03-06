package vn.co.bpass.everyfood_btl.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class DatMon implements Parcelable{
    String tenmonan;
    int soluong;

    public DatMon() {
    }

    protected DatMon(Parcel in) {
        tenmonan = in.readString();
        soluong = in.readInt();
    }

    public static final Creator<DatMon> CREATOR = new Creator<DatMon>() {
        @Override
        public DatMon createFromParcel(Parcel in) {
            return new DatMon(in);
        }

        @Override
        public DatMon[] newArray(int size) {
            return new DatMon[size];
        }
    };

    public String getTenmonan() {
        return tenmonan;
    }

    public void setTenmonan(String tenmonan) {
        this.tenmonan = tenmonan;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tenmonan);
        dest.writeInt(soluong);
    }
}

package vn.co.bpass.everyfood_btl.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

/**
 * Created by VietDung-KMA-AT11D on 9/24/17.
 */

public class BinhLuanModel implements Parcelable {
    long luotthich;
    double chamdiem;
    ThanhVienModel thanhVienModel; // Thay thế cho mauser
    String noidung, tieude, mauser;
    List<String> listHinhAnhBinhLuan; // 1 Bình luận có nhiều Hình Ảnh
    String mabinhluan;


    public BinhLuanModel() {

    }

    public void themBinhLuan(BinhLuanModel binhLuanModel, final List<String> listDuongDanHinhAnh, String maQuanAn) {
        DatabaseReference nodeBinhLuanRef = FirebaseDatabase.getInstance().getReference().child("binhluans");
        String keyBinhLuan = nodeBinhLuanRef.child(maQuanAn).push().getKey();
        nodeBinhLuanRef.child(maQuanAn).child(keyBinhLuan).setValue(binhLuanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listDuongDanHinhAnh.size() > 0) {
                        for (String duongDanHinhAnh : listDuongDanHinhAnh) {
                            Uri uri = Uri.fromFile(new File(duongDanHinhAnh));
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhanh/" + uri.getLastPathSegment());
                            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                }
                            });
                        }
                    }
                }
            }
        });

        if (listDuongDanHinhAnh.size() > 0) {
            for (String duongDanHinhAnh : listDuongDanHinhAnh) {
                Uri uri = Uri.fromFile(new File(duongDanHinhAnh));
                DatabaseReference nodeHinhAnhBinhLuanRef = FirebaseDatabase.getInstance().getReference().child("hinhanhbinhluans").child(keyBinhLuan);
                nodeHinhAnhBinhLuanRef.push().setValue(uri.getLastPathSegment());
            }
        }

    }

    protected BinhLuanModel(Parcel in) {
        luotthich = in.readLong();
        chamdiem = in.readDouble();
        noidung = in.readString();
        tieude = in.readString();
        mauser = in.readString();
        listHinhAnhBinhLuan = in.createStringArrayList();
        mabinhluan = in.readString();
        thanhVienModel = in.readParcelable(ThanhVienModel.class.getClassLoader());
    }

    public static final Creator<BinhLuanModel> CREATOR = new Creator<BinhLuanModel>() {
        @Override
        public BinhLuanModel createFromParcel(Parcel in) {
            return new BinhLuanModel(in);
        }

        @Override
        public BinhLuanModel[] newArray(int size) {
            return new BinhLuanModel[size];
        }
    };

    public double getChamdiem() {
        return chamdiem;
    }

    public void setChamdiem(double chamdiem) {
        this.chamdiem = chamdiem;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public ThanhVienModel getThanhVienModel() {
        return thanhVienModel;
    }

    public void setThanhVienModel(ThanhVienModel thanhVienModel) {
        this.thanhVienModel = thanhVienModel;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getMauser() {
        return mauser;
    }

    public void setMauser(String mauser) {
        this.mauser = mauser;
    }

    public List<String> getListHinhAnhBinhLuan() {
        return listHinhAnhBinhLuan;
    }

    public void setListHinhAnhBinhLuan(List<String> listHinhAnhBinhLuan) {
        this.listHinhAnhBinhLuan = listHinhAnhBinhLuan;
    }

    public String getMabinhluan() {
        return mabinhluan;
    }

    public void setMabinhluan(String mabinhluan) {
        this.mabinhluan = mabinhluan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(luotthich);
        dest.writeDouble(chamdiem);
        dest.writeString(noidung);
        dest.writeString(tieude);
        dest.writeString(mauser);
        dest.writeStringList(listHinhAnhBinhLuan);
        dest.writeString(mabinhluan);
        dest.writeParcelable(thanhVienModel, flags);
    }
}

package vn.co.bpass.everyfood_btl.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by VietDung-KMA-AT11D on 9/24/17.
 */

public class ThanhVienModel implements Parcelable{
    String hoten;
    String hinhanh;
    String mathanhvien;

    /* - Chú ý: những biến nào được khai báo ở bên trong Class, nhưng không được sử dụng để truyền dữ liệu lên Firebase realtime
     DB, thì ta phải để các biến đó là private, nếu không Firebase sẽ hiểu biến đó là public và truyền nó lên realtime DB, dẫn
     đến lỗi tràn bộ đệm . Vì Firebase chỉ cho phép truyền 4mb dữ liệu lên realtime db
       - Nếu không để private, Firebase sẽ hiểu lầm biến này là biến có thể được dùng để setValue() trong Firebase realtime DB,
       trong khi đó, biến có kiểu DatabaseReference có dung lượng rất lớn => Dẫn đến tràn bộ nhớ
       => KẾT LUẬN : biến nào không dùng để truyền dữ liệu lên Firebase realtime db thì nên để Private
          , và nên định rõ Access Modifier cho từng biến */
    private DatabaseReference dataNodeThanhVien;


    /* - CHÚ Ý : Bắt buộc phải có 1 Constructor rỗng mặc định, nếu không khi gọi hàm setValue() để truyền dữ liệu lên Firbase
      realtime DB sẽ bị lỗi Unknown Source */
    public ThanhVienModel() {
        this.dataNodeThanhVien = FirebaseDatabase.getInstance().getReference().child("thanhviens");
    }

    protected ThanhVienModel(Parcel in) {
        hoten = in.readString();
        hinhanh = in.readString();
        mathanhvien = in.readString();
    }

    public static final Creator<ThanhVienModel> CREATOR = new Creator<ThanhVienModel>() {
        @Override
        public ThanhVienModel createFromParcel(Parcel in) {
            return new ThanhVienModel(in);
        }

        @Override
        public ThanhVienModel[] newArray(int size) {
            return new ThanhVienModel[size];
        }
    };

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMathanhvien() {
        return mathanhvien;
    }

    public void setMathanhvien(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }

    public void themThongTinThanhVien(ThanhVienModel thanhVienModel, String uid) {

        /* - Khi gọi hàm setValue để truyền dữ liệu lên Firebase realtime DB, nếu tham số truyền vào trong hàm setValue là một
          Java Object, thì class của Java Object đó bắt buộc phải có 1 constructor mặc định rỗng tuếch, không có bất cứ một
          code nào ở trong Constructor đó, như vậy việc truyền dữ liệu mới có thể thành công */
        this.dataNodeThanhVien.child(uid).setValue(thanhVienModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hoten);
        dest.writeString(hinhanh);
        dest.writeString(mathanhvien);
    }
}

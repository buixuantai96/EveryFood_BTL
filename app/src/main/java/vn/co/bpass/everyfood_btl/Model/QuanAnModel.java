package vn.co.bpass.everyfood_btl.Model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Controller.interfaces.OdauInterface;

/**
 * Created by VietDung-KMA-AT11D on 9/23/17.
 */

public class QuanAnModel implements Parcelable{
    boolean giaohang;
    String maquanan, giodongcua, giomocua, tenquanan, videogioithieu;
    /* - Nhất định phải là kiểu dữ liệu List<String>, vì Firebase không hỗ trợ Mảng bình thường, xem thêm ở Read and Write Data
      ở trang Docs của Firebase */
    List<String> tienich,hinhanhquanan; // 1 Quán Ăn có nhiều hình ảnh, 1 Quán Ăn có nhiều tiện ích
    List<BinhLuanModel> listBinhLuanModel; // 1 Quán Ăn có nhiều bình luận
    List<ChiNhanhQuanAnModel> listChiNhanhQuanAnModel; // 1 Quán Ăn có nhiều Chi Nhánh, mỗi Chi Nhánh có địa chỉ,latitude,longitude
    List<Bitmap> listBitMap; // Serializable Không mã hóa được kiểu Bitmap vì kiểu Bitmap nặng, phải dùng Parcelable
    List<ThucDonModel> listThucDon;
    long luotthich;
    long giatoithieu,giatoida;

    DatabaseReference nodeRoot;

    public QuanAnModel() {
        // Link tới node root của Firebase realtime DB
        this.nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    private DataSnapshot dataRoot;

    /* - Chú ý : Để parse 1 class bằng Parcelable, cần thực hiện 2 bước là bước đọc và bước ghi, ghi trước sau đó đọc dữ liệu */
    // Bước đọc
    protected QuanAnModel(Parcel in) {
        giaohang = in.readByte() != 0;
        maquanan = in.readString();
        giodongcua = in.readString();
        giomocua = in.readString();
        tenquanan = in.readString();
        videogioithieu = in.readString();
        tienich = in.createStringArrayList();
        luotthich = in.readLong();
        giatoithieu = in.readLong();
        giatoida = in.readLong();
        hinhanhquanan = in.createStringArrayList();
        //listBitMap = in.createTypedArrayList(Bitmap.CREATOR);

        /* - Đối với custom List, cần phải khởi tạo trước, sau đó mới đọc được */
        listChiNhanhQuanAnModel = new ArrayList<ChiNhanhQuanAnModel>();
        in.readTypedList(listChiNhanhQuanAnModel,ChiNhanhQuanAnModel.CREATOR);
        listBinhLuanModel = new ArrayList<BinhLuanModel>();
        in.readTypedList(listBinhLuanModel,BinhLuanModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Bước ghi
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (giaohang ? 1 : 0));
        dest.writeString(maquanan);
        dest.writeString(giodongcua);
        dest.writeString(giomocua);
        dest.writeString(tenquanan);
        dest.writeString(videogioithieu);
        dest.writeStringList(tienich);
        dest.writeLong(luotthich);
        dest.writeLong(giatoithieu);
        dest.writeLong(giatoida);
        dest.writeStringList(hinhanhquanan);

        /* - Parcelable có thể làm việc với dữ liệu Bitmap, tuy nhiên ở đây là một List Bitmap, vì vậy dung lượng quá lớn
           dẫn tới tràn bộ nhớ, và Parcelable không thể hỗ trợ việc này */
        //dest.writeTypedList(listBitMap);
        dest.writeTypedList(listChiNhanhQuanAnModel);
        dest.writeTypedList(listBinhLuanModel);
    }

    public static final Creator<QuanAnModel> CREATOR = new Creator<QuanAnModel>() {
        @Override
        public QuanAnModel createFromParcel(Parcel in) {
            return new QuanAnModel(in);
        }

        @Override
        public QuanAnModel[] newArray(int size) {
            return new QuanAnModel[size];
        }
    };

    public void getDanhSachQuanAn(final OdauInterface odauInterface, final Location viTriHienTai, final int itemTiepTheo, final int itemDaLoad) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // dataSnapshot chính là dữ liệu được duyệt bắt đầu từ node Root
                dataRoot = dataSnapshot;
                layDanhSachQuanAn(dataSnapshot,odauInterface,viTriHienTai,itemTiepTheo,itemDaLoad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        /* - 2 câu lệnh If - else, giúp ta chỉ download dữ liệu của Node ROOT ở trên Firebase realtime DB 1 lần duy nhất,
        sau đó tái sử dụng lại cho các vòng lặp tiếp theo
           - Chứ không phải, ở mỗi lần vòng lặp LOAD MORE, ta lại download lại toàn bộ dữ liệu của Node ROOT, như vậy sẽ tốn tài
           nguyên server */
        if(this.dataRoot != null){ // ở lần Load More thứ 2, dataRoot đã chứa dữ liệu của node Root => Ko cần gọi
            // hàm addListenerForSingleValueEvent(), rồi chạy vào onDataChane() để download lại dữ liệu node Root nữa
            layDanhSachQuanAn(dataRoot,odauInterface,viTriHienTai,itemTiepTheo,itemDaLoad);
        }else {
            // Download dữ liệu từ Node Root của firebase realtime db
            /* - Khi phải lấy dữ liệu từ Firebase, mà dữ liệu lại nằm ở nhiều bảng khác nhau, có quan hệ key với nhau. Để tránh phải
           sử dụng đa tiến trình, hoặc là phải sử dụng các lời gọi hàm addListenerForSingleValueEvent() lồng nhau để truy vấn.
           - Ta sẽ lắng nghe sự kiện lấy dữ liệu từ Node Root luôn, từ đó lấy ra được dataSnapshot chứa dữ liệu của tất cả các
           node trong CSDL, không cần gọi addListenerForSingleValueEvent() cho từng node để lấy dữ liệu nữa */
            this.nodeRoot.addListenerForSingleValueEvent(valueEventListener); // Chạy lệnh này trước, rồi mới chạy onDataChange sau
        }
    }

    private void layDanhSachQuanAn(DataSnapshot dataSnapshot,OdauInterface odauInterface,Location viTriHienTai,int itemTiepTheo,int itemDaLoad){
        DataSnapshot dataSnapshotQuanAn = dataSnapshot.child("quanans"); // Lấy danh sách Quán Ăn

        int i = 0; // Đếm xem mình đã Load tới Quán Ăn thứ bao nhiêu rồi

        for(DataSnapshot valueQuanAn:dataSnapshotQuanAn.getChildren()){ // Khắc phục việc gặp phải Key động khi duyệt dữ liệu
            /* - Giả sử, ban đầu ta muốn load 3 sản phẩm, thì itemBanDau = 0, itemTiepTheo = 3
               - Muốn load tiếp 3 sản phầm tiếp theo, thì itemBanDau = 3, itemTiepTheo = 6 */
            if(i == itemTiepTheo){ // Load theo từng nhóm 3 Quán Ăn một lần
                break;
            }
            if(i < itemDaLoad){ // Bỏ qua, không load lại những Quán Ăn đã được load rồi
                i++;
                continue; // Không chạy các câu lệnh phía dưới, mà chạy lại vòng lặp FOR
            }
            i++; // Tăng i lên để load Quán Ăn tiếp theo
            QuanAnModel quanAnModel = valueQuanAn.getValue(QuanAnModel.class);
            quanAnModel.setMaquanan(valueQuanAn.getKey()); // Lấy ra MÃ QUÁN ĂN
            // Lấy danh sách hình ảnh của quán ăn thông qua Mã Quán Ăn
            DataSnapshot dataSnapshotHinhQuanAn = dataSnapshot.child("hinhanhquanans").child(valueQuanAn.getKey());
            List<String> listHinhAnh = new ArrayList<>();
            for(DataSnapshot valueHinhQuanAn:dataSnapshotHinhQuanAn.getChildren()){ // Khắc phục việc gặp phải Key động khi duyệt dữ liệu
                listHinhAnh.add(valueHinhQuanAn.getValue(String.class));
            }
            quanAnModel.setHinhanhquanan(listHinhAnh); // Lấy xong list hình ảnh của 1 Quán Ăn

            // Lấy danh sách các bình luận của QUÁN ĂN thông qua MÃ QUÁN ĂN
            DataSnapshot dataSnapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
            List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
            for(DataSnapshot valueBinhLuan:dataSnapshotBinhLuan.getChildren()){
                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                binhLuanModel.setThanhVienModel(thanhVienModel);

                // Lấy danh sách hình ảnh , của 1 bình luận, dựa vào mã bình luận
                List<String> listHinhAnhBinhLuan = new ArrayList<>();
                DataSnapshot dataSnapshotHinhAnhBinhLuan = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
                for(DataSnapshot valueHinhAnhBinhLuan:dataSnapshotHinhAnhBinhLuan.getChildren()){ // 1 Mã bình luận -> có nhiều Hình Ảnh
                    listHinhAnhBinhLuan.add(valueHinhAnhBinhLuan.getValue(String.class));
                }
                binhLuanModel.setListHinhAnhBinhLuan(listHinhAnhBinhLuan);
                binhLuanModelList.add(binhLuanModel);
            }
            quanAnModel.setListBinhLuanModel(binhLuanModelList); //  Lấy được danh sách Bình Luận của QUÁN ĂN

            // Lấy chi nhánh Quán Ăn
            List<ChiNhanhQuanAnModel> listChiNhanhQuanAnModel = new ArrayList<>();
            DataSnapshot snapshotChiNhanhQuanAn = dataSnapshot.child("chinhanhquanans").child(quanAnModel.getMaquanan());
            for(DataSnapshot valueChiNhanhQuanAn:snapshotChiNhanhQuanAn.getChildren()){
                ChiNhanhQuanAnModel chiNhanhQuanAnModel = valueChiNhanhQuanAn.getValue(ChiNhanhQuanAnModel.class);

                        /* - Tính khoảng cách từ vị trí hiện tại, đến các chi nhánh của các Quán Ăn */
                Location viTriQuanAn = new Location("");
                viTriQuanAn.setLatitude(chiNhanhQuanAnModel.getLatitude());
                viTriQuanAn.setLongitude(chiNhanhQuanAnModel.getLongitude());
                Log.d("kiemtravitri","lat : " + viTriHienTai.getLatitude() + " - long : " + viTriHienTai.getLongitude());

                double khoangcach = viTriHienTai.distanceTo(viTriQuanAn)/1000;
                //Log.d("khoangcach",khoangcach/1000 + " - " +  chiNhanhQuanAnModel.getDiachi());
                chiNhanhQuanAnModel.setKhoangcach(khoangcach);
                listChiNhanhQuanAnModel.add(chiNhanhQuanAnModel);
            }
            quanAnModel.setListChiNhanhQuanAnModel(listChiNhanhQuanAnModel);

                    /* - Truyền 1 đối tượng QuanAnModel đã có đủ dữ liệu, sang oDauController, để hiển thị dữ liệu lên màn hình
                       oDauFragment*/
                    /* - Khi chạy đến câu lệnh này, hàm getDanhSachQuanAnModel() ở bên OdauController sẽ được
                        chạy, đối tượng quanAnModel đã được gán đủ các dữ liệu ở QuanAnModel class sẽ được truyền dữ liệu sang
                        OdauController class để hiển thị dữ liệu lên RecyclerView */
            odauInterface.getDanhSachQuanAnModel(quanAnModel);
        }
    }


    public boolean isGiaohang() {
        return giaohang;
    }

    public void setGiaohang(boolean giaohang) {
        this.giaohang = giaohang;
    }

    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }

    public String getVideogioithieu() {
        return videogioithieu;
    }

    public void setVideogioithieu(String videogioithieu) {
        this.videogioithieu = videogioithieu;
    }

    public List<String> getTienich() {
        return tienich;
    }

    public void setTienich(List<String> tienich) {
        this.tienich = tienich;
    }

    public Long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(Long luotthich) {
        this.luotthich = luotthich;
    }

    public List<String> getHinhanhquanan() {
        return hinhanhquanan;
    }

    public void setHinhanhquanan(List<String> hinhanhquanan) {
        this.hinhanhquanan = hinhanhquanan;
    }

    public List<BinhLuanModel> getListBinhLuanModel() {
        return listBinhLuanModel;
    }

    public void setListBinhLuanModel(List<BinhLuanModel> listBinhLuanModel) {
        this.listBinhLuanModel = listBinhLuanModel;
    }

    public List<ChiNhanhQuanAnModel> getListChiNhanhQuanAnModel() {
        return listChiNhanhQuanAnModel;
    }

    public void setListChiNhanhQuanAnModel(List<ChiNhanhQuanAnModel> listChiNhanhQuanAnModel) {
        this.listChiNhanhQuanAnModel = listChiNhanhQuanAnModel;
    }

    public List<Bitmap> getListBitMap() {
        return listBitMap;
    }

    public void setListBitMap(List<Bitmap> listBitMap) {
        this.listBitMap = listBitMap;
    }


    public Long getGiatoithieu() {
        return giatoithieu;
    }

    public void setGiatoithieu(Long giatoithieu) {
        this.giatoithieu = giatoithieu;
    }

    public Long getGiatoida() {
        return giatoida;
    }

    public void setGiatoida(Long giatoida) {
        this.giatoida = giatoida;
    }

    public List<ThucDonModel> getListThucDon() {
        return listThucDon;
    }

    public void setListThucDon(List<ThucDonModel> listThucDon) {
        this.listThucDon = listThucDon;
    }
}

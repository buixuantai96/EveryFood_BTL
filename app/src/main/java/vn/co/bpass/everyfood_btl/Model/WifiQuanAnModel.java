package vn.co.bpass.everyfood_btl.Model;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.co.bpass.everyfood_btl.Controller.interfaces.ChiTietQuanAnInterface;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class WifiQuanAnModel {
    String ten, matkhau, ngaydang;

    private int flag = 0;

    public WifiQuanAnModel() {

    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getNgaydang() {
        return ngaydang;
    }

    public void setNgaydang(String ngaydang) {
        this.ngaydang = ngaydang;
    }


    private DatabaseReference nodeWifiQuanAnRef;

    public void layDanhSachWifiQuanAn(String maQuanAn, final ChiTietQuanAnInterface chiTietQuanAnInterface) { // 1 Quán Ăn có nhiều mã Wifi
        this.nodeWifiQuanAnRef = FirebaseDatabase.getInstance().getReference().child("wifiquanans").child(maQuanAn);
//        this.nodeWifiQuanAnRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot valueWifi:dataSnapshot.getChildren()){
//                    //Log.d("kiemtra",valueWifi+"");
//                    WifiQuanAnModel wifiQuanAnModel = valueWifi.getValue(WifiQuanAnModel.class);
//                    chiTietQuanAnInterface.hienThiDanhSachWifi(wifiQuanAnModel);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        this.nodeWifiQuanAnRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                flag = 1;
                WifiQuanAnModel wifiQuanAnModel = dataSnapshot.getValue(WifiQuanAnModel.class);
                //Log.d("kiemtra",wifiQuanAnModel.getTen()+"aaaa");
                chiTietQuanAnInterface.hienThiDanhSachWifi(wifiQuanAnModel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (flag == 0) {
            WifiQuanAnModel wifiQuanAnModel = new WifiQuanAnModel();
            wifiQuanAnModel.setTen("Wifi chưa có, mời bạn click vào để cập nhật");
            wifiQuanAnModel.setNgaydang("");
            wifiQuanAnModel.setMatkhau("");
            chiTietQuanAnInterface.hienThiDanhSachWifi(wifiQuanAnModel);
        }
    }

    public void themWifiQuanAn(final Context context, WifiQuanAnModel wifiQuanAnModel, String maQuanAn) {
        DatabaseReference refNodeWifiQuanAns = FirebaseDatabase.getInstance().getReference().child("wifiquanans").child(maQuanAn);
        refNodeWifiQuanAns.push().setValue(wifiQuanAnModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(context, context.getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

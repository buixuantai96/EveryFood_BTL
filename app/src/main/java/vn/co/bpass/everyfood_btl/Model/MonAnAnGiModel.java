package vn.co.bpass.everyfood_btl.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.co.bpass.everyfood_btl.Controller.interfaces.AnGiInterface;

/**
 * Created by VietDung-KMA-AT11D on 10/3/17.
 */

public class MonAnAnGiModel {
    String mamon;
    long giatien;
    String hinhanh;
    String tenmonan;

    DatabaseReference nodeRoot;
    private DataSnapshot dataRoot;
    public MonAnAnGiModel() {
        // Link tới node root của Firebase realtime DB
        this.nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public void getDanhSachMonAn(final AnGiInterface anGiInterface){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                layDanhSachMonAn(dataRoot,anGiInterface);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if(this.dataRoot != null){
            this.layDanhSachMonAn(dataRoot,anGiInterface);
        }
        else {
            this.nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    public void layDanhSachMonAn(DataSnapshot dataRoot,AnGiInterface anGiInterface){
        DataSnapshot dataSnapshotMonAn = dataRoot.child("monans");
        //Log.d("snapshot",dataSnapshotMonAn+"");
        for(DataSnapshot valueMonAn:dataSnapshotMonAn.getChildren()){
            //Log.d("valueMonAn",valueMonAn+"");
            MonAnAnGiModel monAnAnGiModel = valueMonAn.getValue(MonAnAnGiModel.class);
            anGiInterface.getDanhSachMonAnAnGi(monAnAnGiModel);
        }
    }

    public String getMamon() {
        return mamon;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public long getGiatien() {
        return giatien;
    }

    public void setGiatien(long giatien) {
        this.giatien = giatien;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTenmonan() {
        return tenmonan;
    }

    public void setTenmonan(String tenmonan) {
        this.tenmonan = tenmonan;
    }
}

package vn.co.bpass.everyfood_btl.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Controller.interfaces.ThucDonInterface;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class ThucDonModel {
    String mathucdon;
    String tenthucdon;
    List<MonAnModel> listMonAn;

    public ThucDonModel() {
    }

    public String getMathucdon() {
        return mathucdon;
    }

    public void setMathucdon(String mathucdon) {
        this.mathucdon = mathucdon;
    }

    public String getTenthucdon() {
        return tenthucdon;
    }

    public void setTenthucdon(String tenthucdon) {
        this.tenthucdon = tenthucdon;
    }

    public List<MonAnModel> getListMonAn() {
        return listMonAn;
    }

    public void setListMonAn(List<MonAnModel> listMonAn) {
        this.listMonAn = listMonAn;
    }

    public void getDanhSachThucDonQuanAnTheoMaQuanAn(final String maquanan, final ThucDonInterface thucDonInterface){
        DatabaseReference nodeThucDonQuanAnRef = FirebaseDatabase.getInstance().getReference().child("thucdonquanans").child(maquanan);
        nodeThucDonQuanAnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final List<ThucDonModel> listThucDonModel = new ArrayList<ThucDonModel>();

                //Log.d("kiemtra",dataSnapshot+"");
                for(DataSnapshot valueThucDon:dataSnapshot.getChildren()){
                    final ThucDonModel thucDonModel = new ThucDonModel();

                    //Log.d("kiemtra",valueThucDon+"");
                    DatabaseReference nodeThucDonRef = FirebaseDatabase.getInstance().getReference().child("thucdons").child(valueThucDon.getKey());
                    nodeThucDonRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotThucDon) {
                            //Log.d("kiemtra",dataSnapshot+"");
                            String mathucdon = dataSnapshotThucDon.getKey();
                            thucDonModel.setMathucdon(mathucdon);
                            thucDonModel.setTenthucdon(dataSnapshotThucDon.getValue().toString());
                            //Log.d("kiemtra",dataSnapshot.child(mathucdon).getValue()+"");
                            //Log.d("kiemtraMaThucDon",mathucdon);
                            List<MonAnModel> listMonAnModel = new ArrayList<MonAnModel>();

                            for(DataSnapshot valueMonAn:dataSnapshot.child(mathucdon).getChildren()){
                                //Log.d("kiemtra",valueMonAn+"");
                                MonAnModel monAnModel = valueMonAn.getValue(MonAnModel.class);
                                monAnModel.setMamon(valueMonAn.getKey());
                                //Log.d("kiemtra",monAnModel.getTenmon());
                                listMonAnModel.add(monAnModel);
                            }
                            thucDonModel.setListMonAn(listMonAnModel);
                            listThucDonModel.add(thucDonModel);

                            thucDonInterface.downloadThucDonThanhCong(listThucDonModel);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

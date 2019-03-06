package vn.co.bpass.everyfood_btl.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Adapters.AdapterRecyclerOdau;
import vn.co.bpass.everyfood_btl.Controller.interfaces.OdauInterface;
import vn.co.bpass.everyfood_btl.Model.QuanAnModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/23/17.
 */

public class OdauController {
    Context context;
    QuanAnModel quanAnModel;
    AdapterRecyclerOdau adapterRecyclerOdau;
    int itemDaLoad = 3;


    public OdauController(Context context) {
        this.context = context;
        this.quanAnModel = new QuanAnModel();
    }

    public void getDanhSachQuanAnController(Context context,NestedScrollView nestedScrollViewOdau, RecyclerView recyclerViewOdau, final ProgressBar progressBarOdauFragment, final Location viTriHienTai) {
        final List<QuanAnModel> listQuanAnModel = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.context);
        recyclerViewOdau.setLayoutManager(layoutManager);
        this.adapterRecyclerOdau = new AdapterRecyclerOdau(context,listQuanAnModel, R.layout.custom_layout_recycleview_odau);
        recyclerViewOdau.setAdapter(adapterRecyclerOdau);

        // Hiển thị ProgressBar
        progressBarOdauFragment.setVisibility(View.VISIBLE);


        final OdauInterface odauInterface = new OdauInterface() {
            @Override
            public void getDanhSachQuanAnModel(final QuanAnModel quanAnModel) {
                final List<Bitmap> listBitMap = new ArrayList<>();
                for (String linkHinh : quanAnModel.getHinhanhquanan()) {
                    // Download hình ảnh từ Storage,  mỗi Quán Ăn có thể có nhiều ảnh, nhưng ta sẽ chọn ảnh đầu tiên để show lên
                    StorageReference storageHinhAnh = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkHinh);
                    // Download dạng Byte
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageHinhAnh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            listBitMap.add(bitmap);
                            quanAnModel.setListBitMap(listBitMap);

                            // Tránh việc 1 quán ăn có nhiều hình ảnh, lại load dữ liệu 2 lần trùng nhau lên RecyclerView
                            if(quanAnModel.getListBitMap().size() == quanAnModel.getHinhanhquanan().size()){
                                listQuanAnModel.add(quanAnModel);
                                //Log.d("kiemtra",listQuanAnModel.size()+"");
                                Log.d("tenquanan",quanAnModel.getTenquanan());
                                adapterRecyclerOdau.notifyDataSetChanged();

                                // Load xong dữ liệu, Ẩn ProgressBar đi
                                progressBarOdauFragment.setVisibility(View.GONE);
                            }
                        }
                    });
                }


            }
        };

        /* - Bắt sự kiện Scroll của NestedScrollView */
        nestedScrollViewOdau.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            /* - Tham số đầu tiên NestedScrollView v, đại diện cho NestedScrollView */
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /* - Kiểm tra bên trong NestedScrollView có chứa các View con hay không, nếu có thì mới thực hiện
                   - Các View con ta muốn kiểm tra xem có hay không , chính là các Item con của RecyclerView
                   - CHÚ Ý : Array starts at index 0 => phải - 1*/
                if (v.getChildAt(v.getChildCount() - 1) != null) { // Lấy ra item cuối cùng nằm trong NestedScrollView, xem có null ko
                    /* - Nếu chiều cao của trục Y của thanh Scroll của NestedScrollView >= chiều cao của tất cả các item từ đầu
                    cho tới item cuối cùng hiện tại đang nằm trong NestedScrollView cộng lại TRỪ ĐI chiều cao của NestedScrollView
                    ( dùng >= bởi vì chiều cao trục Y của thanh Scroll của NestedScrollView thì luôn luôn > chiều cao của tất
                    cả các item con cộng lại ) */
                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1)).getMeasuredHeight() - v.getMeasuredHeight()) {
                        // Loadmore RecyclerView, mỗi lần load 3 Quán Ăn
                        itemDaLoad += 3;
                        quanAnModel.getDanhSachQuanAn(odauInterface, viTriHienTai, itemDaLoad, itemDaLoad - 3); // itemBanDau = 3-3 = 0
                    }
                }
            }
        });


            // Ban đầu khi mới mở lên, load duy nhất 3 Quán Ăn đầu tiên
            quanAnModel.getDanhSachQuanAn(odauInterface, viTriHienTai, itemDaLoad, 0); // itemBanDau = 3-3 = 0


    }
}

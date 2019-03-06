package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/25/17.
 */

public class AdapterBinhLuan extends RecyclerView.Adapter<AdapterBinhLuan.ViewHolder> {
    Context context;
    int layout;
    List<BinhLuanModel> listBinhLuanModel;



    public AdapterBinhLuan(Context context,int layout,List<BinhLuanModel> listBinhLuanModel){
        this.context = context;
        this.layout = layout;
        this.listBinhLuanModel = listBinhLuanModel;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView2;
        TextView txtTieuDeBinhLuan2,txtNoiDungBinhLuan2,txtSoDiem2;
        RecyclerView recyclerViewHinhBinhLuan;

        public ViewHolder(View itemView) {
            super(itemView);

            this.circleImageView2 = (CircleImageView) itemView.findViewById(R.id.circleImageUser2);
            this.txtTieuDeBinhLuan2 = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan2);
            this.txtNoiDungBinhLuan2 = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan2);
            this.txtSoDiem2 = (TextView) itemView.findViewById(R.id.txtChamDiemBinhLuan2);
            this.recyclerViewHinhBinhLuan = (RecyclerView) itemView.findViewById(R.id.recyclerHinhBinhLuan);
        }
    }

    @Override
    public AdapterBinhLuan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterBinhLuan.ViewHolder holder, int position) {
        final List<Bitmap> listBitmapHinhBinhLuan = new ArrayList<>();
        final BinhLuanModel binhLuanModel = this.listBinhLuanModel.get(position);
        holder.txtTieuDeBinhLuan2.setText(binhLuanModel.getTieude());
        holder.txtNoiDungBinhLuan2.setText(binhLuanModel.getNoidung());
        holder.txtSoDiem2.setText(binhLuanModel.getChamdiem()+"");
        //this.setHinhAnhBinhLuan(holder.circleImageView2,binhLuanModel.getThanhVienModel().getHinhanh());
        holder.circleImageView2.setImageResource(R.drawable.user);

        for(String linkHinhAnh:binhLuanModel.getListHinhAnhBinhLuan()){
            // Download hình ảnh của bình luận, từ Storage
            /* - User nào cũng có hinhanh, bởi vì khi đăng ký ta đã gán một cái hình mặc định là user.png cho mỗi User */
            // Download hình mặc định cho user về
            StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkHinhAnh);
            long ONE_MEGABYTE = 1024 * 1024;
            storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    listBitmapHinhBinhLuan.add(bitmap);
                    if(listBitmapHinhBinhLuan.size() == binhLuanModel.getListHinhAnhBinhLuan().size()){
                        AdapterRecyclerHinhBinhLuan adapterRecyclerHinhBinhLuan = new AdapterRecyclerHinhBinhLuan(context,R.layout.custom_layout_hinhbinhluan,listBitmapHinhBinhLuan,binhLuanModel,false);
                        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(context,2); // Đối số thứ 2 : số cột của GridView
                        holder.recyclerViewHinhBinhLuan.setLayoutManager(gridLayoutManager);
                        holder.recyclerViewHinhBinhLuan.setAdapter(adapterRecyclerHinhBinhLuan);
                        adapterRecyclerHinhBinhLuan.notifyDataSetChanged();
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
//        int soBinhLuan = this.listBinhLuanModel.size();
//        if( soBinhLuan > 5){
//            return 5;
//        }
//        else{
//            return this.listBinhLuanModel.size();
//        }
        return this.listBinhLuanModel.size();
    }
    // Lấy dữ liệu hình ảnh bình luận của Quán Ăn từ Firebase, set lên giao diện
    private void setHinhAnhBinhLuan(final CircleImageView circleImageView, String linkHinhAnh){
            /* - User nào cũng có hinhanh, bởi vì khi đăng ký ta đã gán một cái hình mặc định là user.png cho mỗi User */
            // Download hình mặc định cho user về
            StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("thanhvien").child(linkHinhAnh);
            long ONE_MEGABYTE = 1024 * 1024;
            storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Chuyển Byte thành Bitmap
                    /* - Đối số 1 : Byte của hình ảnh đã donwload được về từ Storage
                       - Đối số 2 : offset
                       - Đối số 3 : length */
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    circleImageView.setImageBitmap(bitmap);
                }
            });
        }

}

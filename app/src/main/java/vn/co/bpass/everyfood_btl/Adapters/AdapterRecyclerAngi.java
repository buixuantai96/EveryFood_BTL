package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import vn.co.bpass.everyfood_btl.Model.MonAnAnGiModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 10/3/17.
 */

public class AdapterRecyclerAngi extends RecyclerView.Adapter<AdapterRecyclerAngi.ViewHolderAngi> {
    Context context;
    int layout;
    List<MonAnAnGiModel> listMonAnAngiModel;

    public AdapterRecyclerAngi(Context context, int layout, List<MonAnAnGiModel> listMonAnAngiModel) {
        this.context = context;
        this.layout = layout;
        this.listMonAnAngiModel = listMonAnAngiModel;
    }

    public class ViewHolderAngi extends RecyclerView.ViewHolder {
        ImageView imgHinhMonAnAngi;
        TextView txtTenMonAnAngi,txtGiaTienMonAnAngi;

        public ViewHolderAngi(View itemView) {
            super(itemView);

            this.imgHinhMonAnAngi = (ImageView) itemView.findViewById(R.id.imgHinhMonAnAngi);
            this.txtTenMonAnAngi = (TextView) itemView.findViewById(R.id.txtTenMonAnAngi);
            this.txtGiaTienMonAnAngi = (TextView) itemView.findViewById(R.id.txtGiaTienMonAnAngi);
        }
    }

    @Override
    public ViewHolderAngi onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderAngi viewHolderAngi = new ViewHolderAngi(view);
        return viewHolderAngi;
    }

    @Override
    public void onBindViewHolder(ViewHolderAngi holder, int position) {
        MonAnAnGiModel monAnAnGiModel = listMonAnAngiModel.get(position);
        holder.txtTenMonAnAngi.setText(monAnAnGiModel.getTenmonan());
        NumberFormat numberFormat = new DecimalFormat("###,###");
        String sGiaTienMonAnAngi = numberFormat.format(monAnAnGiModel.getGiatien()) + "đ";
        holder.txtGiaTienMonAnAngi.setText(sGiaTienMonAnAngi);
        this.setHinhAnhMonAnAngi(holder.imgHinhMonAnAngi,monAnAnGiModel.getHinhanh());
    }

    // Lấy dữ liệu hình ảnh bình luận của Quán Ăn từ Firebase, set lên giao diện
    private void setHinhAnhMonAnAngi(final ImageView imageView, String linkHinhAnh){
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
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMonAnAngiModel.size();
    }


}

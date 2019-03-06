package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.R;
import vn.co.bpass.everyfood_btl.View.FullScreenImageActivity;
import vn.co.bpass.everyfood_btl.View.HienThiChiTietBinhLuanActivity;

/**
 * Created by VietDung-KMA-AT11D on 9/26/17.
 */

public class AdapterRecyclerHinhBinhLuan extends RecyclerView.Adapter<AdapterRecyclerHinhBinhLuan.ViewHolder> {
    Context context;
    int layout;
    List<Bitmap> listBitmapHinhBinhLuan;
    BinhLuanModel binhLuanModel;
    boolean isChiTietBinhLuan;


    public AdapterRecyclerHinhBinhLuan(Context context, int layout, List<Bitmap> listBitmapHinhBinhLuan, BinhLuanModel binhLuanModel, boolean isChiTietBinhLuan) {
        this.context = context;
        this.layout = layout;
        this.listBitmapHinhBinhLuan = listBitmapHinhBinhLuan;
        this.binhLuanModel = binhLuanModel;
        this.isChiTietBinhLuan = isChiTietBinhLuan;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewHinhBinhLuan;
        TextView txtSoHinhBinhLuan;
        FrameLayout frameLayoutKhungHinhBinhLuan;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageViewHinhBinhLuan = (ImageView) itemView.findViewById(R.id.imageViewHinhBinhLuan);
            this.txtSoHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtSoHinhBinhLuan);
            this.frameLayoutKhungHinhBinhLuan = (FrameLayout) itemView.findViewById(R.id.frameLayoutKhungHinhBinhLuan);
        }
    }

    @Override
    public AdapterRecyclerHinhBinhLuan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerHinhBinhLuan.ViewHolder holder, final int position) {
        final Bitmap bitmap = this.listBitmapHinhBinhLuan.get(position);
        holder.imageViewHinhBinhLuan.setImageBitmap(bitmap);


        if (isChiTietBinhLuan == false) {
            /* - 1 bình luận hiển thị ra 4 ảnh, được đánh index 0,1,2,3. Tại bức ảnh thứ 4, tức là index = 3, ta cho phép hiển thị
          FrameLayout chứa background mờ màu xám, dồng thời xét số ảnh còn lại có hay không, nếu có thì phải thêm đoạn text
          + (Số hình ảnh còn lại) */
            if (position == 3) {
                int soHinhConLai = this.listBitmapHinhBinhLuan.size() - 4;
                if (soHinhConLai > 0) {
                    holder.frameLayoutKhungHinhBinhLuan.setVisibility(View.VISIBLE);
                    holder.txtSoHinhBinhLuan.setText("+" + soHinhConLai);
                    holder.imageViewHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent iChiTietBinhLuan = new Intent(context, HienThiChiTietBinhLuanActivity.class);
                            iChiTietBinhLuan.putExtra("binhluanmodel", binhLuanModel);
                            context.startActivity(iChiTietBinhLuan);
                        }
                    });
                }

            }

            else{
                holder.imageViewHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("click","click");
                        Intent iFullScreenImage = new Intent(context, FullScreenImageActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                        byte[] byteArray = stream.toByteArray();
                        iFullScreenImage.putExtra("bytearrayanhbinhluan", byteArray);
                        context.startActivity(iFullScreenImage);
                    }
                });
            }
        }


        /* - Bắt sự kiện click vào ImageView imageViewHinhBinhLuan để xem ảnh Bình luận Full Screen. lúc này ta đã có dữ liệu
          Bitmap của từng ảnh, ta parse Bitmap sang byte, rồi truyền sang Activity mới thông qua Intent */
    }

    @Override
    public int getItemCount() {
        if (isChiTietBinhLuan == false) {
            if (this.listBitmapHinhBinhLuan.size() < 4) {
                return this.listBitmapHinhBinhLuan.size();
            } else {
                return 4; // Chỉ hiển thị ra 4 ảnh đối với mỗi bình luận
            }
        } else {
            return this.listBitmapHinhBinhLuan.size();
        }
    }

}

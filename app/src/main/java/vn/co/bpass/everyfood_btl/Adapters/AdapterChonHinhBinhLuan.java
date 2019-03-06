package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.List;

import vn.co.bpass.everyfood_btl.Model.ChonHinhBinhLuanModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/29/17.
 */

public class AdapterChonHinhBinhLuan extends RecyclerView.Adapter<AdapterChonHinhBinhLuan.ViewHolderChonHinhBinhLuan> {
    Context context;
    int layout;
    List<ChonHinhBinhLuanModel> listChonHinhBinhLuanModel;

    public AdapterChonHinhBinhLuan(Context context,int layout,List<ChonHinhBinhLuanModel> listChonHinhBinhLuanModel){
        this.context = context;
        this.layout = layout;
        this.listChonHinhBinhLuanModel = listChonHinhBinhLuanModel;
    }

    public class ViewHolderChonHinhBinhLuan extends RecyclerView.ViewHolder {
        ImageView imgChonHinhBinhLuan;
        CheckBox checkboxChonHinhBinhLuan;

        public ViewHolderChonHinhBinhLuan(View itemView) {
            super(itemView);

            this.imgChonHinhBinhLuan = (ImageView) itemView.findViewById(R.id.imgChonHinhBinhLuan);
            this.checkboxChonHinhBinhLuan = (CheckBox) itemView.findViewById(R.id.checkboxChonHinhBinhLuan);

        }
    }

    @Override
    public AdapterChonHinhBinhLuan.ViewHolderChonHinhBinhLuan onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderChonHinhBinhLuan viewHolderChonHinhBinhLuan = new ViewHolderChonHinhBinhLuan(view);
        return viewHolderChonHinhBinhLuan;
    }

    @Override
    public void onBindViewHolder(AdapterChonHinhBinhLuan.ViewHolderChonHinhBinhLuan holder, final int position) {

        final ChonHinhBinhLuanModel chonHinhBinhLuanModel = this.listChonHinhBinhLuanModel.get(position);
        Uri uriHinhAnh = Uri.parse(chonHinhBinhLuanModel.getDuongdan());
        holder.imgChonHinhBinhLuan.setImageURI(uriHinhAnh);
        holder.checkboxChonHinhBinhLuan.setChecked(chonHinhBinhLuanModel.isChecked());
        holder.checkboxChonHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                listChonHinhBinhLuanModel.get(position).setChecked(checkBox.isChecked());
            }
        });

//        try {
//            Bitmap imageBitMap = this.decodeUri(this.context,uriHinhAnh,100);
//            holder.imgChonHinhBinhLuan.setImageBitmap(imageBitMap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public int getItemCount() {
        return this.listChonHinhBinhLuanModel.size();
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }


}

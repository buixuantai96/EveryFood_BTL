package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/29/17.
 */

public class AdapterHienThiHinhBinhLuanDuocChon extends RecyclerView.Adapter<AdapterHienThiHinhBinhLuanDuocChon.ViewHolderHienThiHinhBinhLuanDuocChon> {
    Context context;
    int layout;
    List<String> listDuongDanHinhAnh;

    public AdapterHienThiHinhBinhLuanDuocChon(Context context, int layout, List<String> listDuongDanHinhAnh) {
        this.context = context;
        this.layout = layout;
        this.listDuongDanHinhAnh = listDuongDanHinhAnh;
    }

    public class ViewHolderHienThiHinhBinhLuanDuocChon extends RecyclerView.ViewHolder {
        ImageView imageView,imageViewXoa;

        public ViewHolderHienThiHinhBinhLuanDuocChon(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.imageViewXoa = (ImageView) itemView.findViewById(R.id.imageViewXoa);


        }
    }

    @Override
    public AdapterHienThiHinhBinhLuanDuocChon.ViewHolderHienThiHinhBinhLuanDuocChon onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderHienThiHinhBinhLuanDuocChon viewHolderHienThiHinhBinhLuanDuocChon = new ViewHolderHienThiHinhBinhLuanDuocChon(view);
        return  viewHolderHienThiHinhBinhLuanDuocChon;
    }

    @Override
    public void onBindViewHolder(final AdapterHienThiHinhBinhLuanDuocChon.ViewHolderHienThiHinhBinhLuanDuocChon holder, int position) {
        String duongDan = this.listDuongDanHinhAnh.get(position);
        Uri uri = Uri.parse(duongDan);
        holder.imageView.setImageURI(uri );

        holder.imageViewXoa.setTag(position);
        holder.imageViewXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viTri = (int) v.getTag();
                listDuongDanHinhAnh.remove(viTri);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listDuongDanHinhAnh.size();
    }


}

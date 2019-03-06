package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.bpass.everyfood_btl.Model.DatMon;
import vn.co.bpass.everyfood_btl.Model.MonAnModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.ViewHolderMonAn> {
    Context context;
    int layout;
    List<MonAnModel> listMonAnModel;

    public static List<DatMon> listDatMon = new ArrayList<>();


    public AdapterMonAn(Context context, int layout, List<MonAnModel> listMonAnModel) {
        this.context = context;
        this.layout = layout;
        this.listMonAnModel = listMonAnModel;

    }

    public class ViewHolderMonAn extends RecyclerView.ViewHolder {
        TextView txtTenMonAn,txtSoLuongMonAn;
        ImageView imgGiamSoLuong,imgTangSoLuong;

        public ViewHolderMonAn(View itemView) {
            super(itemView);

            this.txtTenMonAn = (TextView) itemView.findViewById(R.id.txtTenMonAn);
            this.txtSoLuongMonAn = (TextView) itemView.findViewById(R.id.txtSoLuongMonAn);
            this.imgGiamSoLuong = (ImageView) itemView.findViewById(R.id.imgGiamSoLuong);
            this.imgTangSoLuong = (ImageView) itemView.findViewById(R.id.imgTangSoLuong);


        }
    }

    @Override
    public ViewHolderMonAn onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderMonAn viewHolderMonAn = new ViewHolderMonAn(view);
        return viewHolderMonAn;
    }

    /* - CHÚ Ý : holder tượng trưng cho 1 dòng của RecyclerView
       - Tạo biến đếm dem ở trong onBindViewHolder, tức là mỗi 1 dòng của RecyclerView sẽ có 1 biến đếm riêng biệt */
    @Override
    public void onBindViewHolder(final ViewHolderMonAn holder, int position) {
        final MonAnModel monAnModel = this.listMonAnModel.get(position);
        holder.txtTenMonAn.setText("+ "+ monAnModel.getTenmon());

        holder.txtSoLuongMonAn.setTag(0);
        holder.imgTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuongMonAn.getTag().toString());
                dem++;
                holder.txtSoLuongMonAn.setText(dem+"");
                //Log.d("kiemtra",dem+"");
                holder.txtSoLuongMonAn.setTag(dem);

                // Remove đối tượng DatMon cũ đi
                DatMon datMonTag = (DatMon) holder.imgGiamSoLuong.getTag();
                if(datMonTag != null){
                    AdapterMonAn.listDatMon.remove(datMonTag);
                }

                // Add đối tượng DatMon mới vào trong mảng
                DatMon datMon = new DatMon();
                datMon.setTenmonan(monAnModel.getTenmon());
                datMon.setSoluong(dem);
                holder.imgGiamSoLuong.setTag(datMon);
                AdapterMonAn.listDatMon.add(datMon);

                for(DatMon datMon1:AdapterMonAn.listDatMon){
                    Log.d("kiemtra",datMon1.getTenmonan() + " - " + datMon1.getSoluong());
                }
                //Log.d("kiemtra",AdapterMonAn.listDatMon.size()+"");

            }
        });

        holder.imgGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuongMonAn.getTag().toString());
//                if(dem != 0){
//                    dem--;
//                    if(dem == 0){
//                        DatMon datMon = (DatMon) v.getTag();
//                        AdapterMonAn.listDatMon.remove(datMon);
//                        Log.d("kiemtra",AdapterMonAn.listDatMon.size()+"");
//                    }
//                }
//                holder.txtSoLuongMonAn.setText(dem+"");
//                //Log.d("kiemtra",dem+"");
//                holder.txtSoLuongMonAn.setTag(dem);
                if(dem > 0){
                    dem--;
                    holder.txtSoLuongMonAn.setText(dem+"");
                    holder.txtSoLuongMonAn.setTag(dem);

                    // Remove đối tượng DatMon cũ đi
                    DatMon datMonTag = (DatMon) holder.imgGiamSoLuong.getTag();
                    if(datMonTag != null){
                        AdapterMonAn.listDatMon.remove(datMonTag);
                    }

                    // Add đối tượng DatMon mới vào trong mảng
                    DatMon datMon = new DatMon();
                    datMon.setTenmonan(monAnModel.getTenmon());
                    datMon.setSoluong(dem);
                    holder.imgGiamSoLuong.setTag(datMon);
                    AdapterMonAn.listDatMon.add(datMon);
                    for(DatMon datMon1:AdapterMonAn.listDatMon){
                        Log.d("kiemtra",datMon1.getTenmonan() + " - " + datMon1.getSoluong());
                    }
                }
                else if(dem == 0){
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listMonAnModel.size();
    }


}

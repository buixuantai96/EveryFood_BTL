package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.co.bpass.everyfood_btl.Model.WifiQuanAnModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/27/17.
 */

public class AdapterDanhSachWifi extends RecyclerView.Adapter<AdapterDanhSachWifi.ViewHolderWifi> {
    Context context;
    int layout;
    List<WifiQuanAnModel> listWifiQuanAnModel;

    public  AdapterDanhSachWifi(Context context, int layout, List<WifiQuanAnModel> listWifiQuanAnModel){
        this.context = context;
        this.layout = layout;
        this.listWifiQuanAnModel = listWifiQuanAnModel;
    }

    public class ViewHolderWifi extends RecyclerView.ViewHolder {

        TextView txtTenWifi,txtMatKhauWifi,txtNgayDang;

        public ViewHolderWifi(View itemView) {
            super(itemView);

            this.txtTenWifi = (TextView) itemView.findViewById(R.id.txtTenWifi);
            this.txtMatKhauWifi = (TextView) itemView.findViewById(R.id.txtMatKhauWifi);
            this.txtNgayDang = (TextView) itemView.findViewById(R.id.txtNgayDangWifi);
        }
    }

    @Override
    public AdapterDanhSachWifi.ViewHolderWifi onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderWifi viewHolderWifi = new ViewHolderWifi(view);
        return viewHolderWifi;

    }

    @Override
    public void onBindViewHolder(AdapterDanhSachWifi.ViewHolderWifi holder, int position) {
        WifiQuanAnModel wifiQuanAnModel = this.listWifiQuanAnModel.get(position);
        holder.txtTenWifi.setText(wifiQuanAnModel.getTen());
        holder.txtMatKhauWifi.setText(wifiQuanAnModel.getMatkhau());
        holder.txtNgayDang.setText(wifiQuanAnModel.getNgaydang());
    }

    @Override
    public int getItemCount() {
        return this.listWifiQuanAnModel.size();
    }


}

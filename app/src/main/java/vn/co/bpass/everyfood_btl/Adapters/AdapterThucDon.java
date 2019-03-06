package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.co.bpass.everyfood_btl.Model.ThucDonModel;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class AdapterThucDon extends RecyclerView.Adapter<AdapterThucDon.ViewHolderThucDon> {

    Context context;
    int layout;
    List<ThucDonModel> listThucDonModel;

    public AdapterThucDon(Context context, int layout, List<ThucDonModel> listThucDonModel) {
        this.context = context;
        this.layout = layout;
        this.listThucDonModel = listThucDonModel;
    }

    public class ViewHolderThucDon extends RecyclerView.ViewHolder {
        TextView txtTenThucDon;
        RecyclerView recyclerMonAn;

        public ViewHolderThucDon(View itemView) {
            super(itemView);

            this.txtTenThucDon = (TextView) itemView.findViewById(R.id.txtTenThucDon);
            this.recyclerMonAn = (RecyclerView) itemView.findViewById(R.id.recyclerMonAn);
        }
    }

    @Override
    public ViewHolderThucDon onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ViewHolderThucDon viewHolderThucDon = new ViewHolderThucDon(view);
        return viewHolderThucDon;
    }

    @Override
    public void onBindViewHolder(ViewHolderThucDon holder, int position) {
        ThucDonModel thucDonModel = this.listThucDonModel.get(position);
        holder.txtTenThucDon.setText("- " + thucDonModel.getTenthucdon() + " : ");
        holder.recyclerMonAn.setLayoutManager(new LinearLayoutManager(context));
        AdapterMonAn adapterMonAn = new AdapterMonAn(this.context,R.layout.custom_layout_monan,thucDonModel.getListMonAn());
        holder.recyclerMonAn.setAdapter(adapterMonAn);
        adapterMonAn.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.listThucDonModel.size();
    }


}

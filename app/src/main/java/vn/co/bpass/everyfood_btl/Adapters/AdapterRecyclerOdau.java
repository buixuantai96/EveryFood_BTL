package vn.co.bpass.everyfood_btl.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.co.bpass.everyfood_btl.Model.BinhLuanModel;
import vn.co.bpass.everyfood_btl.Model.ChiNhanhQuanAnModel;
import vn.co.bpass.everyfood_btl.Model.QuanAnModel;
import vn.co.bpass.everyfood_btl.R;
import vn.co.bpass.everyfood_btl.View.ChiTietQuanAnActivity;

/**
 * Created by VietDung-KMA-AT11D on 9/23/17.
 */
// alt + enter => create class ViewHolder
public class AdapterRecyclerOdau extends RecyclerView.Adapter<AdapterRecyclerOdau.ViewHolder> {
    List<QuanAnModel> listQuanAnModel;
    int resource;
    Context context;

    public AdapterRecyclerOdau(Context context,List<QuanAnModel> listQuanAnModel, int resource) {
        this.listQuanAnModel = listQuanAnModel;
        this.resource = resource;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenQuanAnOdau,txtTieuDeBinhLuan,txtTieuDeBinhLuan2,txtNoiDungBinhLuan,txtNoiDungBinhLuan2;
        TextView txtChamDiemBinhLuan,txtChamDiemBinhLuan2,txtTongBinhLuan,txtTongHinhAnhBinhLuan,txtDiemTrungBinhQuanAn;
        TextView txtDiaChiQuanAnOdau,txtKhoangCachQuanAnOdau;
        Button btnDatMonOdau;
        ImageView imageHinhQuanAnOdau;
        CircleImageView circleImageUser,circleImageUser2;
        LinearLayout containerBinhLuan,containerBinhLuan2;
        CardView cardViewOdau;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtTenQuanAnOdau = (TextView) itemView.findViewById(R.id.txtTenQuanAnOdau);
            this.btnDatMonOdau = (Button) itemView.findViewById(R.id.btnDatMonOdau);
            this.imageHinhQuanAnOdau = (ImageView) itemView.findViewById(R.id.imageHinhQuanAnOdau);

            this.txtTieuDeBinhLuan = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan);
            this.txtTieuDeBinhLuan2 = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan2);
            this.txtNoiDungBinhLuan = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan);
            this.txtNoiDungBinhLuan2 = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan2);
            this.circleImageUser = (CircleImageView) itemView.findViewById(R.id.circleImageUser);
            this.circleImageUser2 = (CircleImageView) itemView.findViewById(R.id.circleImageUser2);
            this.containerBinhLuan = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan);
            this.containerBinhLuan2 = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan2);
            this.txtChamDiemBinhLuan = (TextView) itemView.findViewById(R.id.txtChamDiemBinhLuan);
            this.txtChamDiemBinhLuan2 = (TextView) itemView.findViewById(R.id.txtChamDiemBinhLuan2);
            this.txtTongBinhLuan = (TextView) itemView.findViewById(R.id.txtTongBinhLuan);
            this.txtTongHinhAnhBinhLuan = (TextView) itemView.findViewById(R.id.txtTongHinhAnhBinhLuan);
            this.txtDiemTrungBinhQuanAn = (TextView) itemView.findViewById(R.id.txtDiemTrungBinhQuanAn);

            this.txtDiaChiQuanAnOdau = (TextView) itemView.findViewById(R.id.txtDiaChiQuanAnOdau);
            this.txtKhoangCachQuanAnOdau = (TextView) itemView.findViewById(R.id.txtKhoangCachQuanAnOdau);

            this.cardViewOdau = (CardView) itemView.findViewById(R.id.cardViewOdau);
        }
    }


    // Khởi tạo giao diện
    @Override
    public AdapterRecyclerOdau.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resource,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Gán dữ liệu lên giao diện
    @Override
    public void onBindViewHolder(final AdapterRecyclerOdau.ViewHolder holder, int position) {
        // Set giao diện
        final QuanAnModel quanAnModel = this.listQuanAnModel.get(position);

        holder.txtTenQuanAnOdau.setText(quanAnModel.getTenquanan());
        if(quanAnModel.isGiaohang() == true){
            holder.btnDatMonOdau.setVisibility(View.VISIBLE);
        }

        /* - Kiểm tra Quán Ăn có hình ảnh hay không, nếu có thì Download hình ảnh quán ăn từ Storage về */
        if(quanAnModel.getListBitMap().size() > 0){
            holder.imageHinhQuanAnOdau.setImageBitmap(quanAnModel.getListBitMap().get(0));
        }

        /* - Load dữ liệu bình luận của Quán Ăn từ Storage về client
           - Tới đoạn code này, đã lấy được tất cả các đối tượng Bình Luận của Quán Ăn */
        if(quanAnModel.getListBinhLuanModel().size() > 0){ // Kiểm tra xem Quán Ăn có bình luận hay không ?
            holder.containerBinhLuan.setVisibility(View.VISIBLE);
            BinhLuanModel binhLuanModel = quanAnModel.getListBinhLuanModel().get(0);
            holder.txtTieuDeBinhLuan.setText(binhLuanModel.getTieude()); // Set tiêu đề của Bình Luận
            holder.txtNoiDungBinhLuan.setText(binhLuanModel.getNoidung()); // Set nội dung của Bình Luận
            holder.txtChamDiemBinhLuan.setText(binhLuanModel.getChamdiem()+"");
            holder.circleImageUser.setImageResource(R.drawable.user);
            //this.setHinhAnhBinhLuan(holder.circleImageUser,binhLuanModel.getThanhVienModel().getHinhanh()); // Set hình ảnh của Bình Luận
            if(quanAnModel.getListBinhLuanModel().size() >= 2){
                holder.containerBinhLuan2.setVisibility(View.VISIBLE);
                BinhLuanModel binhLuanModel2 = quanAnModel.getListBinhLuanModel().get(1);
                holder.txtTieuDeBinhLuan2.setText(binhLuanModel2.getTieude());
                holder.txtNoiDungBinhLuan2.setText(binhLuanModel2.getNoidung());
                holder.txtChamDiemBinhLuan2.setText(binhLuanModel2.getChamdiem()+"");
//                if(binhLuanModel2.getThanhVienModel().getHinhanh() != null) {
//                    this.setHinhAnhBinhLuan(holder.circleImageUser2,binhLuanModel.getThanhVienModel().getHinhanh());
//                } else {
//                    holder.circleImageUser2.setImageResource(R.drawable.user);
//                }
                //this.setHinhAnhBinhLuan(holder.circleImageUser2,binhLuanModel2.getThanhVienModel().getHinhanh());

                holder.circleImageUser2.setImageResource(R.drawable.user);
            }



            // Set số lượng bình luận của 1 Quán Ăn
            holder.txtTongBinhLuan.setText(quanAnModel.getListBinhLuanModel().size()+"");

            int tongSoAnhCuaCacBinhLuanCuaMotQuanAn = 0;
            double tongDiem = 0;
            /* - Duyệt qua từng đối tượng Bình Luận, mỗi đối tượng Bình Luận lại có nhiều Hình Ảnh Bình Luận
               - Như vậy có thể hiểu là, 1 Bình Luận của 1 Quán Ăn thì có NHIỀU Hình Ảnh Bình Luận
               => Đếm tổng số ẢNH BÌNH LUẬN của các Bình Luận bên trong 1 Quán Ăn
               - Tính tổng số điểm của các bình luận trong 1 Quán Ăn */
            for(BinhLuanModel binhLuanModel1:quanAnModel.getListBinhLuanModel()){
                tongSoAnhCuaCacBinhLuanCuaMotQuanAn += binhLuanModel1.getListHinhAnhBinhLuan().size();
                tongDiem += binhLuanModel1.getChamdiem();
            }
            // Tính tổng điểm Trung Bình của bình luận
            double diemTrungBinhCuaQuanAn = tongDiem / quanAnModel.getListBinhLuanModel().size();
            holder.txtDiemTrungBinhQuanAn.setText(String.format("%.2f",diemTrungBinhCuaQuanAn));

            if(tongSoAnhCuaCacBinhLuanCuaMotQuanAn > 0){
                holder.txtTongHinhAnhBinhLuan.setText(tongSoAnhCuaCacBinhLuanCuaMotQuanAn+"");
            } else {
                holder.txtTongHinhAnhBinhLuan.setText("0");
            }

        }else{
//            holder.containerBinhLuan.setVisibility(View.GONE);
//            holder.containerBinhLuan2.setVisibility(View.GONE);
            holder.txtTongBinhLuan.setText("0");
            holder.txtTongHinhAnhBinhLuan.setText("0");
        }

        // Lấy ra đối tượng Chi Nhánh Quán Ăn, hiển thị địa chỉ gần nhất, và số km để tới địa chỉ đó
        if(quanAnModel.getListChiNhanhQuanAnModel().size() > 0){ // Quán Ăn có nhiều địa chỉ
            // Biến tạm. luôn giữ khoảng cách tới địa chỉ đầu tiên trong mảng các địa chỉ quán ăn của 1 Quán Ăn
            // Biến tạm sau khi kết thúc vòng lặp so sánh, sẽ chứa giá trị khoảng cách nhỏ nhất
            //double khoangCachTam = quanAnModel.getListChiNhanhQuanAnModel().get(0).getKhoangcach();
            ChiNhanhQuanAnModel chiNhanhQuanAnModelTam = quanAnModel.getListChiNhanhQuanAnModel().get(0);
            for(ChiNhanhQuanAnModel chiNhanhQuanAnModel:quanAnModel.getListChiNhanhQuanAnModel()){
                if(chiNhanhQuanAnModelTam.getKhoangcach() > chiNhanhQuanAnModel.getKhoangcach()){
                    chiNhanhQuanAnModelTam = chiNhanhQuanAnModel;
                }
            }
            holder.txtDiaChiQuanAnOdau.setText(chiNhanhQuanAnModelTam.getDiachi());
            holder.txtKhoangCachQuanAnOdau.setText(String.format("%.1f",chiNhanhQuanAnModelTam.getKhoangcach()) + " km");
        }

        holder.cardViewOdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iChiTietQuanAn = new Intent(context, ChiTietQuanAnActivity.class);
                iChiTietQuanAn.putExtra("quanan",quanAnModel);
                context.startActivity(iChiTietQuanAn);
                //Log.d("test","aaaa");
            }
        });
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

    @Override
    public int getItemCount() {
        return this.listQuanAnModel.size();

    }


}

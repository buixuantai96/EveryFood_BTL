package vn.co.bpass.everyfood_btl.View.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.co.bpass.everyfood_btl.Controller.AngiController;
import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/22/17.
 */

public class AnGiFragment extends Fragment{
    AngiController angiController;
    RecyclerView recyclerAnGi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* - Khởi tạo giao diện cho Fragment */
        View layout = inflater.inflate(R.layout.layout_fragment_angi,container,false);

        this.recyclerAnGi = (RecyclerView) layout.findViewById(R.id.recyclerAnGi);

        this.angiController = new AngiController(this.getContext());
        angiController.getDanhSachMonAnAnGi(this.recyclerAnGi);

        return layout;
    }
}

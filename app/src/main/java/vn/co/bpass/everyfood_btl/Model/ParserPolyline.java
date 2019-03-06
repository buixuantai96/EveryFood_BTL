package vn.co.bpass.everyfood_btl.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

public class ParserPolyline {

    public ParserPolyline(){

    }

    public List<LatLng> layDanhSachToaDoDanDuong(String dataJSON){
        List<LatLng> listLatLng = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(dataJSON);
            JSONArray routes = jsonObject.getJSONArray("routes");
            for(int i=0;i<routes.length();i++){
                JSONObject jsonObjectOfArray = routes.getJSONObject(i);
                JSONObject overview_polyline = jsonObjectOfArray.getJSONObject("overview_polyline");
                String points = overview_polyline.getString("points");
                //Log.d("kiemtraparseJSON",points+"");
                listLatLng =  PolyUtil.decode(points);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listLatLng;
    }
}

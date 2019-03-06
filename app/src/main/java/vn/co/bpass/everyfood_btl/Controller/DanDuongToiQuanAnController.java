package vn.co.bpass.everyfood_btl.Controller;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import vn.co.bpass.everyfood_btl.Model.DownloadPolyLineJSON;
import vn.co.bpass.everyfood_btl.Model.ParserPolyline;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

public class DanDuongToiQuanAnController {
    ParserPolyline parserPolyline;
    DownloadPolyLineJSON downloadPolyLineJSON;

    public DanDuongToiQuanAnController(){
        this.parserPolyline = new ParserPolyline();
        this.downloadPolyLineJSON = new DownloadPolyLineJSON();
    }

    public void hienThiDanDuongToiQuanAn(GoogleMap googleMap,String url){


        this.downloadPolyLineJSON.execute(url);
        try {
            /* - Hàm get() sẽ dừng tất cả các tiến trình đang chạy, tuy nhiên ở DanDuongToiQuanAnActivity ta chỉ load duy nhất 1
             cái Google Map, ngoài ra không chạy ngầm tiến trình song song nào, do vậy có thể sử dụng get() */
            String dataJSON = this.downloadPolyLineJSON.get();
            //Log.d("kiemtra23123123123123",dataJSON);
            List<LatLng> latLngList = this.parserPolyline.layDanhSachToaDoDanDuong(dataJSON);
            PolylineOptions polylineOptions = new PolylineOptions();
            for(LatLng toaDo:latLngList){
                polylineOptions.add(toaDo);
            }
            Polyline polyline = googleMap.addPolyline(polylineOptions);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

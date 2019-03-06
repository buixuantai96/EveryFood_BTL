package vn.co.bpass.everyfood_btl.Model;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by VietDung-KMA-AT11D on 9/28/17.
 */

/* - AsyncTask :
     + Đối số 1 : URL
     + Đối số 2 : đối số update, không cần
     + Đối số 3 : Kết quả trả về,Value chuỗi mã hóa của Key "points" nằm trong chuỗi JSON ta đang muốn đọc */
public class DownloadPolyLineJSON extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... params) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); // Mở kết nối
            httpURLConnection.connect(); // Thực hiện kết nối ( Kết nối lên )
            // Tạo luồng đọc dữ liệu, lúc này ta đã có được dữ liệu của trang web rồi
            InputStream inputStream = httpURLConnection.getInputStream();
            // Bắt đầu đọc dữ liệu của URL
            InputStreamReader reader = new InputStreamReader(inputStream);
            // Bộ đệm giúp đọc dữ liệu chuỗi mượt mà hơn
            BufferedReader bufferedReader = new BufferedReader(reader); // Chứa các chuỗi nằm trong nội dung của URL
            String line = "";
            // Chứng nào vẫn còn dòng để đọc
            while( (line = bufferedReader.readLine()) != null ){
                stringBuffer.append(line);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("kiemtra",stringBuffer.toString());
        return stringBuffer.toString();
    }
    @Override
    protected void onPostExecute(String dataJSON) {
        super.onPostExecute(dataJSON);
    }
}

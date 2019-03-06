package vn.co.bpass.everyfood_btl.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import vn.co.bpass.everyfood_btl.R;

/**
 * Created by VietDung-KMA-AT11D on 9/30/17.
 */

public class FullScreenImageActivity extends AppCompatActivity {
    ImageView imgFullScreenImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fullscreen_image);

        this.imgFullScreenImage = (ImageView) findViewById(R.id.imgFullScreenImage);

        Intent iFullScreenImage = getIntent();
        byte[] byteArray = iFullScreenImage.getByteArrayExtra("bytearrayanhbinhluan");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgFullScreenImage.setImageBitmap(bmp);

    }
}

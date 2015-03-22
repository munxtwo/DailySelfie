package com.ktay.DailySelfie;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageFullViewActivity extends Activity {

    private static final String TAG = "ImageFullViewActivity";
    public static final String IMAGE_URI = "IMAGE_URI";

    private Uri mImageUri;
    private ImageView mImageView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_full_view);
        mImageView = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mImageUri = (Uri) bundle.get(IMAGE_URI);
            displayImage();
        }
    }

    private void displayImage() {
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setImageURI(mImageUri);
    }
}
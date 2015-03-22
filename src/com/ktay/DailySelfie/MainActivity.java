package com.ktay.DailySelfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;
import com.ktay.DailySelfie.util.CameraUtil;

import java.io.File;

public class MainActivity extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQ_CODE = 100;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                dispatchCaptureImageIntent();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                mImageView.setImageBitmap(imageBitmap);
            }
        }
    }

    private void dispatchCaptureImageIntent() {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = CameraUtil.getOutputMediaFile(CameraUtil.MEDIA_TYPE_IMAGE);

            if (imageFile == null) {
                Toast.makeText(this, R.string.error_unable_to_access_storage, Toast.LENGTH_SHORT);
            } else {
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, CameraUtil.getOutputMediaFileUri(imageFile));
                startActivityForResult(captureImageIntent, CAPTURE_IMAGE_ACTIVITY_REQ_CODE);
            }
        }
    }
}

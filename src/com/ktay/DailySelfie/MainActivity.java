package com.ktay.DailySelfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String DIR_NAME = "DailySelfie";
    private static final long ALARM_INTERVAL_TWO_MIN = 120000L;

    private File mMediaStorageDir;
    private List<File> mImageList;
    private ImageAdapter mImageAdapter;
    private File mCurrentImageFile;
    private AlarmManager mAlarmManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initMediaStorageDir();

        if (mMediaStorageDir == null) {
            return;
        }

        mImageList = new LinkedList<File>(Arrays.asList(mMediaStorageDir.listFiles()));
        GridView gridView = (GridView) findViewById(R.id.gridView);
        mImageAdapter = new ImageAdapter(this, mImageList);
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewFullImageIntent = new Intent(MainActivity.this, ImageFullViewActivity.class);
                viewFullImageIntent.putExtra(ImageFullViewActivity.IMAGE_URI, Uri.fromFile((File) mImageAdapter
                        .getItem(position)));

                startActivity(viewFullImageIntent);
            }
        });

        setupAlarm();

    }

    private void setupAlarm() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notidicationReceiverIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
        PendingIntent notificationReceiverPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notidicationReceiverIntent,
                0);
        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, ALARM_INTERVAL_TWO_MIN, ALARM_INTERVAL_TWO_MIN,
                notificationReceiverPendingIntent);
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
                mImageList.add(mCurrentImageFile);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void dispatchCaptureImageIntent() {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            mCurrentImageFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (mCurrentImageFile == null) {
                Toast.makeText(this, R.string.error_unable_to_access_storage, Toast.LENGTH_SHORT).show();
            } else {
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri(mCurrentImageFile));
                startActivityForResult(captureImageIntent, CAPTURE_IMAGE_ACTIVITY_REQ_CODE);
            }
        }
    }

    private File getOutputMediaFile(int type){
        // Create the storage directory if it does not exist
        if (mMediaStorageDir == null){
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mMediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private Uri getOutputMediaFileUri(File file) {
        return Uri.fromFile(file);
    }

    private void initMediaStorageDir() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.e(TAG, "media storage is not mounted");
            Toast.makeText(this, R.string.error_media_storage_not_mounted, Toast.LENGTH_SHORT).show();
            mMediaStorageDir = null;
        }

        mMediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mMediaStorageDir.exists()){
            if (!mMediaStorageDir.mkdirs()){
                Log.e(TAG, "failed to create directory");
                Toast.makeText(this, R.string.error_failed_to_create_dir, Toast.LENGTH_SHORT).show();

                mMediaStorageDir = null;
            }
        }
    }
}

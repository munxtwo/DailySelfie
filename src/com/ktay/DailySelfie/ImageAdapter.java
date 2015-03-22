package com.ktay.DailySelfie;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<File> mImageList;

    public ImageAdapter(Context context, List<File> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(((GridView) parent).getColumnWidth(), ((GridView) parent).getColumnWidth()));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(Uri.fromFile((File) getItem(position)));

        return imageView;
    }
}
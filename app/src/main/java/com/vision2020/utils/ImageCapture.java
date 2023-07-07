package com.vision2020.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vision2020.R;

import java.io.File;
import java.io.IOException;

public class ImageCapture {

    Activity mActivity;
    ImageResponse mImageResponse;
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "profile_photo.jpg";

    public ImageCapture(final Activity mainActivity, ImageResponse imageResponse, boolean isProgressPics) {
        this.mActivity = mainActivity;
        this.mImageResponse = imageResponse;

        // Setting filepath on storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(isProgressPics ? progressPicsPath() : userImagesPath(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(isProgressPics ? progressPicsPath() :userImagesPath(), TEMP_PHOTO_FILE_NAME);
        }

        View view = mainActivity.getLayoutInflater().inflate(R.layout.activity_select_image, null);
        Button button_choose_image_camera = view.findViewById(R.id.btnretrive);
        Button button_choose_image_gallery =  view.findViewById(R.id.btncustomedit);
        //ImageView button_cancel_image_chooser =  view.findViewById(R.id.button_cancel_image_chooser);
        final Dialog mBottomChooserDialog = new Dialog(mainActivity);//, R.style.DialogTheme
        mBottomChooserDialog.setContentView(view);
        mBottomChooserDialog.setCancelable(false);
        mBottomChooserDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomChooserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomChooserDialog.show();

        button_choose_image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageResponse.cameraImage(mFileTemp);
                mBottomChooserDialog.dismiss();
            }
        });

        button_choose_image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomChooserDialog.dismiss();
                mImageResponse.galleryImage(mFileTemp);
            }
        });

    }
    public static final String appStorageDirProfileImage = Environment.getExternalStorageDirectory()
            + "/vision/image/";
    public static final String appStorageDirProgressPics = Environment.getExternalStorageDirectory()
            + "/vision/image/ProgressPics/";
    public static File progressPicsPath() {
        File fileProfileImage = new File(appStorageDirProgressPics);
        if (!fileProfileImage.exists()) {
            fileProfileImage.mkdirs();
            File file = new File(fileProfileImage.getAbsoluteFile() + "/.NOMEDIA");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileProfileImage;
    }

    public static File userImagesPath() {
        File fileProfileImage = new File(appStorageDirProfileImage);
        if (!fileProfileImage.exists()) {
            fileProfileImage.mkdirs();
            File file = new File(fileProfileImage.getAbsoluteFile() + "/.NOMEDIA");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileProfileImage;
    }

}
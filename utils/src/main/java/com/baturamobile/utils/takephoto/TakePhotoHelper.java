package com.baturamobile.utils.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vssnake on 08/09/2017.
 */

public class TakePhotoHelper {



    public TakePhotoHelper(Activity activity){
        this.mActivity = activity;
    }


    private Activity mActivity;


    String mCurrentPhotoPath;


    public static final int REQUEST_TAKE_PHOTO = 30;


    private File createImageFile(String directory) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + directory;
        File storageDir = new File(storageDirPath);
        if (!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath ="file:" + image.getAbsolutePath();
        return image;
    }



    public  void dispatchTakePublicPictureIntent(String directory,String authorityProvider) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(directory);
            } catch (IOException ex) {
                // Error occurred while creating the File
                throw  ex;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
             //   Uri photoURI = Uri.parse(photoFile.getAbsolutePath() + "/ " + directory);
               Uri photoURI = FileProvider.getUriForFile(mActivity,
                       authorityProvider,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public Uri getImageUri() {
        return Uri.parse(mCurrentPhotoPath);
    }
}

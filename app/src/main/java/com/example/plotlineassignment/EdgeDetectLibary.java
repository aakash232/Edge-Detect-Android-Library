package com.example.plotlineassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EdgeDetectLibary extends AppCompatActivity {

    public database db;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Bitmap edgeLib_detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);

        return resultBitmap;
    }

    public void edgeLib_init(Context context){
        //initialize openCV library
        OpenCVLoader.initDebug();
        edgeLib_askPermission(context);
        //Database object
        db = new database(context);
    }

    public boolean edgeLib_storeImage(Context context, Bitmap bitmap, Bitmap resultBitmap) {

        //using date and time as NAME for storing in DB
        Date date = Calendar.getInstance().getTime();
        String name = date.toString();

        //converting bitmaps to byte array
        ByteArrayOutputStream orgByteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, orgByteArray);
        byte[] org_img = orgByteArray.toByteArray();

        ByteArrayOutputStream resByteArray = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, resByteArray);
        byte[] res_img = resByteArray.toByteArray();

        //Inserting data in database
        if(db.insertdata(name, org_img, res_img)){
            Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT).show();
            //Fetching NAME from database
            Toast.makeText(context, "NAME: "+db.getName(name), Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            Toast.makeText(context, "Data not saved", Toast.LENGTH_SHORT).show();

        return false;
    }

    public Pair<Intent,Integer> edgeLib_imagePicker(Context context) {

        final CharSequence[][] options = {{"Take Photo", "Choose from Gallery", "Cancel"}};
        final Pair<Intent, Integer>[] choice = new Pair[]{new Pair<Intent, Integer>(null, -99)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");

        builder.setItems(options[0], (dialog, item) -> {

            if (options[0][item].equals("Take Photo"))
            {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 1);
                choice[0] = new Pair<Intent,Integer>(takePicture,1);
            }
            else if (options[0][item].equals("Choose from Gallery"))
            {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 2);
                choice[0] = new Pair<Intent,Integer>(gallery,1);
            }
            else if (options[0][item].equals("Cancel")) {
                dialog.dismiss();
            }

        });
        builder.show();

        return choice[0];
    }

    public Bitmap edgeLib_getBitmapByURL(Context context, String url) {

        final boolean[] flag = {false};

        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap=resource;
                        flag[0] =true;
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(context,"No image loaded",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        if(flag[0])
            return bitmap;
        else
            return null;
    }

    public void edgeLib_askPermission(Context context) {
        Dexter.withContext(context).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }

        }).check();
    }

}
package com.example.a4k8kwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullScreenImageActivity extends AppCompatActivity {
    private ImageView fullScreenImageView;
    private Button applyButton, backButton;
    private  ProgressBar progressBar;
    private String selectedImageResId;

    public static void start(Context context, String imageResId) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra("image_res_id", imageResId);
        context.startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // hide mobile key button

        Window window = getWindow();
        // Set the status bar color
        window.setStatusBarColor(getResources().getColor(R.color.colortransparent));
        fullScreenImageView = findViewById(R.id.full_screen_image_view);
        applyButton = findViewById(R.id.apply_button);
        backButton = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progress_bar);

        selectedImageResId = getIntent().getStringExtra("image_res_id");

//        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(selectedImageResId)
                .thumbnail(0.4F)
                .into(fullScreenImageView);


        applyButton.setOnClickListener(v -> showWallpaperDialog());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenImageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }); applyButton.setOnClickListener(v -> showWallpaperDialog());
//        fullScreenImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }


    private void showWallpaperDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Wallpaper")
                .setItems(new String[]{"Lock screen wallpaper", "Home screen wallpaper", "Both", "Cancel"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                        case 1:
                        case 2:
                            applyButton.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                                setWallpaper(which);

                            break;
                        case 3:
                            dialog.dismiss();
//                            applyButton.setVisibility(View.GONE);
                            break;
                    }
                }).show();
    }

    private void setWallpaper(int option) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
//        Bitmap bitmap = getBitmapFromURL(selectedImageResId);
        new DownloadImageTask() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (option == 0) {
                                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                            } else if (option == 1) {
                                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                            } else if (option == 2) {
                                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                            }
                        } else {
                            wallpaperManager.setBitmap(bitmap);
                        }
                        saveImageToGallery(bitmap);
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
//                        Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute(selectedImageResId);

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            return getBitmapFromURL(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Wallpaper");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image set as wallpaper");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}
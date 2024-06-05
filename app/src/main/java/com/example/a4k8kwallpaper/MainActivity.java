package com.example.a4k8kwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private boolean isLoading = false;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // hide mobile key button

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        List<Integer> initialImageList = getImages(currentPage);
        List<Wallpaper> wallpapers =getWallpapersFromJson(this);
        imageAdapter = new ImageAdapter(this,wallpapers, this::onImageClick);
        recyclerView.setAdapter(imageAdapter);

        Window window = getWindow();

        // Set the status bar color
        window.setStatusBarColor(getResources().getColor(R.color.colortransparent));
    }

    private void onImageClick(String imageResId) {
        FullScreenImageActivity.start(this, imageResId);
    }

    private List<Integer> getImages(int page) {

        List<Integer> images = new ArrayList<>();
        int start = (page - 1) * PAGE_SIZE;
        for (int i = start; i < start + PAGE_SIZE && i < allImages.size(); i++) {
            images.add(allImages.get(i));
        }
        return images;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("wallpapers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public List<Wallpaper> getWallpapersFromJson(Context context) {
        List<Wallpaper> wallpaperList = new ArrayList<>();
        try {
            String jsonStr = loadJSONFromAsset(context);
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray wallpapers = jsonObj.getJSONArray("Wallpapers");

                for (int i = 0; i < wallpapers.length(); i++) {
                    JSONObject w = wallpapers.getJSONObject(i);
                    int no = w.getInt("no");
                    String imageToUse = w.getString("imageToUse");
                    String imageToShow = w.getString("imageToShow");
                    Wallpaper wallpaper = new Wallpaper(no, imageToUse, imageToShow);
                    wallpaperList.add(wallpaper);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wallpaperList;
    }

    private List<Integer> allImages = Arrays.asList(

    );
}

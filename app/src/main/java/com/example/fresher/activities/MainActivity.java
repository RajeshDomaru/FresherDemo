package com.example.fresher.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.fresher.BuildConfig;
import com.example.fresher.R;
import com.example.fresher.fragments.CustomViewFragment;
import com.example.fresher.fragments.DashboardFragment;
import com.example.fresher.fragments.SignUpFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;

    private NavigationView nvMain;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private AppCompatImageView ivProfile;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        loadPage();

        setOnClickListener();

        getFirebaseToken();

    }

    private void getFirebaseToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                String token = task.getResult();

                Log.e("TokenResult", token);

            } else {

                Log.w("TokenException", task.getException());

            }

        });

    }

    private void init() {

        Toolbar toolBar = findViewById(R.id.toolBar);

        setSupportActionBar(toolBar);

        nvMain = findViewById(R.id.nvMain);

        drawerlayout = findViewById(R.id.drawerlayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolBar, R.string.open, R.string.close);

        drawerlayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }

    private void loadPage() {

        SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);

        String email = sharedPreferences.getString("Email", null);
        String name = sharedPreferences.getString("Name", null);

        if (email == null && name == null) {

            gotoSignUpPage();

        } else {

            loadHeader(email, name);

            gotoDashboard();

        }

    }

    private void loadHeader(String email, String name) {

        View headerView = nvMain.getHeaderView(0);

        AppCompatTextView tvContentName = headerView.findViewById(R.id.tvContentName);

        AppCompatTextView tvEmail = headerView.findViewById(R.id.tvEmail);

        ivProfile = headerView.findViewById(R.id.ivProfile);

        Bitmap bitmap = getProfilePicture();

        if (bitmap != null)

            ivProfile.setImageBitmap(bitmap);

        tvContentName.setText(name);

        tvEmail.setText(email);

        ivProfile.setOnClickListener(v -> {

            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        cameraPermissionActivityResultLauncher.launch(Manifest.permission.CAMERA);

                    } else {

                        openCamera();

                    }

                }

            } catch (Exception exception) {

                exception.printStackTrace();

            }

        });

    }

    private final ActivityResultLauncher<String> cameraPermissionActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {

                if (result) {

                    openCamera();

                }

            });

    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == RESULT_OK) {

                    addProfilePicture();

                    if (ivProfile != null) {

                        Bitmap bitmap = getProfilePicture();

                        if (bitmap != null)

                            ivProfile.setImageBitmap(bitmap);

                    }

                }

            });

    private void openCamera() {

        try {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File photoFile = createImageFile();

            Uri photoURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            activityResultLauncher.launch(intent); // startActivityForResult(intent, 2342);

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

    private File createImageFile() throws Exception {

        String imageFileName = "Profile";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",   /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getPath();

        return image;

    }

    private void addProfilePicture() {

        SharedPreferences.Editor editor = getSharedPreferences("ProfilePicture", 0).edit();

        editor.putString("currentPhotoPath", currentPhotoPath);

        editor.apply();

    }

    private Bitmap getProfilePicture() {

        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePicture", 0);

        String photoPath = sharedPreferences.getString("currentPhotoPath", null);

        return BitmapFactory.decodeFile(photoPath);

    }

    @SuppressLint("NonConstantResourceId")
    private void setOnClickListener() {

        nvMain.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.home:

                    gotoDashboard();

                    break;

                case R.id.map:

                    gotoMapActivity();

                    break;

                case R.id.customView:

                    gotoCustom();

                    break;

                case R.id.logout:

                    showAlertDialog();

                    break;

            }

            drawerlayout.closeDrawer(GravityCompat.START);

            return false;

        });

    }

    private void gotoMapActivity() {

        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);

    }


    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("You want to logout?");

        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {

            logOut();

            gotoSignUpPage();

        });

        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void logOut() {

        SharedPreferences.Editor editor = getSharedPreferences("Details", 0).edit();

        editor.putString("Email", null);
        editor.putString("Name", null);

        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle != null && actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {

            drawerlayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }

    private void gotoSignUpPage() {

        Fragment fragment = new SignUpFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();

    }

    private void gotoDashboard() {

        Fragment fragment = new DashboardFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, getApplicationContext().getResources().getString(R.string.dashboard))
                .commit();

    }

    private void gotoCustom() {

        Fragment fragment = new CustomViewFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, getApplicationContext().getResources().getString(R.string.custom_view))
                .addToBackStack(getApplicationContext().getResources().getString(R.string.dashboard))
                .commit();

    }

}
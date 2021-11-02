package com.example.fresher.utils;

import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class FragmentPermissionHelper {

    public void startPermissionRequest(
            FragmentActivity fragmentActivity,
            FragmentPermissionInterface fragmentPermissionInterface,
            String manifest
    ) {

        ActivityResultLauncher<String> requestActivityResultLauncher =
                fragmentActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {

                    if (result) {

                        Log.d("Isss", "True");

                    } else {

                        Log.d("Isss", "False");

                    }

                });

        requestActivityResultLauncher.launch(manifest);

    }

}

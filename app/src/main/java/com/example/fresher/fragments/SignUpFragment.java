package com.example.fresher.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.fresher.R;

public class SignUpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View mainView;

    private EditText email_id, name_id;

    private Button button_save;

    public SignUpFragment() {
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        init();

        setOnClickListener();

        return mainView;

    }

    private void init() {

        email_id = mainView.findViewById(R.id.email_id);

        name_id = mainView.findViewById(R.id.name_id);

        button_save = mainView.findViewById(R.id.button_save);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolBar);

        toolbar.setVisibility(View.GONE);

    }

    private void setOnClickListener() {

        button_save.setOnClickListener(view -> {

            save();

            goToSecondPage();

        });

    }

    private void save() {
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Details", 0).edit();

        String email = email_id.getText().toString();
        String name = name_id.getText().toString();

        editor.putString("Email", email);
        editor.putString("Name", name);

        editor.apply();

    }

    private void goToSecondPage() {

        Fragment fragment = new DashboardFragment();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, requireContext().getResources().getString(R.string.dashboard))
                .addToBackStack(null)
                .commit();

    }

}
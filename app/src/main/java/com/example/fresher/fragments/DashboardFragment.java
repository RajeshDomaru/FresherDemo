package com.example.fresher.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fresher.R;

import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View mainView;

    private CardView cvExpenses, cvThread;

    private ProgressBar pbThread;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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

        mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init();

        setOnClickListener();

        return mainView;

    }

    private void init() {

        Toolbar toolbar = requireActivity().findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(requireContext().getResources().getString(R.string.dashboard));

        cvExpenses = mainView.findViewById(R.id.cvExpenses);

        cvThread = mainView.findViewById(R.id.cvThread);

        pbThread = mainView.findViewById(R.id.pbThread);

    }

    private void setOnClickListener() {

        cvExpenses.setOnClickListener(v -> {

            Fragment fragment = new ExpensesListFragment();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragment, requireContext().getResources().getString(R.string.expenses_list))
                    .addToBackStack(requireContext().getResources().getString(R.string.dashboard))
                    .commit();
        });

//        cvThread.setOnClickListener(v -> invokeThread());

        /*cvThread.setOnClickListener(v -> {

            NewAsyncTack newAsyncTack = new NewAsyncTack();

            newAsyncTack.execute("Parameter1", "Parameter2");

        });*/

        cvThread.setOnClickListener(v -> {

            invokeExecutorService();

        });

    }

    private void invokeExecutorService() {

        pbThread.setVisibility(View.VISIBLE);

        pbThread.setProgress(0);

        Executors.newSingleThreadExecutor().execute(() -> {

            for (int i = 1; i <= 5; i++) {

                try {

                    Thread.sleep(1000);

                    pbThread.setProgress(i);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            new Handler(Looper.getMainLooper()).post(() -> {

                pbThread.setVisibility(View.GONE);

            });

        });

    }

    private void invokeThread() {

        pbThread.setVisibility(View.VISIBLE);

        pbThread.setProgress(0);

        new Thread(() -> {

            for (int i = 1; i <= 5; i++) {

                try {

                    Thread.sleep(1000);

                    pbThread.setProgress(i);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            new Handler(Looper.getMainLooper()).post(() -> {

                pbThread.setVisibility(View.GONE);

            });

        }).start();

    }

    private class NewAsyncTack extends AsyncTask<String, Integer, Boolean> {

        public NewAsyncTack() {
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pbThread.setVisibility(View.VISIBLE);

            pbThread.setProgress(0);

        }

        @Override
        protected Boolean doInBackground(String... strings) {

            // strings[0] = Parameter1, strings[1] = Parameter2

            for (int i = 0; i <= 5; i++) {

                try {

                    Thread.sleep(1000);

                    publishProgress(i);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            if (values.length > 0)

                pbThread.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            super.onPostExecute(aBoolean);

            pbThread.setVisibility(View.GONE);

        }

    }

}
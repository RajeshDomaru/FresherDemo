package com.example.fresher.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fresher.R;
import com.example.fresher.adapter.ExpensesAdapter;
import com.example.fresher.core.ExpensesModel;
import com.example.fresher.sqlight.MySQLiteOpenHelper;
import com.example.fresher.utils.AppConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpensesListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;

    private String mParam2;

    private View mainView;

    private FloatingActionButton fabAddExpenses;

    private RecyclerView rvExpenses;

    private ProgressBar pbExpensesList;

    public ExpensesListFragment() {
    }

    public static ExpensesListFragment newInstance(String param1, String param2) {
        ExpensesListFragment fragment = new ExpensesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_expenses_list, container, false);

        init();

        setOnClickListener();

        loadExpensesFromFB();

        return mainView;

    }

    private void init() {

        Toolbar toolbar = requireActivity().findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(requireContext().getResources().getString(R.string.expenses_list));

        fabAddExpenses = mainView.findViewById(R.id.fabAddExpenses);

        pbExpensesList = mainView.findViewById(R.id.pbExpensesList);

        rvExpenses = mainView.findViewById(R.id.rvExpenses);

        rvExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void setOnClickListener() {

        fabAddExpenses.setOnClickListener(v -> {

            Fragment fragment = AddOrEditExpensesFragment.newInstance(null);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragment, requireContext().getResources().getString(R.string.add_expenses))
                    .addToBackStack(requireContext().getResources().getString(R.string.expenses_list))
                    .commit();

        });

    }

    private void loadExpenses() {

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(requireContext());

        ArrayList<ExpensesModel> expensesModelArrayList = mySQLiteOpenHelper.getAllExpenses();

        ExpensesAdapter expensesAdapter =
                new ExpensesAdapter(
                        requireContext(),
                        expensesModelArrayList,
                        this::loadExpenses
                );

        rvExpenses.setAdapter(expensesAdapter);

    }

    private void loadExpensesFromFB() {

        pbExpensesList.setVisibility(View.VISIBLE);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(AppConstants.expenses_tbl);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("snapshot", snapshot.toString());

                ArrayList<ExpensesModel> expensesModelArrayList = new ArrayList<>();

                if (snapshot.hasChildren()) {

                    for (DataSnapshot snapshotExpensesModel : snapshot.getChildren()) {

                        ExpensesModel expensesModel = snapshotExpensesModel.getValue(ExpensesModel.class);

                        expensesModelArrayList.add(expensesModel);

                    }

                    ExpensesAdapter expensesAdapter =
                            new ExpensesAdapter(
                                    requireContext(),
                                    expensesModelArrayList,
                                    () -> loadExpensesFromFB()
                            );

                    rvExpenses.setAdapter(expensesAdapter);

                }

                pbExpensesList.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.home_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.menuCopy == item.getItemId()) {

            Toast.makeText(requireContext(), "Pressed Copy!", Toast.LENGTH_SHORT).show();

        } else if (R.id.menuShare == item.getItemId()) {

            Toast.makeText(requireContext(), "Pressed Share!", Toast.LENGTH_SHORT).show();

        } else if (R.id.menuDelete == item.getItemId()) {

            Toast.makeText(requireContext(), "Pressed Delete!", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);

    }

}
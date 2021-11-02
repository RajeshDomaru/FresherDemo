package com.example.fresher.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.fresher.R;
import com.example.fresher.core.ExpensesModel;
import com.example.fresher.sqlight.MySQLiteOpenHelper;
import com.example.fresher.utils.AppConstants;
import com.example.fresher.utils.UtilsClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddOrEditExpensesFragment extends Fragment {

    private View mainView;

    private AppCompatButton btnCancel, btnAddOrUpdate;

    private AppCompatEditText etTitle, etDate, etAmount;

    private AppCompatSpinner spPaidVia;

    private final String[] paidViaArray = new String[]{"GPay", "paytm", "PhonePay", "Credit Card", "Debit Card"};

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    private ExpensesModel expensesModel;

    private DatabaseReference databaseReference;

    public AddOrEditExpensesFragment() {

    }

    public static AddOrEditExpensesFragment newInstance(ExpensesModel expensesModel) {

        AddOrEditExpensesFragment fragment = new AddOrEditExpensesFragment();

        Bundle args = new Bundle();

        args.putSerializable("expensesModel", expensesModel);

        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            expensesModel = (ExpensesModel) getArguments().getSerializable("expensesModel");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_add_or_edit_expenses, container, false);

        init();

        setOnClickListener();

        return mainView;

    }

    private void setOnClickListener() {

        btnAddOrUpdate.setOnClickListener(v -> {

            if (isValidInput()) {

                if (expensesModel == null) {

//                    saveExpenses();

                    saveExpensesToFB();

                } else {

//                    updateExpenses();

                    updateExpensesToFB();

                }

            }

        });

        btnCancel.setOnClickListener(v -> requireActivity().onBackPressed());

    }

    private void saveExpensesToFB() {

        ExpensesModel expensesModel = getFieldValues();

        String expensesID = databaseReference.push().getKey();

        expensesModel.setExpense_Id(expensesID);

        if (expensesID != null) {

            databaseReference.child(expensesID).setValue(expensesModel);

            Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.expenses_inserted), Toast.LENGTH_SHORT).show();

            requireActivity().onBackPressed();

        }

    }

    private void updateExpensesToFB() {

        ExpensesModel expensesModel = getFieldValues();

        String expensesID = expensesModel.getExpense_Id();

        if (expensesID != null) {

            databaseReference.child(expensesID).setValue(expensesModel);

            Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.expenses_updated), Toast.LENGTH_SHORT).show();

            requireActivity().onBackPressed();

        }

    }

    private boolean isValidInput() {

        boolean isValid = true;

        if (!UtilsClass.isValidStringFromEditText(etTitle)) {

            etTitle.setError(requireContext().getResources().getString(R.string.required));

            isValid = false;

        }

        if (!UtilsClass.isValidStringFromEditText(etDate)) {

            etDate.setError(requireContext().getResources().getString(R.string.required));

            isValid = false;

        } else if (!UtilsClass.isValidDateFromEditText(etDate)) {

            etDate.setError(requireContext().getResources().getString(R.string.date_not_valid));

            isValid = false;

        }

        if (!UtilsClass.isValidStringFromEditText(etAmount)) {

            etAmount.setError(requireContext().getResources().getString(R.string.required));

            isValid = false;

        } else if (!UtilsClass.isValidAmountEditText(etAmount)) {

            etAmount.setError(requireContext().getResources().getString(R.string.amount_not_valid));

            isValid = false;

        }

        return isValid;

    }

    private void saveExpenses() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> handler.post(() -> {

            try {

                ExpensesModel expensesModel = getFieldValues();

                long insertedId = mySQLiteOpenHelper.insertExpenses(expensesModel);

                Log.d("insertedId", String.valueOf(insertedId));

                if (insertedId == -1) {

                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.expenses_inserted), Toast.LENGTH_SHORT).show();

                    requireActivity().onBackPressed();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }));

    }

    private void updateExpenses() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> handler.post(() -> {

            try {

                ExpensesModel expensesModel = getFieldValues();

                boolean isUpdate = mySQLiteOpenHelper.updateExpenses(expensesModel);

                if (isUpdate) {

                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.expenses_updated), Toast.LENGTH_SHORT).show();

                    requireActivity().onBackPressed();

                } else {

                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }));

    }

    private ExpensesModel getFieldValues() {

        int expenseId = 0;

        String expense_Id = "";

        if (expensesModel != null) {

            expenseId = expensesModel.getExpenseId();

            expense_Id = expensesModel.getExpense_Id();

        }

        String title = UtilsClass.validStringFromEditText(etTitle);
        String date = UtilsClass.validStringFromEditText(etDate);
        float amount = UtilsClass.validFloatFromEditText(etAmount);
        String paidVia = spPaidVia.getSelectedItem().toString();

        return new ExpensesModel(expenseId, expense_Id, title, date, amount, paidVia);

    }

    private void loadPaidVia() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, paidViaArray);

        spPaidVia.setAdapter(arrayAdapter);

    }

    private void init() {

        Toolbar toolbar = requireActivity().findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(AppConstants.expenses_tbl);

        btnCancel = mainView.findViewById(R.id.btnCancel);
        btnAddOrUpdate = mainView.findViewById(R.id.btnAddOrUpdate);
        etTitle = mainView.findViewById(R.id.etTitle);
        etDate = mainView.findViewById(R.id.etDate);
        etAmount = mainView.findViewById(R.id.etAmount);
        spPaidVia = mainView.findViewById(R.id.spPaidVia);

        loadPaidVia();

        if (expensesModel == null) {

            toolbar.setTitle(requireContext().getResources().getString(R.string.add_expenses));

            btnAddOrUpdate.setText(requireContext().getResources().getString(R.string.add));

        } else {

            toolbar.setTitle(requireContext().getResources().getString(R.string.edit_expenses));

            btnAddOrUpdate.setText(requireContext().getResources().getString(R.string.update));

            prefill();

        }

        mySQLiteOpenHelper = new MySQLiteOpenHelper(requireContext());

    }

    private void prefill() {

        String title = UtilsClass.validString(expensesModel.getTitle());
        String date = UtilsClass.validString(expensesModel.getDate());
        String amount = UtilsClass.validString(String.valueOf(expensesModel.getAmount()));
        String paidVia = UtilsClass.validString(expensesModel.getPaidVia());

        etTitle.setText(title);
        etDate.setText(date);
        etAmount.setText(amount);

        int paidViaPosition = findPaidVia(paidVia);
        if (paidViaPosition < paidViaArray.length)
            spPaidVia.setSelection(paidViaPosition);

    }

    private int findPaidVia(String paidVia) {

        for (int i = 0; i < paidViaArray.length; i++) {

            if (paidVia.equalsIgnoreCase(paidViaArray[i])) {

                return i;

            }

        }

        return 0;

    }

}
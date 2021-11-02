package com.example.fresher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fresher.R;
import com.example.fresher.core.ExpensesModel;
import com.example.fresher.fragments.AddOrEditExpensesFragment;
import com.example.fresher.interfaces.OnRefresh;
import com.example.fresher.sqlight.MySQLiteOpenHelper;
import com.example.fresher.utils.AppConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {

    private final Context context;

    private final ArrayList<ExpensesModel> expensesModelArrayList;

    private final OnRefresh onRefresh;

    public ExpensesAdapter(Context context, ArrayList<ExpensesModel> expensesModelArrayList, OnRefresh onRefresh) {

        this.context = context;

        this.expensesModelArrayList = expensesModelArrayList;

        this.onRefresh = onRefresh;

    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View expensesViewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expenses_layout, parent, false);

        return new ExpensesViewHolder(expensesViewHolder);

    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {

        ExpensesModel expensesModel = expensesModelArrayList.get(position);

        holder.tvTitle.setText(expensesModel.getTitle());

        holder.llExpensesView.setOnClickListener(v -> {


        });

        holder.ivEdit.setOnClickListener(v -> {

            Fragment fragment = AddOrEditExpensesFragment.newInstance(expensesModel);

            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragment, context.getResources().getString(R.string.edit_expenses))
                    .addToBackStack(context.getResources().getString(R.string.expenses_list))
                    .commit();

        });

        holder.ivDelete.setOnClickListener(v -> deleteConfirmationAlert(expensesModel.getExpense_Id()));

    }

    private void deleteConfirmationAlert(String expense_Id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.delete_confirmation_text);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {

//            deleteExpense(expenseId);

            deleteExpenseInFb(expense_Id);

            dialog.dismiss();

        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void deleteExpenseInFb(String expenseId) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(AppConstants.expenses_tbl);

        databaseReference.child(expenseId).removeValue();

        Toast.makeText(context, context.getResources().getString(R.string.deleted_text), Toast.LENGTH_SHORT).show();

        onRefresh.onRefresh();

    }

    private void deleteExpense(int expenseId) {

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context);

        boolean isDeleted = mySQLiteOpenHelper.deleteExpenses(expenseId);

        if (isDeleted) {

            Toast.makeText(context, context.getResources().getString(R.string.deleted_text), Toast.LENGTH_SHORT).show();

            onRefresh.onRefresh();

        } else {

            Toast.makeText(context, context.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public int getItemCount() {

        return expensesModelArrayList.size();

    }

    static class ExpensesViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llExpensesView;

        private final AppCompatTextView tvTitle;

        private final AppCompatImageView ivEdit, ivDelete;

        public ExpensesViewHolder(@NonNull View itemView) {

            super(itemView);

            llExpensesView = itemView.findViewById(R.id.llExpensesView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

            ivEdit = itemView.findViewById(R.id.ivEdit);

            ivDelete = itemView.findViewById(R.id.ivDelete);

        }

    }

}

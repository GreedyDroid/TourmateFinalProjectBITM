package com.example.sayed.tourmate.events.event_detail;


import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.events.SpentMoneyFor;

import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class AddCostDialogFragment extends DialogFragment{
    private AutoCompleteTextView costAutoComplete;
    private ArrayAdapter<String>costReasonAdapter;
    private TextView amountTV;
    private Context context;
    private String time;
    private Calendar calendar;
    private static final String ACTION = "cost_data";

    private final String LOG_TAG = AddCostDialogFragment.class.getSimpleName();

    // onCreate --> (onCreateDialog) --> onCreateView --> onActivityCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        context = getActivity();

        //get Current Time:
        calendar = Calendar.getInstance();
        time = calendar.getTime().toString();

        //For Dialog
        View dialogView = inflater.inflate(R.layout.cost_input_dialog, container, false);
        costAutoComplete = (AutoCompleteTextView) dialogView.findViewById(R.id.cost_auto_complete_TV);
        amountTV = (TextView) dialogView.findViewById(R.id.amount_ET);
        // "Got it" button
        Button buttonPos = (Button) dialogView.findViewById(R.id.pos_button);
        buttonPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    SpentMoneyFor newCost = new SpentMoneyFor(costAutoComplete.getText().toString(),
                                        amountTV.getText().toString(), time);
                    Intent intent = new Intent(ACTION).putExtra("newCost", newCost);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    dismiss();
                }
            }
        });

        // "Cancel" button
        Button buttonNeg = (Button) dialogView.findViewById(R.id.neg_button);
        buttonNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If shown as dialog, cancel the dialog (cancel --> dismiss)
                if (getShowsDialog())
                    getDialog().cancel();
                    // If shown as Fragment, dismiss the DialogFragment (remove it from view)
                else
                    dismiss();
            }
        });

        //Cost AutoComplete
        //All suggestion
        String[] costSuggestions = getResources().getStringArray(R.array.auto_fill_cost_array);
            costReasonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, costSuggestions);
        
        costAutoComplete.setAdapter(costReasonAdapter);


        return dialogView;
    }

    // If shown as dialog, set the width of the dialog window
    // onCreateView --> onActivityCreated -->  onViewStateRestored --> onStart --> onResume
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
        if (getShowsDialog()) {
            // Set the width of the dialog to the width of the screen in portrait mode
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int dialogWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            getDialog().getWindow().setLayout(dialogWidth, WRAP_CONTENT);
        }
    }

    // If dialog is cancelled: onCancel --> onDismiss
    @Override
    public void onCancel(DialogInterface dialog) {
        Log.v(LOG_TAG, "onCancel");
    }

    // If dialog is cancelled: onCancel --> onDismiss
    // If dialog is dismissed: onDismiss
    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v(LOG_TAG,"onDismiss");
    }
    
    //Validate Input 
    private boolean validate()  {
        boolean valid = true;

        String amount = amountTV.getText().toString();
        String cost_reason = costAutoComplete.getText().toString();

        if (amount.isEmpty()) {
            amountTV.setError("Enter Amount");
            valid = false;
        } else {
            amountTV.setError(null);
        }

        if (cost_reason.isEmpty()) {
            valid = false;
        }
        return valid;
    }
}

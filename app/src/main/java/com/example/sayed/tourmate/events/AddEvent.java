package com.example.sayed.tourmate.events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.databinding.ActivityAddEventBinding;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    private ActivityAddEventBinding binding;
    private PlacesAutocompleteTextView pAutocomplete;
    //For DatePicker>>>>
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private String location="", budget="", startDate="", returnDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

        //For Date Picker.... set Time>>>>>>>>>>>>>>>>
        calendar = Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DATE);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
        binding.endDateBT.setEnabled(false);

        //For Auto Place Suggestion>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        pAutocomplete = findViewById(R.id.placeAutoComplete);
        //Here Geocode returns full adderss... by default it will return only city division and country
        pAutocomplete.setResultType(AutocompleteResultType.GEOCODE);

        pAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                pAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        //making all text view visible
                        binding.locationLV.setVisibility(View.VISIBLE);
                        Log.d("text", "details"+placeDetails);
                        if (placeDetails.name != null){
                            location = placeDetails.name;
                            binding.locationTV.setText(location);
                        }
                        for (AddressComponent component : placeDetails.address_components){
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        location= location+", "+component.short_name;
                                        binding.locationTV.setText(binding.locationTV.getText()+", "+component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        location = location+", "+component.short_name;
                                        binding.locationTV.setText(binding.locationTV.getText()+"\n"+component.long_name);
                                        break;
                                    case POSTAL_CODE:
                                    //    mZip.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("test", "failure " + throwable);
                    }
                });

            }
        });
        //End Of Auto Place Picker Suggestion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
    //End of On create



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }



    //start date set button clicked>>>>>>>>>>>>>>>>>>>>
    public void setStartDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, startDateListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private DatePickerDialog.OnDateSetListener startDateListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(i, i1, i2);
            //SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MMM/yy");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY");
            String newDate = simpleDateFormat.format(calendar.getTime());
            binding.startDateBT.setText(newDate);
            binding.endDateBT.setEnabled(true);
            startDate = newDate;
        }
    };

    public void setEndDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, endDateListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private DatePickerDialog.OnDateSetListener endDateListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(i, i1, i2);
            //SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MMM/yy");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY");
            String newDate = simpleDateFormat.format(calendar.getTime());
            binding.endDateBT.setText(newDate);
            returnDate = newDate;
        }
    };
    //date picker dialog end>>>>>>>>>>>>>>>




    //After hitting Add Tour Event Button
    public void addEventBT(View view) {
        budget = binding.tourBudgetET.getText().toString();
        if (validate()){

            startActivity(new Intent(AddEvent.this, Events.class)
                    .putExtra("tourLocation", location)
                    .putExtra("tourBudget", budget)
                    .putExtra("tourStartDate", startDate)
                    .putExtra("tourReturnDate", returnDate));
        }else {
            Snackbar.make(view, "Set All Field Correctly!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    //Validate Information>>>>>>>>
    private boolean validate(){
        boolean isValid = true;
        if (budget.isEmpty()){
            binding.tourBudgetET.setError("Set Budget");
            isValid = false;
        }
        if (binding.locationTV.getText().toString().isEmpty()){
            isValid = false;
        }
        if (startDate.isEmpty()){
            isValid = false;
        }
        if (returnDate.isEmpty()){
            isValid = false;
        }
        return isValid;
    }
}

package com.example.sayed.tourmate.events;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class AddEvent extends AppCompatActivity {
    private ActivityAddEventBinding binding;
    private PlacesAutocompleteTextView pAutocomplete;
    private TourMatePlacesAutocompleteAdapter autocompleteAdapter;
    String localArea="", city="", state, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

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
                            binding.locationTV.setText(placeDetails.name);
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
                                        binding.locationTV.setText(binding.locationTV.getText()+", "+component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}

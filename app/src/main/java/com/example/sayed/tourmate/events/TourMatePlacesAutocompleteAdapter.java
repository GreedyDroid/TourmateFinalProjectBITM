package com.example.sayed.tourmate.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sayed.tourmate.R;
import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.adapter.AbstractPlacesAutocompleteAdapter;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;

/**
 * Created by nurud on 10/18/2017.
 */


public class TourMatePlacesAutocompleteAdapter extends AbstractPlacesAutocompleteAdapter{
    public TourMatePlacesAutocompleteAdapter(final Context context, final PlacesApi api, final AutocompleteResultType autocompleteResultType, final AutocompleteHistoryManager history) {
        super(context, api, autocompleteResultType, history);
    }

    @Override
    protected View newView(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.places_auto_complete_item, viewGroup, false);
    }

    @Override
    protected void bindView(View view, Place place) {
        ((TextView) view).setText(place.description);
    }
}

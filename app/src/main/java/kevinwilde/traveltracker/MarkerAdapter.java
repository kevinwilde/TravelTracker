package kevinwilde.traveltracker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by Kevin Wilde on 12/22/2015.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mLayoutInflater;
    private View mView;
    private HashMap<String, Memory> mMemories;

    MarkerAdapter(LayoutInflater layoutInflater, HashMap<String, Memory> memories) {
        mLayoutInflater = layoutInflater;
        mMemories = memories;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (mView == null) {
            mView = mLayoutInflater.inflate(R.layout.marker, null);
        }
        Memory memory = mMemories.get(marker.getId());
        TextView title = (TextView) mView.findViewById(R.id.title);
        TextView snippet = (TextView) mView.findViewById(R.id.snippet);
        TextView notes = (TextView) mView.findViewById(R.id.notes);
        title.setText(memory.city);
        snippet.setText(memory.country);
        notes.setText(memory.notes);

        return mView;
    }
}

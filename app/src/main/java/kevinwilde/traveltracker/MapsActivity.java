package kevinwilde.traveltracker;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener, MemoryDialogFragment.Listener,
        GoogleMap.OnInfoWindowClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MapsActivity";
    private static final String MEMORY_DIALOG_TAG = "MemoryDialog";
    private static final String DELETE_MEMORY_DIALOG_TAG = "DeleteMemoryDialog";

    private GoogleMap mMap;
    private HashMap<String, Memory> mMemories = new HashMap<>();
    private MemoriesDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDataSource = new MemoriesDataSource(this);
//        new AsyncTask<Void, Void, List<Memory>>(){
//            @Override
//            protected List<Memory> doInBackground(Void... params) {
//                return mDataSource.getAllMemories();
//            }
//
//            @Override
//            protected void onPostExecute(List<Memory> memories) {
//                onFetchedMemories(memories);
//            }
//        }.execute();
        getLoaderManager().initLoader(0, null, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        onMapReady(mMap);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);


        //// Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Memory memory = new Memory();
        updateMemoryPosition(memory, latLng);
        MemoryDialogFragment.newInstance(memory).show(getFragmentManager(), MEMORY_DIALOG_TAG);
    }

    private void onFetchedMemories(List<Memory> memories){
        for (Memory memory : memories) {
            addMarker(memory);
        }
    }

    private void updateMemoryPosition(Memory memory, LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> matches = null;
        try {
            matches = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Address bestMatch = (matches.isEmpty()) ? null : matches.get(0);
        int maxLineIndex = bestMatch.getMaxAddressLineIndex();

        memory.latitude = latLng.latitude;
        memory.longitude = latLng.longitude;
        memory.city = bestMatch.getAddressLine(maxLineIndex - 1);
        memory.country = bestMatch.getAddressLine(maxLineIndex);
    }

    @Override
    public void OnSaveClicked(Memory memory) {
        addMarker(memory);
        mDataSource.CreateMemory(memory);
    }

    private void addMarker(Memory memory) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(memory.latitude, memory.longitude)));
        mMemories.put(marker.getId(), memory);
    }

    @Override
    public void OnCancelClicked(Memory memory) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Memory memory = mMemories.get(marker.getId());
        updateMemoryPosition(memory, marker.getPosition());
        mDataSource.UpdateMemory(memory);
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        final Memory memory = mMemories.get(marker.getId());
        String[] actions  = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(memory.city+", "+memory.country)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1){
                            marker.remove();
                            mDataSource.DeleteMemory(memory);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MemoriesLoader(this, mDataSource);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        onFetchedMemories(mDataSource.cursorToMemoriesList(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

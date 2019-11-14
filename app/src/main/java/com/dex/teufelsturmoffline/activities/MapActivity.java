package com.dex.teufelsturmoffline.activities;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.database.SettingsSaver;
import com.dex.teufelsturmoffline.model.Peak;
import com.dex.teufelsturmoffline.model.PeakMarker;
import com.dex.teufelsturmoffline.views.DialogRouteListFragment;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.view.InputListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements InputListener {

    private MapView mapView;
    private LatLong currentLatLong;
    private byte zoomLevel;
    private FloatingActionButton fap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AndroidGraphicFactory.createInstance(getApplication());
       // mapView = new MapView(this);
       // setContentView(mapView);
        setContentView(R.layout.activity_map);
        mapView  = findViewById(R.id.map);
        mapView.addInputListener(this);
        fap = findViewById(R.id.fap_map_pos);
        fap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Position not implemented yet :(", Toast.LENGTH_LONG).show();
            }
        });

        try {
            mapView.setClickable(true);
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(false);

            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            File mapFile = new File(getFilesDir(), "/sn.map");
            MapDataStore mapDataStore = new MapFile(mapFile);
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            DatabaseHelper db = new DatabaseHelper(this);
            List<Peak> peakList = db.getAllPeaks();

            List<PeakMarker> markers = new ArrayList<>();

            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(
                    ContextCompat.getDrawable(this, R.drawable.map_pin_solid)
            );
            bitmap.scaleTo(45,75);

            for (Peak peak : peakList){
                PeakMarker  m = new PeakMarker(new LatLong(peak.getLatitude(), peak.getLongitude()), bitmap, 0,0, peak, this);
                markers.add(m);
            }

            for (PeakMarker marker : markers){
                mapView.getLayerManager().getLayers().add(marker);
            }

            loadSettings();
            applySettings();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void applySettings() {
        mapView.setCenter(currentLatLong);
        mapView.setZoomLevel(zoomLevel);
    }

    public void openDialog(String name){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogRouteListFragment newFragment = new DialogRouteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        newFragment.setArguments(bundle);

        newFragment.show(ft, "dialog");

    }

    @Override
    public void onMoveEvent() {
        currentLatLong = mapView.getModel().mapViewPosition.getCenter();
        zoomLevel = mapView.getModel().mapViewPosition.getZoomLevel();
    }

    @Override
    public void onZoomEvent() {
        zoomLevel = mapView.getModel().mapViewPosition.getZoomLevel();
    }

    private void saveSettings(){
        SettingsSaver.setZoomLvl(this, zoomLevel);
        SettingsSaver.setCenter(this, currentLatLong);
    }

    private void loadSettings(){
        zoomLevel = SettingsSaver.getZoomLvl(this);
        currentLatLong = SettingsSaver.getCenter(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

}

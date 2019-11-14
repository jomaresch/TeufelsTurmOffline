package com.dex.teufelsturmoffline.activities;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AndroidGraphicFactory.createInstance(getApplication());
        mapView = new MapView(this);
        setContentView(mapView);


        try {
            mapView.setClickable(true);
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(true);

            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            File mapFile = new File(getFilesDir(), "/ss.map");
            MapDataStore mapDataStore = new MapFile(mapFile);
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            DatabaseHelper db = new DatabaseHelper(this);
            List<Peak> peakList = db.getAllPeaks();

            List<PeakMarker> markers = new ArrayList<>();

            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(ContextCompat.getDrawable(this,
                    R.drawable.ic_info));
            bitmap.scaleTo(60,75);

            for (Peak peak : peakList){
                PeakMarker  m = new PeakMarker(new LatLong(peak.getLatitude(), peak.getLongitude()), bitmap, 0,0, peak, this);
                markers.add(m);
            }

            for (PeakMarker marker : markers){
                mapView.getLayerManager().getLayers().add(marker);
            }

            mapView.setCenter(new LatLong(51.0504088,  13.7372621));
            mapView.setZoomLevel((byte) 10);
        } catch (Exception e) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace();
        }

    }

    public void openDialog(String name){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogRouteListFragment newFragment = new DialogRouteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        newFragment.setArguments(bundle);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(newFragment.getDialog().getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        newFragment.show(ft, "dialog");
//        newFragment.getDialog().getWindow().setAttributes(lp);

    }
}

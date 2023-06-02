package com.dex.teufelsturmoffline.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.database.SettingsSaver;
import com.dex.teufelsturmoffline.model.Peak;
import com.dex.teufelsturmoffline.model.PeakMarker;

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

public class MapViewFragment extends Fragment implements InputListener, com.dex.teufelsturmoffline.model.MapView {

    private MapView mapView;
    private LatLong currentLatLong;
    private byte zoomLevel;

    private void applySettings() {
        mapView.setCenter(currentLatLong);
        mapView.setZoomLevel(zoomLevel);
    }

    public void openDialog(String name){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        mapView  =view.findViewById(R.id.forge_map);
        mapView.addInputListener(this);

        try {
            mapView.setClickable(true);
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(false);

            TileCache tileCache = AndroidUtil.createTileCache(getActivity(), "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            File mapFile = new File(getActivity().getFilesDir(), "/sn.map");
            MapDataStore mapDataStore = new MapFile(mapFile);
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            DatabaseHelper db = new DatabaseHelper(getActivity());
            List<Peak> peakList = db.getAllPeaks();

            List<PeakMarker> markers = new ArrayList<>();

            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(
                    ContextCompat.getDrawable(getActivity(), R.drawable.map_pin_solid)
            );
            bitmap.scaleTo(45,75);

            for (Peak peak : peakList){
                PeakMarker  m = new PeakMarker(
                        new LatLong(peak.getLatitude(), peak.getLongitude()),
                        bitmap,
                        0,
                        0,
                        peak,
                        this);
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
        return view;
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
        SettingsSaver.setZoomLvl(getActivity(), zoomLevel);
        SettingsSaver.setCenter(getActivity(), currentLatLong);
    }

    private void loadSettings(){
        zoomLevel = SettingsSaver.getZoomLvl(getActivity());
        currentLatLong = SettingsSaver.getCenter(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }
}
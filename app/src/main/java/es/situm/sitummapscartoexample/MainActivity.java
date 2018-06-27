package es.situm.sitummapscartoexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import es.situm.sdk.SitumSdk;
import es.situm.sdk.error.Error;
import es.situm.sdk.model.cartography.Building;
import es.situm.sdk.model.cartography.BuildingInfo;
import es.situm.sdk.model.cartography.Floor;
import es.situm.sdk.model.cartography.Poi;
import es.situm.sdk.utils.Handler;

import es.situm.sitummapscarto.MapView;
import es.situm.sitummapscarto.SitumMap;
import es.situm.sitummapscarto.basemodule.connector.interfaces.sitummap.OnActiveBuildingListener;
import es.situm.sitummapscarto.basemodule.connector.interfaces.sitummap.BuildingDataSource;
import es.situm.sitummapscarto.basemodule.connector.interfaces.sitummap.OnFloorChangedListener;
import es.situm.sitummapscarto.basemodule.connector.interfaces.sitummap.OnPoiSelectedListener;
import es.situm.sitummapscarto.settings.DefaultCartoSettings;
import es.situm.sitummapscarto.interfaces.OnSitumMapReadyCallBack;

public class MainActivity extends AppCompatActivity implements OnSitumMapReadyCallBack{


    SitumMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout mainlayout = (FrameLayout) findViewById(R.id.mainlayout);

        //INIT SITUM SDK AND LOGIN
        SitumSdk.init(this);

        //LOAD MAP LAYOUT
        MapView mapview = findViewById(R.id.sitummap);

        SitumSdk.communicationManager().fetchBuildings(new Handler<Collection<es.situm.sdk.model.cartography.Building>>() {
            @Override
            public void onSuccess(Collection<es.situm.sdk.model.cartography.Building> buildings) {

                //LOAD BUILDINGS
                mapview.setUserBuildings(buildings);

                //INITIALIZE MAP
                mapview.getMapAsync(MainActivity.this);

            }

            @Override
            public void onFailure(Error error) {

                Toast.makeText(MainActivity.this,"The buildings could not be recovered",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onMapReady(SitumMap map) {

        this.map = map;


        map.setOnFloorChangedListener(new OnFloorChangedListener() {
            @Override
            public void onFloorChanged(Floor floor) {

                Toast.makeText(MainActivity.this,floor.getIdentifier(),Toast.LENGTH_SHORT).show();

            }
        });


        map.setOnPoiSelectionListener(new OnPoiSelectedListener() {
            @Override
            public void onPoiSelected(Poi poi) {

                Toast.makeText(MainActivity.this,poi.getIdentifier(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPoiDeselected() {

                Toast.makeText(MainActivity.this,"POI deselected",Toast.LENGTH_SHORT).show();
            }
        });


        map.setOnBuildingDataSourceListener(new BuildingDataSource() {
            @Override
            public void completeBuildingInfo(Building building, @NonNull BuildingInfoCallback callback) {

                SitumSdk.communicationManager().fetchBuildingInfo(building, new Handler<BuildingInfo>() {
                    @Override
                    public void onSuccess(final BuildingInfo newBuildingInfo) {

                        callback.onReceived(newBuildingInfo);
                        map.refresh();

                    }

                    @Override
                    public void onFailure(Error error) {


                    }
                });

                Toast.makeText(MainActivity.this,"Building info needed: "+building.getIdentifier(),Toast.LENGTH_LONG).show();


            }
        });


        map.setOnActiveBuildingListener(new OnActiveBuildingListener() {
            @Override
            public void activeBuilding(@NonNull Building building) {

                Toast.makeText(MainActivity.this,"Active building: "+building.getIdentifier(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNotActiveBuilding() {

                Toast.makeText(MainActivity.this,"Not active building",Toast.LENGTH_LONG).show();

            }
        });

        Toast.makeText(this, "SITUM MAP LOADED", Toast.LENGTH_LONG).show();



    }

    @Override
    protected void onStart() {

        super.onStart();
        if (map != null) map.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (map != null) map.onResume();

    }

    @Override
    protected void onRestart() {
        // get the data saved in the onpause method to rebuild the app state
        super.onRestart();
        if (map != null) map.onRestart();
    }

    @Override
    protected void onPause() {
        // save the application state
        super.onPause();
        if (map != null) map.onPause();
    }

    @Override
    protected void onStop() {

        if (map != null) map.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        if (map != null) map.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        if (map != null && map.onBackPressed()) super.onBackPressed();
    }


}

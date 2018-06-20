SitumMapsCartoExample

This project is an example of how to integrate the SitumMapsCarto library in your application.

To integrate the library follow the next steps:


1.- Copy the sitummapscarto-release.aar library into your /libs folder.

2.- Add the following dependencies to your build.gradle:

    compile('es.situm:situm-sdk:2.16.0@aar') {
        transitive = true
    }

    compile(name:'sitummapscarto-release', ext:'aar')
        

4.- Add the next lines to your AndroidManifest.xml and put your Google Maps API KEY.

    <uses-library android:name="com.google.android.maps"
    android:required="false"/>

    <meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_API_KEY" />
    <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />


5.- Add your situm sdk credentials to the AndroidManifest.xml or programatically.

6.- Add the MapView view to your layout:

    <es.situm.sitummapscarto.MapView
        android:id="@+id/sitummap"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        
7.- In your Activiy load the MapView into OnCreated() and set the map settings:
        
        MapView mapview = findViewById(R.id.sitummap);
        
        DefaultCartoSettings settings = new DefaultCartoSettings();
        mapview.setSettings(settings);
        
8.- Implement in your activity the interface  OnSitumMapReadyCallBack and the onMapReady(SitumMap map) method

        public class MainActivity extends AppCompatActivity implements OnSitumMapReadyCallBack{

            SitumMap map;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }

            @Override
            public void onMapReady(SitumMap map){
                this.map = map;
            }

        }

        
9.- Load the buildings into the map and call getMapAsync. When the map is ready, OnMapReady(SitumMap map) will be called:

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
        
10.- Override the life cycle method of your activity as:

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
            super.onRestart();
            if (map != null) map.onRestart();
        }

        @Override
        protected void onPause() {
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

        
When the SitumMap is loaded, you can work with them:

 - To kwon when a level or floor is changed, implement the following listener:

        map.setOnFloorChangedListener(new OnFloorChangedListener() {
            @Override
            public void onFloorChanged(Floor floor) {

                Toast.makeText(MainActivity.this,floor.getIdentifier(),Toast.LENGTH_SHORT).show();

            }
        });
        
- To kwon when a poi is selected or deselected, implement the following listener:
        
        map.setOnPoiSelectionChanguedListener(new OnPoiSelectedListener() {
            @Override
            public void onPoiSelected(Poi poi) {
            
                Toast.makeText(MainActivity.this,poi.getIdentifier(),Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onPoiDeselected() {
            
                Toast.makeText(MainActivity.this,"POI deselected",Toast.LENGTH_SHORT).show();
            }
        });
        
- To kwon when you need pass the additional info to a building because the zoom on the map requires it:
        
        map.setOnBuildingDataSourceListener(new BuildingDataSource() {
            @Override
            public void onFetchCompleteInformation(Building building, @NonNull BuildingInfoCallback callback) {
            
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
        
- To kwon when the active building changed:

        map.setOnActiveBuildingChangedListener(new OnActiveBuildingListener() {
            @Override
            public void activeBuilding(@NonNull Building building) {

                Toast.makeText(MainActivity.this,"Active building changed: "+building.getIdentifier(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNotActiveBuilding() {
            
                Toast.makeText(MainActivity.this,"Not active building",Toast.LENGTH_LONG).show();
                
            }
        });

        
- To get the active building:
        
        Building building = map.getActiveBuilding();
        
- To get the active floor:

        Floor floor = map.getActiveFloor();


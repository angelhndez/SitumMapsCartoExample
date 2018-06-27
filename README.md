# SitumMapsCarto Library integration Sample app


This is an Android application designed with the SitumMapsCarto library. With the sample application you will be able in a few steps of
  1. Load your buildings into the SitumMap.
  2. Load the POIs of the buildings.
  3. Interact with the buildings, floors and POIs.
  4. Get events from the buildings, floors and POIs.


# Table of contents
#### [Introduction](#introduction)
#### [Setup](#configureproject)
1. [Step 1: Configure our SDK in your Android project](#configureproject)
2. [Step 2: Initialize the SDK](#init)
3. [Step 3: Set your credentials](#config)
4. [Step 4: Setup Google Maps](#mapsapikey)
5. [Step 5: Add the MapView view to your layout](#mapview)
6. [Step 6: Initialize and load the MapView](#initmapview)
7. [Step 7: Override the life cicle of the activity](#lifecicle)


#### [Samples](#samples)

1. [Load user buildings](#loadbuildings)
2. [Load additional building information](#loadadditionalinfo)
3. [Interact with the buildings](#interactbuildings)
4. [Interact with floors](#interactfloors)
5. [Interact with POIs](#interactpois)

#### [More information](#moreinfo)


### Introduction <a name="introduction"></a>

SitumMapsCarto is a library that allow any developer to build and integrate the cartography functionalities of the sdk of situm very easy



In this tutorial, we will guide you step by step to set up your app with the SitumMapsCarto library.

Before starting to write code, we recommend you to set up an account in our Dashboard
(https://dashboard.situm.es), retrieve your API KEY and configure your first building.

1. Go to the [sign in form](http://dashboard.situm.es/accounts/register) and enter your username
and password to sign in.
2. Go to the [account section](https://dashboard.situm.es/accounts/profile) and on the bottom, click
on "generate one" to generate your API KEY.
3. Go to the [buildings section](http://dashboard.situm.es/buildings) and create your first building.
4. Download [Situm Mapping Tool](https://play.google.com/store/apps/details?id=es.situm.maps)
Android application. With this application you will be able to configure and test Situm's indoor
positioning system in your buildings.


### <a name="configureproject"></a> Step 1: Configure our SDK in your Android project

First of all, you must configure Situm SDK in your Android project. This has been already done for
you in the sample application, but nonetheless we will walk you through the process.

* Add the maven repository to the project *build.gradle*:

```groovy
allprojects {
    repositories {
        maven { url "https://repo.situm.es/artifactory/libs-release-local" }
    }
}
```

* Then copy the sitummapscarto-1.0.0.aar library into your /libs folder and add the Situm SDK library and the sitummapscarto library dependencies into the section *dependencies* of the app *build.gradle*.
It's important to add the `transitive = true` property to download the Situm SDK dependencies.

```groovy
    compile ('es.situm:situm-sdk:2.22.0@aar') {
        transitive = true
    }

    compile(name:'sitummapscarto-release', ext:'aar')
```

### <a name="init"></a> Step 2: Initialize the SDK

You must initialize the SDK in the `onCreate()` method of your Application:

```java
@Override
public void onCreate() {
    super.onCreate();
    SitumSdk.init(this);

}
```

### <a name="config"></a> Step 3: Set your credentials


You can set the credentials (user and API key) in the `AndroidManifest.xml` file adding the next `meta-data` fields:

```xml
<meta-data
    android:name="es.situm.sdk.API_USER"
    android:value="API_USER_EMAIL" />
<meta-data
    android:name="es.situm.sdk.API_KEY"
    android:value="API_KEY" />
```



### <a name="mapsapikey"><a/> Step 4: Setup Google Maps

First of all, you need to add the Google Services dependency to the project. If you need more info:
[Setup Google Play Services](https://developers.google.com/android/guides/setup). To do this, paste the dependency in
your module *build.gradle* as usual:
```
compile 'com.google.android.gms:play-services-maps:11.8.0'
```
Add in the app manifest the version of the Google Play Services that we have imported. To do this, paste
the next snippet inside the application tag:
```
<uses-library android:name="com.google.android.maps"
    android:required="false"/>

    <meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_API_KEY" />
    <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

An api key is needed to add Google Maps Services and use them in an application. If you need more info,
you can visit the [official Google Maps documentation](https://developers.google.com/maps/documentation/android-api/intro).
To obtain a Google api key please refer to the same documentation:
[obtain Google Maps api key](https://developers.google.com/maps/documentation/android-api/signup),


### <a name="mapview"><a/> Step 5: Add the MapView view to your layout

Place the map view in the layout of your design

```
<es.situm.sitummapscarto.MapView
        android:id="@+id/sitummap"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```


### <a name="initmapview"><a/> Step 6: Initialize and load the MapView

In your Activiy load the MapView into OnCreated()

```java
    MapView mapview = findViewById(R.id.sitummap);
```

You must implement the `OnSitumMapReadyCallBack` interface and override the `onMapReady` method.

```java
    public class MainActivity extends AppCompatActivity implements OnSitumMapReadyCallBack{

        SitumMap map;

        @Override
        public void onMapReady(SitumMap map){
            this.map = map;
        }

    }
```

`onMapReady` will be called when the map is loaded and ready.

### <a name="lifecicle"><a/> Step 7: Override the life cicle of the activity

In your Activiy override the next as:

```java
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

```





## Samples <a name="samples"></a>

Now that you have correctly configured your Android project, you can start writing your application's code.
In this sample project, all the code has been included in the file
[MainActivity.java](https://github.com/angelhndez/SitumMapsCartoExample/blob/master/app/src/main/java/es/situm/sitummapscartoexample/MainActivity.java).

## <a name="loadbuildings"></a> Load user buildings

You can get your buildings from the Situm Sdk and load them on the map easily as indicated below:

```java
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
```


You can see that it is necessary to load the buildings with `setUserBuildings` before initializing the map with `getMapAsync`.


## <a name="loadadditionalinfo"></a> Load additional building information

You can implement `setOnBuildingDataSourceListener` listener that is called when the zoom of the map requires additional information for the active building.
```java
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
```


## <a name="interactbuildings"></a> Interact with the buildings

You can implement `setOnActiveBuildingListener` listener to know when the active building changes:
```java
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
```

You can also obtain at any time which is the current active building with
```java
 Building building = map.getActiveBuilding();
```


## <a name="interactfloors"></a> Interact with floors

You can implement `setOnFloorChangedListener` listener to know when the floor changes:
```java
 map.setOnFloorChangedListener(new OnFloorChangedListener() {
     @Override
     public void onFloorChanged(Floor floor) {

         Toast.makeText(MainActivity.this,floor.getIdentifier(),Toast.LENGTH_SHORT).show();

     }
 });
```

You can also obtain at any time which is the current floor with
```java
 Floor floor = map.getActiveFloor();
```

## <a name="interactpois"></a> Interact with POIs

You can implement `setOnPoiSelectionListener` listener to know when the POI selection changes:
```java
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
```

## <a name="moreinfo"></a> More information

More info is available at our [Developers Page](https://des.situm.es/developers/pages/android/).
For any other question, contact us [here](mailto:support@situm.es)
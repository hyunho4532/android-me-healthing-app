package com.example.workingapp.Activity.marker;

import static com.example.workingapp.R.*;
import static com.example.workingapp.R.drawable.activity_bread1;
import static com.example.workingapp.R.drawable.activity_coffee_no_brand;
import static com.example.workingapp.R.drawable.activity_compose_coffee;
import static com.example.workingapp.R.drawable.activity_dunkin;
import static com.example.workingapp.R.drawable.activity_ediya;
import static com.example.workingapp.R.drawable.activity_marker_add_icon;
import static com.example.workingapp.R.drawable.activity_marker_location;
import static com.example.workingapp.R.drawable.activity_mega_coffee;
import static com.example.workingapp.R.drawable.activity_parri_baguette;
import static com.example.workingapp.R.drawable.activity_star_bucks;
import static com.example.workingapp.R.drawable.activity_the_coffee;
import static com.example.workingapp.R.drawable.activity_tous_les_jours;
import static com.example.workingapp.R.id.*;
import static noman.googleplaces.NRPlaces.*;
import static noman.googleplaces.PlaceType.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workingapp.Activity.MainActivity;
import com.example.workingapp.Activity.marker.adapter.MarkerStreamAdapter;
import com.example.workingapp.Activity.marker.data.MarkerItem;
import com.example.workingapp.Activity.marker.database.OpenMarkerHelper;
import com.example.workingapp.Activity.marker.googleMap.MarkerSetting;
import com.example.workingapp.Activity.marker.googleMap.location.ListViewActivity;
import com.example.workingapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


public class ActivityMarkerActivity extends AppCompatActivity implements MarkerSetting, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {

    public GoogleMap _googleMap;
    public Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION};

    public Location mCurrentLocation;
    public LatLng currentPosition;

    LatLng previousPosition = null;
    Marker addedMarker = null;
    int tracking = 0;

    public FusedLocationProviderClient mFusedLocationClient;
    public LocationRequest locationRequest;
    public Location location;

    public View mLayout;

    public TextView tv_my_gps_location_view;

    CardView cv_food_menu1, cv_exercise_menu2, cv_bread_menu3, cv_best_map_insert, cv_best_map_move;
    EditText et_search_address;
    Button btn_search_address;

    List<Marker> previous_marker = null;

    private RecyclerView mRecyclerView;
    private ArrayList<MarkerItem> mMarkerItems;
    private OpenMarkerHelper mMarkerHelper;
    private MarkerStreamAdapter markerStreamAdapter;

    private MarkerOptions markerOptions;

    private long pressedTime;

    private ImageView iv_earth_click, iv_2d_click, iv_3d_click, iv_subway_click, iv_bus_click, iv_share_click;

    private Dialog dialog;

    private Geocoder geocoder;

    private TextView tv_menu_marker_title, tv_menu_snippet_title;
    
    private CardView cv_menu;

    @Override
    public void onBackPressed() {

        if (pressedTime == 0) {
            Toast.makeText(ActivityMarkerActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int)(System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(ActivityMarkerActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();

                Intent intent = new Intent(ActivityMarkerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(layout.activity_marker);

        mMarkerHelper = new OpenMarkerHelper(this);
        mRecyclerView = findViewById(stream_recyclerview);
        mMarkerItems = new ArrayList<>();

        previous_marker = new ArrayList<>();

        cv_food_menu1 = findViewById(id.cv_food_menu1);
        cv_exercise_menu2 = findViewById(id.cv_exercise_menu2);
        cv_bread_menu3 = findViewById(id.cv_bread_menu3);

        cv_best_map_move = findViewById(cv_weather);

        cv_best_map_insert = findViewById(id.cv_best_map_insert);

        et_search_address = findViewById(et_search);

        iv_earth_click = findViewById(id.iv_earth_click);

        iv_2d_click = findViewById(id.iv_2d_click);
        iv_3d_click = findViewById(id.iv_3d_click);

        iv_subway_click = findViewById(id.iv_subway_click);
        iv_bus_click = findViewById(id.iv_bus_click);
        iv_share_click = findViewById(R.id.iv_share_click);

        tv_menu_marker_title = findViewById(id.tv_menu_marker_title);
        tv_menu_snippet_title = findViewById(id.tv_menu_snippet_title);

        btn_search_address = findViewById(id.btn_search_address);

        cv_menu = findViewById(cv_map_click);

        dialog = new Dialog(ActivityMarkerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout.dialog_earth_item);

        loadRecentDB();

        cv_food_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceFoodInformation(currentPosition);
            }
        });

        cv_exercise_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceExerciseInformation(currentPosition);
            }
        });

        cv_bread_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceBreadInformation(currentPosition);
            }
        });

        btn_search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = getLocationFromAddress(getApplicationContext(), et_search_address.getText().toString());

                showCurrentLocation(location);
            }
        });

        Glide.with(ActivityMarkerActivity.this).load(drawable.activity_earth_icon_menu).into(iv_earth_click);

        iv_earth_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                ImageView iv_2d_click = dialog.findViewById(id.iv_2d_click);
                ImageView iv_3d_click = dialog.findViewById(id.iv_3d_click);
                ImageView iv_4d_click = dialog.findViewById(id.iv_4d_click);

                iv_2d_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        dialog.dismiss();
                    }
                });

                iv_3d_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                        dialog.dismiss();
                    }
                });

                iv_4d_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                        dialog.dismiss();
                    }
                });
            }
        });

        iv_subway_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceSubwayInformation(currentPosition);
            }
        });

        iv_bus_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceSubwayInformation(currentPosition);
            }
        });

        cv_best_map_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ActivityMarkerActivity.this, "즐겨찾기 클릭.", Toast.LENGTH_SHORT).show();

                _googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @SuppressLint("PotentialBehaviorOverride")
                    @Override
                    public void onMapLongClick(@NonNull LatLng latLng) {

                        View dialogView = getLayoutInflater().inflate(layout.dialog_stream, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setView(dialogView);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        TextView tv_latitude_title_view = dialogView.findViewById(id.tv_latitude_title_view);
                        TextView tv_hardness_title_view = dialogView.findViewById(id.tv_hardness_title_view);

                        tv_latitude_title_view.setText(String.valueOf(latLng.latitude));
                        tv_hardness_title_view.setText(String.valueOf(latLng.longitude));

                        EditText et_title = dialogView.findViewById(id.et_title);
                        EditText et_snippet = dialogView.findViewById(id.et_snippet);

                        Button btn_ok = dialogView.findViewById(id.btn_ok);

                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LatLng position = new LatLng(latLng.latitude, latLng.longitude);

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(position);
                                markerOptions.title(et_title.getText().toString());
                                markerOptions.snippet(et_snippet.getText().toString());

                                @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_marker_add_icon);

                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                _googleMap.addMarker(markerOptions);

                                mMarkerHelper.onInsert(et_title.getText().toString(), et_snippet.getText().toString(), Double.parseDouble(tv_latitude_title_view.getText().toString()), Double.parseDouble(tv_hardness_title_view.getText().toString()));


                                MarkerItem markerItem = new MarkerItem();
                                markerItem.setTitle(et_title.getText().toString());
                                markerItem.setSnippet(et_snippet.getText().toString());
                                markerItem.setLatitude(Double.parseDouble(tv_latitude_title_view.getText().toString()));
                                markerItem.setHardness(Double.parseDouble(tv_hardness_title_view.getText().toString()));

                                markerStreamAdapter.addItem(markerItem);
                                mRecyclerView.smoothScrollToPosition(0);

                                Toast.makeText(ActivityMarkerActivity.this, "등록 추가되었습니다.", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        });

                        _googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(@NonNull Marker marker) {
                                marker.remove();
                            }
                        });
                    }
                });
            }
        });

        tv_my_gps_location_view = findViewById(id.tv_my_gps_location_view);

        previous_marker = new ArrayList<>();

        mLayout = findViewById(layout_main);

        locationRequest = new com.google.android.gms.location.LocationRequest()
                .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        @SuppressLint("CutPasteId") final ScrollView scrollView = (ScrollView)findViewById(layout_main);
        ImageView ivMapTransparent = (ImageView)findViewById(id.ivMapTransparent);

        ivMapTransparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    default:
                        return true;
                }
            }
        });


        iv_share_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Sharing_Intent = new Intent(Intent.ACTION_SEND);
                Sharing_Intent.setType("text/plain");

                String getMarkerTitleText = tv_menu_marker_title.getText().toString();
                String getMarkerLocationText = tv_menu_snippet_title.getText().toString();

                Sharing_Intent.putExtra(Intent.EXTRA_TEXT, "간판 이름은 " + getMarkerTitleText + " (이)고 위치는 " + getMarkerLocationText);

                Intent Sharing = Intent.createChooser(Sharing_Intent, "공유하기");
                startActivity(Sharing);
            }
        });
    }

    private Location getLocationFromAddress(Context applicationContext, String address) {
        Geocoder geocoder = new Geocoder(applicationContext);
        List<Address> addresses;
        Location resLocation = new Location("");

        try {
            addresses = geocoder.getFromLocationName(address, 5);

            if ((addresses == null) || (addresses.size() == 0)) {
            }

            if (et_search_address.getText().toString().equals("")) {
                Toast.makeText(ActivityMarkerActivity.this, "알 수 없는 주소입니다.", Toast.LENGTH_SHORT).show();
            }

            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return resLocation;
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    private void loadRecentDB() {
        mMarkerItems = mMarkerHelper.getMarkerList();

        if (markerStreamAdapter == null) {
            markerStreamAdapter = new MarkerStreamAdapter(mMarkerItems, this);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(markerStreamAdapter);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        _googleMap = googleMap;

        setDefaultLocation();

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 있어야 합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(ActivityMarkerActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }

        _googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);

                previousPosition = currentPosition;

                if (previousPosition == null) {
                    previousPosition = currentPosition;
                }

                if ((addedMarker != null) && tracking == 1) {
                    double radius = 500;

                    double distance = SphericalUtil.computeDistanceBetween(currentPosition, addedMarker.getPosition());

                    if ((distance < radius) && (!previousPosition.equals(currentPosition))) {
                        Toast.makeText(ActivityMarkerActivity.this, addedMarker.getTitle() + "까지" + (int)distance + "m 남음", Toast.LENGTH_SHORT).show();
                    }
                }

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도: " + String.valueOf(location.getLatitude())
                        + "경도: " + String.valueOf(location.getLongitude());

                tv_my_gps_location_view.setText(markerTitle);

                setCurrentLocation();

                mCurrentLocation = location;
            }
        }
    };

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationSetting();
        } else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                _googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (_googleMap != null) {
                _googleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
            );
        } catch (IOException ioException) {
            return "지오코더 서비스 사용 불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation() {
        if (currentMarker != null) {
            currentMarker.remove();
        }
    }

    private void setDefaultLocation() {
        try {
            String title = getIntent().getStringExtra("title");
            String snippet = getIntent().getStringExtra("snippet");
            double latitude = getIntent().getDoubleExtra("latitude", 0.0);
            double hardness = getIntent().getDoubleExtra("hardness", 0.0);

            LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(hardness)));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.snippet(snippet);

            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_marker_location);

            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            _googleMap.addMarker(markerOptions);
            _googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            _googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));


        } catch (Exception exception) {
            LatLng latLng = new LatLng(37.5, 126.9);

            _googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            _googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    public boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int hasCourseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCourseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length ==
        REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result: grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                startLocationUpdates();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                } else {
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    public void showPlaceFoodInformation(LatLng location) {
        _googleMap.clear();

        if (previous_marker != null) {
            previous_marker.clear();
        }

        new Builder()
                .listener(ActivityMarkerActivity.this)
                .key("AIzaSyB_X_VLyr9sUV-8c25918Dhr9U9oCn6IXo")
                .latlng(location.latitude, location.longitude)
                .radius(500)
                .type(RESTAURANT)
                .build()
                .execute();
    }

    public void showPlaceSubwayInformation(LatLng location) {
        _googleMap.clear();

        if (previous_marker != null) {
            previous_marker.clear();
        }

        new Builder()
                .listener(ActivityMarkerActivity.this)
                .key("AIzaSyB_X_VLyr9sUV-8c25918Dhr9U9oCn6IXo")
                .latlng(location.latitude, location.longitude)
                .radius(5000)
                .type(SUBWAY_STATION)
                .build()
                .execute();
    }

    public void showPlaceExerciseInformation(LatLng location) {
        _googleMap.clear();

        if (previous_marker != null) {
            previous_marker.clear();
        }

        new Builder()
                .listener(ActivityMarkerActivity.this)
                .key("AIzaSyB_X_VLyr9sUV-8c25918Dhr9U9oCn6IXo")
                .latlng(location.latitude, location.longitude)
                .radius(5000)
                .type(CAFE)
                .build()
                .execute();
    }

    public void showPlaceBreadInformation(LatLng location) {
        _googleMap.clear();

        if (previous_marker != null) {
            previous_marker.clear();
        }

        new Builder()
                .listener(ActivityMarkerActivity.this)
                .key("AIzaSyB_X_VLyr9sUV-8c25918Dhr9U9oCn6IXo")
                .latlng(location.latitude, location.longitude)
                .radius(500)
                .type(BAKERY)
                .build()
                .execute();
    }

    private void showDialogForLocationSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMarkerActivity.this);

        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");

        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {

                    needRequest = true;
                }
            }
        }
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
        e.printStackTrace();
    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        runOnUiThread(new Runnable() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void run() {
                for (noman.googleplaces.Place place: places) {

                    @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(activity_marker_location);

                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    Bitmap markerBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                    String markerSnippet = getCurrentAddress(latLng);

                    MarkerOptions markerPlaceOptions = new MarkerOptions();
                    markerPlaceOptions.position(latLng);
                    markerPlaceOptions.title(place.getName());
                    markerPlaceOptions.snippet(markerSnippet);
                    markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));

                    getBreadMarkerImageSettings(place, markerPlaceOptions, latLng, markerSnippet);
                    getCoffeeMarkerImageSettings(place, markerPlaceOptions, latLng, markerSnippet);

                    _googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(@NonNull Marker marker) {
                            _googleMap.addPolyline(new PolylineOptions().add(latLng, latLng).width(5).color(Color.RED));

                            Intent intent = new Intent(ActivityMarkerActivity.this, ListViewActivity.class);
                            startActivity(intent);
                        }
                    });

                    _googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            cv_menu.setVisibility(View.VISIBLE);
                            tv_menu_marker_title.setText(marker.getTitle());
                            tv_menu_snippet_title.setText(marker.getSnippet());

                            return false;
                        }
                    });

                    _googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            cv_menu.setVisibility(View.INVISIBLE);
                        }
                    });

                    Marker item = _googleMap.addMarker(markerPlaceOptions);
                    previous_marker.add(item);
                }

                HashSet<Marker> hashSet = new HashSet<>(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
            }
        });
    }

    // 하드코딩
    private void getBreadMarkerImageSettings(noman.googleplaces.Place place, MarkerOptions markerPlaceOptions, LatLng latLng, String markerSnippet) {
        if (place.getName().contains("파리") || place.getName().contains("pari")) {
            setMarkerIconFromResource(markerPlaceOptions, latLng, place.getName(), markerSnippet, activity_parri_baguette, 120);
        }

        if (place.getName().contains("tous") || place.getName().contains("뚜레")) {
            setMarkerIconFromResource(markerPlaceOptions, latLng, place.getName(), markerSnippet, activity_tous_les_jours, 120);
        }

        if (place.getName().contains("던킨") || place.getName().contains("dunkin")) {
            setMarkerIconFromResource(markerPlaceOptions, latLng, place.getName(), markerSnippet, activity_dunkin, 120);
        }

        if (place.getName().equals("꽈배기")) {
            setMarkerIconFromResource(markerPlaceOptions, latLng, place.getName(), markerSnippet, activity_bread1, 120);
        }

    }

    private void setMarkerIconFromResource(MarkerOptions markerPlaceOptions, LatLng latLng, String placeName, String markerSnippet, int resource, int size) {
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(resource);

        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap markerBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);

        markerPlaceOptions.position(latLng);
        markerPlaceOptions.title(placeName);
        markerPlaceOptions.snippet(markerSnippet);
        markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));
    }

    private void getCoffeeMarkerImageSettings(noman.googleplaces.Place place, MarkerOptions markerPlaceOptions, LatLng latLng, String markerSnippet) {

        if (place.getName().contains("스타벅스") || place.getName().contains("Star")) {
            setCoffeeMarkerIconFromResource(markerPlaceOptions, latLng, place.getName(), markerSnippet, activity_star_bucks, 80);
        }
        if (place.getName().contains("메가커피") || place.getName().contains("Mega")) {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable megaBitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_mega_coffee);
            Bitmap megaBitmap = megaBitmapDrawable.getBitmap();
            Bitmap markerMegaBitmap = Bitmap.createScaledBitmap(megaBitmap, 80, 80, false);

            markerPlaceOptions.position(latLng);
            markerPlaceOptions.title(place.getName());
            markerPlaceOptions.snippet(markerSnippet);
            markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerMegaBitmap));
        }

        if (place.getName().contains("이디야") || place.getName().contains("Ediya")) {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable ediyaBitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_ediya);
            Bitmap ediyaBitmap = ediyaBitmapDrawable.getBitmap();
            Bitmap markerEdiyaBitmap = Bitmap.createScaledBitmap(ediyaBitmap, 120, 120, false);

            markerPlaceOptions.position(latLng);
            markerPlaceOptions.title(place.getName());
            markerPlaceOptions.snippet(markerSnippet);
            markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerEdiyaBitmap));
        }

        if (place.getName().contains("더카페") || place.getName().contains("The")) {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable theCafeBitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_the_coffee);
            Bitmap TheCafeBitmap = theCafeBitmapDrawable.getBitmap();
            Bitmap markerTheCafeBitmap = Bitmap.createScaledBitmap(TheCafeBitmap, 80, 80, false);

            markerPlaceOptions.position(latLng);
            markerPlaceOptions.title(place.getName());
            markerPlaceOptions.snippet(markerSnippet);
            markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerTheCafeBitmap));
        }

        if (place.getName().contains("컴포즈") || place.getName().contains("Compose")) {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable ComposeBitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_compose_coffee);
            Bitmap ComposeBitmap = ComposeBitmapDrawable.getBitmap();
            Bitmap markerComposeBitmap = Bitmap.createScaledBitmap(ComposeBitmap, 80, 80, false);

            markerPlaceOptions.position(latLng);
            markerPlaceOptions.title(place.getName());
            markerPlaceOptions.snippet(markerSnippet);
            markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerComposeBitmap));
        }
    }

    private void setCoffeeMarkerIconFromResource(MarkerOptions markerPlaceOptions, LatLng latLng, String name, String markerSnippet, int activity_star_bucks, int i) {
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable starBitmapDrawable = (BitmapDrawable)getResources().getDrawable(activity_star_bucks);
        Bitmap starBitmap = starBitmapDrawable.getBitmap();
        Bitmap markerStarBitmap = Bitmap.createScaledBitmap(starBitmap, 80, 80, false);

        markerPlaceOptions.position(latLng);
        markerPlaceOptions.title(place.getName());
        markerPlaceOptions.snippet(markerSnippet);
        markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerStarBitmap));
    }

    @Override
    public void onPlacesFinished() {

    }
}
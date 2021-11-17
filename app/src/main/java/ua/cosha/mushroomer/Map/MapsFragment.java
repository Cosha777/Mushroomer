package ua.cosha.mushroomer.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.cosha.mushroomer.R;
import ua.cosha.mushroomer.data.model.User;


public class MapsFragment extends Fragment {

    private String name = "Hello";




    private double myLatitude, myLongitude;
    private GoogleMap myMap;
    private float distance;
    private LocationManager locationManager;
    private boolean zoom = false;
    List<User> userPlaces = new ArrayList<>();
    private static final String USER_KEY = "USER";
    private static String uId;
    private  static DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyLocation();
        putDataToDB("Artem", myLatitude, myLongitude);
    }


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            myMap = googleMap;
            createMyMap();
        }
    };


    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 1, locationListener);

            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
    }


    private void getDistance(Double latitude, Double longitude) {
        Location locationB = new Location("locationB");
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);
        Location myLocation = new Location("mylocation");
        myLocation.setLatitude(myLatitude);
        myLocation.setLongitude(myLongitude);
        distance = myLocation.distanceTo(locationB);
    }


    private void createMyMap() {
        if (!zoom) {
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), 13));
            zoom = true;
        }
        getData();
        for (int i = 0; i < userPlaces.size(); i++) {

            if(!userPlaces.get(i).getId().equals(uId)){
                getDistance(userPlaces.get(i).latitude, userPlaces.get(i).longitude);
                myMap.addMarker(new MarkerOptions().position(new LatLng(userPlaces.get(i).getLatitude(), userPlaces.get(i).getLongitude())).title(userPlaces.get(i).getName()).snippet(String.valueOf(Math.round(distance)) + "  метров"));
                myMap.addPolyline(new PolylineOptions().add(new LatLng(myLatitude, myLongitude), new LatLng(userPlaces.get(i).getLatitude(), userPlaces.get(i).getLongitude())));

            }

//           markers[i] = new MarkerOptions().position(places.get(i)).title("qwe").snippet(String.valueOf(Math.round(distance)) + "  метров").icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.sing_in100, "your text goes here")));
        }
    }

    public void getData(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(userPlaces.size() > 0)  userPlaces.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    userPlaces.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        database.addValueEventListener(valueEventListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(requireContext(), myLatitude + "   " + myLongitude, Toast.LENGTH_LONG).show();
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
            myMap.clear();
            callback.onMapReady(myMap);
//            SetData.refreshData("Artem", myLatitude, myLongitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        removeDataFromDB();
    }

    public static void  putDataToDB(String name, double latitude, double longitude){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentFirebaseUser != null;
        uId = currentFirebaseUser.getUid();

        database = FirebaseDatabase.getInstance().getReference(USER_KEY);
        User user = new User(uId, name, latitude, longitude);
        database.push().setValue(user);

    }

    public static void  removeDataFromDB(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(USER_KEY);
        Query query = database.orderByChild("uId").equalTo(uId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public  static void refreshData(String name, double latitude, double longitude){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference(USER_KEY);
        Query query = database.orderByChild("id").equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("latitude", latitude);
                childUpdates.put("longitude", longitude);
                childUpdates.put("name", name);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().updateChildren(childUpdates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
package ua.cosha.mushroomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ua.cosha.mushroomer.Login.LoginFragment;
import ua.cosha.mushroomer.Map.MapsFragment;
import ua.cosha.mushroomer.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE = 1;
    private Boolean permissionGranted = false;

    ImageButton map, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        map = findViewById(R.id.btnMap);
        login = findViewById(R.id.btnLogin);



        onRequestPermission();



    }
    public  void  onRequestPermission(){
        String [] permission = {FINE_LOCATION, COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this,FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                permissionGranted = true;
            }else {
                ActivityCompat.requestPermissions(this, permission, REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionGranted = false;
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permissionGranted = false;
                        return;
                    }
                }
                permissionGranted = true;
            }
        }
    }




    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.btnLogin:
                fragment = new LoginFragment();
                break;

            case R.id.btnMap:
                fragment = new MapsFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        assert fragment != null;
        ft.replace(R.id.containerMain, fragment);
        ft.commit();


    }
}



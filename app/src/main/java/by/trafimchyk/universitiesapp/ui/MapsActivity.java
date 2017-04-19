package by.trafimchyk.universitiesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import by.trafimchyk.universitiesapp.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String ARG_ADDRESS = "ADDRESS";

    private Address mAddress;

    private GoogleMap mMap;

    public static void startMapsActivity(Context context, Address address) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(ARG_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        if (getIntent().getExtras().isEmpty()) {
            finish();
        } else {
            mAddress = getIntent().getParcelableExtra(ARG_ADDRESS);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initAddress();
    }

    private void initAddress() {
        if (mAddress.getMaxAddressLineIndex() > 0) {
            TextView addressView = ButterKnife.findById(this, R.id.tv_address);
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < mAddress.getMaxAddressLineIndex() - 1; index++) {
                builder.append(mAddress.getAddressLine(index))
                        .append(", ");
            }
            builder.append(mAddress.getAddressLine(mAddress.getMaxAddressLineIndex() - 1));
            addressView.setText(builder.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng university = new LatLng(mAddress.getLatitude(), mAddress.getLongitude());
        mMap.addMarker(new MarkerOptions().position(university).title(mAddress.getFeatureName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(university, 10));
    }

    @OnClick(R.id.btn_get_directions)
    void onDirectionsClick() {
        String url = String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%f,%f",
                mAddress.getLatitude(), mAddress.getLongitude());
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}

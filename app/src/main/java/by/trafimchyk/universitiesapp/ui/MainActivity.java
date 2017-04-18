package by.trafimchyk.universitiesapp.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import by.trafimchyk.universitiesapp.R;
import by.trafimchyk.universitiesapp.api.ApiProvider;
import by.trafimchyk.universitiesapp.api.ApiResponse;
import by.trafimchyk.universitiesapp.api.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.spinner_choose_country)
    AppCompatSpinner countriesView;
    @BindView(R.id.spinner_choose_city)
    AppCompatSpinner citiesView;
    @BindView(R.id.spinner_choose_university)
    AppCompatSpinner universitiesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.search);

        // init with defaults
        refreshItems(countriesView, new ArrayList<Item>(), getString(R.string.country));
        refreshItems(citiesView, new ArrayList<Item>(), getString(R.string.city));
        refreshItems(universitiesView, new ArrayList<Item>(), getString(R.string.university));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ApiProvider.getApi().getCountries().enqueue(new Callback<ApiResponse<Item>>() {
            @Override
            public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                refreshItems(countriesView, response.body().getItems(), getString(R.string.country));
            }

            @Override
            public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load countries", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshItems(AppCompatSpinner spinnerView, List<Item> items, String label) {
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, items);
        Item labelItem = new Item(-1, label);
        adapter.insert(labelItem, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerView.setAdapter(adapter);
    }

    @OnItemSelected(R.id.spinner_choose_country)
    void onCountrySelected(int position) {
        if (position != 0) {
            Item country = (Item) countriesView.getSelectedItem();
            ApiProvider.getApi().getCities(country.getId()).enqueue(new Callback<ApiResponse<Item>>() {
                @Override
                public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                    refreshItems(citiesView, response.body().getItems(), getString(R.string.city));
                }

                @Override
                public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnItemSelected(R.id.spinner_choose_city)
    void onCitySelected(int position) {
        if (position != 0) {
            Item country = (Item) countriesView.getSelectedItem();
            Item city = (Item) citiesView.getSelectedItem();
            ApiProvider.getApi().getUniversities(country.getId(), city.getId()).enqueue(new Callback<ApiResponse<Item>>() {
                @Override
                public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                    refreshItems(universitiesView, response.body().getItems(), getString(R.string.university));
                }

                @Override
                public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to load universities", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_search)
    void onSearchClick() {
        if (areSelectionsValid()) {
            if (Geocoder.isPresent()) {
                Item university = (Item) universitiesView.getSelectedItem();
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(university.getTitle(), 1);
                    if (addresses.isEmpty()) {
                        Toast.makeText(this, "Can't fetch location for selected place", Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = String.format(Locale.getDefault(), "Location for %s: lat=%f | lng=%f", university.getTitle(),
                                addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Can't fetch location for selected place", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Can't fetch location for selected place", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please, choose valid place", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areSelectionsValid() {
        return countriesView.getSelectedItemPosition() != 0 && citiesView.getSelectedItemPosition() != 0
                && universitiesView.getSelectedItemPosition() != 0;
    }
}

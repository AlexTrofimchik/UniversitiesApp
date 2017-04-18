package by.trafimchyk.universitiesapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("database.getCountries?v=5.63&need_all=1&count=1000")
    Call<ApiResponse<Item>> getCountries();

    @GET("database.getCities&v=5.63&need_all=1&count=1000")
    Call<ApiResponse<Item>> getCities(@Query("country_id") long countryId);

    @GET("database.getUniversities?v=5.63&need_all=1&count=1000")
    Call<ApiResponse<Item>> getUniversities(@Query("country_id") long countryId, @Query("city_id") long cityId);
}

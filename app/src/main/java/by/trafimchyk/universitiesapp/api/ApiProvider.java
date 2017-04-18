package by.trafimchyk.universitiesapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider {

    public static final String BASE_URL = "https://api.vk.com/method/";

    private static final ApiProvider sInstance;

    static {
        sInstance = new ApiProvider();
    }

    private Api mApi;

    public static Api getApi() {
        return sInstance.mApi;
    }

    private ApiProvider() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create(); // customize if needed
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        mApi = retrofit.create(Api.class);
    }
}

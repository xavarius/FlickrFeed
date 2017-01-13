package com.malak.yaim.services;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.malak.yaim.model.FlickrFeed;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.malak.yaim.services.FlickrService.FlickrAPI.FLICKR_ENDPOINT;

public class FlickrService {
  private FlickrAPI mFlickrAPI;

  public FlickrService() {
    buildFlickrService();
  }

  public FlickrAPI getFlickrAPI() {
    return mFlickrAPI;
  }

  private void buildFlickrService() {
    final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(FLICKR_ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    mFlickrAPI = retrofit.create(FlickrAPI.class);
  }

  public interface FlickrAPI {
    String FLICKR_ENDPOINT
        = "https://api.flickr.com/services/feeds/";

    String RESPONSE_FORMAT = "json";
    String NO_CALLBACK_METHOD = "?";
    String TAGS_MODE = "any";

    @GET("photos_public.gne?format=" + RESPONSE_FORMAT + "&nojsoncallback=" + NO_CALLBACK_METHOD)
    Observable<FlickrFeed> getPublicFeed();

    /**
     * @param formattedTags - A comma delimited list of tags to filter the feed by.
     * */
    @GET("photos_public.gne?format=" + RESPONSE_FORMAT + "&nojsoncallback=" + NO_CALLBACK_METHOD +
         "&tagmode=" + TAGS_MODE + "&tags={tags}")
    Observable<FlickrFeed> getPublicFeedByTags(@Path("tags") String formattedTags);
  }
}
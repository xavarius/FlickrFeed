package com.malak.yaim.presentation;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.malak.yaim.model.Item;
import com.malak.yaim.services.FlickrService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class FeedPresenter {
  @Inject FlickrService mService;

  @Inject public FeedPresenter() {}

  public void onCreated() {
    loadFeed();
  }

  public void onResumed() {
    loadFeed();
  }

  /**
   * Observe and subscribe to Flickr service. (trigger image download)
   * Updates UI with feeds' images or with error messages.
   *
   * @see FlickrService
   **/
  private void loadFeed() {
    mService.getFlickrAPI()
        .getPublicFeed()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(flickrFeed -> {
          final List<String> urls = new ArrayList<>();
          for (Item i : flickrFeed.getItems()) {
            urls.add(i.getMedia().getM());
          }
          return urls;
        })
        .doOnNext(list -> {
          /*TODO Update UI adapter with list of URLs*/
        })
        .doOnError(throwable -> {
          /*TODO Update UI with error*/
          if (throwable instanceof HttpException) {
            ((HttpException) throwable).code();
          }
        })
        .subscribe();
  }
}
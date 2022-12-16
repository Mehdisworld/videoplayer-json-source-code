package com.mezdev.player.video_player.AdsManager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mezdev.player.video_player.Utils.UtilsData;
import com.mezdev.player.video_player.activity.home;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by DEV MEZIOUD
 */

public class AdsController {


    public static int adCounter = 1;
    static InterstitialAd mInterstitialAd;
    static com.facebook.ads.InterstitialAd interstitialFacebookAd;
    static InterstitialAdListener interstitialAdListener;


    //GetAdsFromJson
    public static void GetAdsUnit(Activity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, UtilsData.UrlDefault, null,
                response -> {
                    try {

                        JSONArray Ads = response.getJSONArray("data");

                        for (int i = 0; i < Ads.length(); i++) {
                            JSONObject object = Ads.getJSONObject(i);

                            UtilsData.adsAdmobInterstitial = object.getString("AdmobInterstitial");
                            UtilsData.adsAdmobBanner = object.getString("AdmobBanner");

                            UtilsData.adsFanInterstitial = object.getString("FanInterstitial");
                            UtilsData.adsFanBanner = object.getString("FanBanner");
                            UtilsData.adsCount = object.getInt("AdsCount");

                            UtilsData.adsSwitch = object.getString("Switch");


                        }


                            Intent intent = new Intent(activity, home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            activity.startActivity(intent);
                            activity.finish();


                    } catch (Exception e) {
                        e.printStackTrace();

                        Toast.makeText(activity, "There is something wrong with the server", Toast.LENGTH_LONG).show();

                    }

                }, error -> Log.e("VOLLEY", "ERROR")
        );

        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    //LoadAds
    public static void LoadAdsBanner(Activity activity, FrameLayout bannerLayout) {

        switch (UtilsData.adsSwitch) {
            case "stop":
                break;
            case "admob":
                InitializedAdmob(activity);
                LoadAdmobBanner(bannerLayout, activity);
                break;
            case "fan":
                LoadFanBanner(bannerLayout, activity);
                break;
        }


    }

    public static void LoadAdsInterstitial(Activity activity) {

        switch (UtilsData.adsSwitch) {
            case "stop":
                break;
            case "admob":
                InitializedAdmob(activity);
                LoadAdmobInterstitial(activity);
                break;
            case "fan":
                LoadFanInterstitial(activity);
                break;
        }

    }
    public static void ShowAdsInterstitial(final Activity activity, final Intent intent, final int requestCode) {

        switch (UtilsData.adsSwitch) {
            case "stop":
                break;
            case "admob":
                ShowAdmobInterstitial(activity, intent, requestCode);
                break;
            case "fan":
                ShowFanInterstitial(activity, intent, requestCode);
                break;

        }

    }

    //InitializedAds
    public static void InitializedAdmob(Activity activity) {
        MobileAds.initialize(activity, initializationStatus -> {

            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                assert status != null;
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
        });
    }

    //LoadAdsBanner
    public static void LoadAdmobBanner(FrameLayout bannerLayout, Activity activity) {

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(activity);
        bannerLayout.addView(adView);
        adView.setAdUnitId(UtilsData.adsAdmobBanner);
        AdSize adSize = new AdSize(300, 50);
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);

    }
    public static void LoadFanBanner(FrameLayout bannerLayout, Activity activity) {

        com.facebook.ads.AdView adBannerFace = new com.facebook.ads.AdView(activity, UtilsData.adsFanBanner, com.facebook.ads.AdSize.BANNER_320_50);
        bannerLayout.addView(adBannerFace);
        adBannerFace.loadAd();

    }

    //Load&ShowAdsInterstitial
    public static void LoadAdmobInterstitial(Activity activity) {

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, UtilsData.adsAdmobInterstitial,
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


    }
    public static void ShowAdmobInterstitial(final Activity activity, final Intent intent, final int requestCode) {
        if (adCounter == UtilsData.adsCount && mInterstitialAd != null) {
            adCounter = 1;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {

                    LoadAdmobInterstitial(activity);
                    startActivity(activity, intent, requestCode);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {

                }

                @Override
                public void onAdShowedFullScreenContent() {

                    mInterstitialAd = null;
                }
            });
            mInterstitialAd.show(activity);
        } else {
            if (adCounter == UtilsData.adsCount) {
                adCounter = 1;
            }
            startActivity(activity, intent, requestCode);
        }
    }

    public static void LoadFanInterstitial(Activity activity) {

        interstitialFacebookAd = new com.facebook.ads.InterstitialAd(activity, UtilsData.adsFanInterstitial);

        interstitialFacebookAd.loadAd(
                interstitialFacebookAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());


    }
    public static void ShowFanInterstitial(final Activity activity, final Intent intent, final int requestCode) {
        if (adCounter == UtilsData.adsCount && interstitialFacebookAd != null) {
            adCounter = 1;


            interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {

                    LoadFanInterstitial(activity);
                    startActivity(activity, intent, requestCode);

                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    interstitialFacebookAd = null;

                }

                @Override
                public void onAdLoaded(Ad ad) {

                }


                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };

            interstitialFacebookAd.show();

        } else {
            if (adCounter == UtilsData.adsCount) {
                adCounter = 1;
            }
            startActivity(activity, intent, requestCode);
        }
    }


    //StartActivity
    static void startActivity(Activity activity, Intent intent, int requestCode) {
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

}
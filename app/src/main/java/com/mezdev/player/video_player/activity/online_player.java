package com.mezdev.player.video_player.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mezdev.player.video_player.AdsManager.AdsController;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.fragment.EndlessRecyclerViewScrollListener;
import com.mezdev.player.video_player.fragment.home_category_list;
import com.mezdev.player.video_player.fragment.ol_play_adepter;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class

online_player extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static String video_detail = "";
    private static String JSON_URL = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=IN&maxResults=10&key=AIzaSyBM-EZzcKthefWDSv_RsXwYgvy3gRK6dqs";
    YouTubePlayerView youtubePlayerView;
    private ArrayList<home_category_list> tutorialList;
    public static ol_play_adepter adapter;
    String videoId = "";
    String channelId = "";
    TextView tv_yv_title, tv_yv_upload_date, tv_yv_detail,  tv_yv_view;
    LinearLayout lil_share, lil_whatsapp;
    Button diogs_hide, diogs_show;
    String nextPageToken = "";
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_player);

        FrameLayout BannerAds   = findViewById(R.id.banner);
        AdsController.LoadAdsBanner(this, BannerAds);

        find_view_by_id();
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        final Bundle extras = getIntent().getExtras();
        videoId = extras.getString("url_id");

        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(videoId, 0);
                    }
                });
            }
        }, true);
        video_detail = "https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&id=" + videoId + "&key=AIzaSyBM-EZzcKthefWDSv_RsXwYgvy3gRK6dqs";

        video_ditail_fetch();
        more_ditails();
        Share_url();
        tutorialList = new ArrayList<>();
        loadTutorialList();
    }

    private void find_view_by_id() {
        recyclerView = (RecyclerView) findViewById(R.id.ol_category_recycler_view);
        tv_yv_title = (TextView) findViewById(R.id.tv_yv_title);
        tv_yv_upload_date = (TextView) findViewById(R.id.tv_yv_upload_date);
        tv_yv_view = (TextView) findViewById(R.id.tv_yv_view);
        tv_yv_detail = (TextView) findViewById(R.id.tv_yv_detail);
        lil_share = (LinearLayout) findViewById(R.id.lil_share);
        lil_whatsapp = (LinearLayout) findViewById(R.id.lil_whatsapp);
        diogs_hide = (Button) findViewById(R.id.diogs_hide);
        diogs_show = (Button) findViewById(R.id.diogs_show);
    }

    private void video_ditail_fetch() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, video_detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject tutorialsObject = obj.getJSONArray("items").getJSONObject(0);

                            String currentString = tutorialsObject.getJSONObject("snippet").getString("publishedAt");
                            String[] separated = currentString.split("T");

                            channelId = tutorialsObject.getJSONObject("snippet").getString("title");
                            tv_yv_title.setText(tutorialsObject.getJSONObject("snippet").getString("title"));
                            tv_yv_view.setText(tutorialsObject.getJSONObject("statistics").getString("viewCount"));
                            tv_yv_upload_date.setText(separated[0]);
                            tv_yv_detail.setText(tutorialsObject.getJSONObject("snippet").getString("description"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void Share_url() {
       lil_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing VIdeo");
                i.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + videoId);
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });

        lil_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + videoId);
                startActivity(intent);
            }
        });
    }

    private void more_ditails() {
        diogs_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diogs_show.setVisibility(View.VISIBLE);
                diogs_hide.setVisibility(View.GONE);
                tv_yv_detail.setVisibility(View.VISIBLE);

            }
        });
        diogs_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diogs_show.setVisibility(View.GONE);
                diogs_hide.setVisibility(View.VISIBLE);
                tv_yv_detail.setVisibility(View.GONE);
            }
        });
    }


    public void recyclerView_code() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetch_new_data();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void fetch_new_data() {
        String JSON_URL_NEW = "https://www.googleapis.com/youtube/v3/videos?pageToken=" + nextPageToken + "&part=contentDetails&chart=mostPopular&regionCode=IN&maxResults=10&key=AIzaSyBM-EZzcKthefWDSv_RsXwYgvy3gRK6dqs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_NEW,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            nextPageToken = obj.getString("nextPageToken");
                            JSONArray tutorialsArray = obj.getJSONArray("items");
                            for (int i = 0; i < tutorialsArray.length(); i++) {
                                JSONObject tutorialsObject = tutorialsArray.getJSONObject(i);
                                if (!tutorialsObject.getJSONObject("contentDetails").has("regionRestriction")) {
                                    if (!tutorialsObject.getString("id").equals(videoId)) {
                                        tutorialList.add(new home_category_list(tutorialsObject.getString("id")));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadTutorialList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            nextPageToken = obj.getString("nextPageToken");
                            JSONArray tutorialsArray = obj.getJSONArray("items");
                            for (int i = 0; i < tutorialsArray.length(); i++) {
                                JSONObject tutorialsObject = tutorialsArray.getJSONObject(i);
                                if (!tutorialsObject.getJSONObject("contentDetails").has("regionRestriction")) {
                                    if (!tutorialsObject.getString("id").equals(videoId)) {
                                        tutorialList.add(new home_category_list(tutorialsObject.getString("id")));
                                    }
                                }
                            }
                            adapter = new ol_play_adepter(online_player.this, tutorialList);
                            recyclerView_code();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}

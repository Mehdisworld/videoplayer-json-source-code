package com.mezdev.player.video_player.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.mezdev.player.video_player.Utils.AppPref;
import com.mezdev.player.video_player.Utils.CommonUtilities;
import com.mezdev.player.video_player.fragment.home_category_list;
import com.mezdev.player.video_player.fragment.ol_home_adapter;
import com.mezdev.player.video_player.fragment.ol_slider;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class online extends AppCompatActivity {
    private ImageView img_ic_search;
    private ImageView img_gift;
    AppPref objpref;
    LinearLayout lil_local, lil_online, lil_action_bar;
    RelativeLayout rlo_screen, rlo_search_bar;

    private XRecyclerView recyclerView;
    public static ol_home_adapter adapter;
    private static final String JSON_URL = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=IN&maxResults=10&key=AIzaSyBM-EZzcKthefWDSv_RsXwYgvy3gRK6dqs";
    private ArrayList<home_category_list> tutorialList;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    String nextPageToken = "";
    LinearLayoutManager mLayoutManager;
    private View mEmptyView;
    SliderLayout sliderLayout;
    private static final String SLIDER_JSON_URL = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=IN&key=AIzaSyBM-EZzcKthefWDSv_RsXwYgvy3gRK6dqs";
    private static final String Serch_list_json = "";
    ImageView img_back, img_clear;
    EditText et_search;
    LinearLayout ol_action_bar,bottom_navigation;
    private ImageView iv_color;
    private TextView tv_color;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        objpref = new AppPref(online.this);
        if (objpref.getTheem() == 1) {
            setTheme(R.style.AppTheme);
        } else if (objpref.getTheem() == 2) {
            this.setTheme(R.style.AppTheme_2);
        } else if (objpref.getTheem() == 3) {
            this.setTheme(R.style.AppTheme_3);
        } else if (objpref.getTheem() == 4) {
            this.setTheme(R.style.AppTheme_4);
        } else if (objpref.getTheem() == 5) {
            this.setTheme(R.style.AppTheme_5);
        } else if (objpref.getTheem() == 6) {
            this.setTheme(R.style.AppTheme_6);
        } else {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        FrameLayout BannerAds   = findViewById(R.id.banner);
        AdsController.LoadAdsBanner(this, BannerAds);
        AdsController.LoadAdsInterstitial(this);

        find_view_by_id();
        bottom_navigation();
        change_theem();
        search_code();

        tutorialList = new ArrayList<>();
        loadTutorialList();

    }

    private void find_view_by_id() {
        img_gift = findViewById(R.id.img_gift);
        img_ic_search = (ImageView) findViewById(R.id.img_ic_search);
        lil_local = (LinearLayout) findViewById(R.id.lil_local);
        lil_online = (LinearLayout) findViewById(R.id.lil_online);
        ol_action_bar = (LinearLayout) findViewById(R.id.ol_action_bar);
        bottom_navigation = (LinearLayout) findViewById(R.id.bottom_navigation);
        recyclerView = (XRecyclerView) findViewById(R.id.ol_category_recycler_view);
        rlo_screen = (RelativeLayout) findViewById(R.id.rlo_screen);
        rlo_search_bar = (RelativeLayout) findViewById(R.id.rlo_search_bar);
        lil_action_bar = (LinearLayout) findViewById(R.id.lil_action_bar);
        et_search = (EditText) findViewById(R.id.et_search);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_clear = (ImageView) findViewById(R.id.img_clear);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        tv_color = (TextView) findViewById(R.id.tv_color);
    }

    private void bottom_navigation() {
        lil_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdsController.adCounter++;
                AdsController.ShowAdsInterstitial(online.this, null, 0);
            }
        });
        lil_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(online.this, home.class);
                AdsController.adCounter++;
                AdsController.ShowAdsInterstitial(online.this, intent, 0);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void change_theem() {
        objpref = new AppPref(online.this);
        if (objpref.getTheem() == 1) {
            this.setTheme(R.style.AppTheme);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary);
            iv_color.setColorFilter(R.color.colorPrimary);
            tv_color.setTextColor(R.color.colorPrimary);
        } else if (objpref.getTheem() == 2) {
            this.setTheme(R.style.AppTheme_2);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_2));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_2));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary_2);
            iv_color.setColorFilter(R.color.colorPrimary_2);
            tv_color.setTextColor(R.color.colorPrimary_2);
        }  else if (objpref.getTheem() == 3) {
            this.setTheme(R.style.AppTheme_3);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_3));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_3));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary_3);
            iv_color.setColorFilter(R.color.colorPrimary_3);
            tv_color.setTextColor(R.color.colorPrimary_3);
        }  else if (objpref.getTheem() == 4) {
            this.setTheme(R.style.AppTheme_4);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_4));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_4));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary_4);
            iv_color.setColorFilter(R.color.colorPrimary_4);
            tv_color.setTextColor(R.color.colorPrimary_4);
        }  else if (objpref.getTheem() == 5) {
            this.setTheme(R.style.AppTheme_5);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_5));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_5));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary_5);
            iv_color.setColorFilter(R.color.colorPrimary_5);
            tv_color.setTextColor(R.color.colorPrimary_5);
        }  else if (objpref.getTheem() == 6) {
            this.setTheme(R.style.AppTheme_6);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_6));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_6));
            ol_action_bar.setBackgroundResource(R.color.colorPrimary_6);
            iv_color.setColorFilter(R.color.colorPrimary_6);
            tv_color.setTextColor(R.color.colorPrimary_6);
        } else {

        }


    }

    private void search_code() {
        img_ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlo_search_bar.setVisibility(View.VISIBLE);
                lil_action_bar.setVisibility(View.GONE);
                bottom_navigation.setVisibility(View.GONE);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rlo_screen);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
                if(fragment instanceof ol_slider)
                {
                }
                else
                {
                    rlo_search_bar.setVisibility(View.GONE);
                    lil_action_bar.setVisibility(View.VISIBLE);
                    bottom_navigation.setVisibility(View.VISIBLE);
                    et_search.setText(null);
                }


            }
        });
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(null);
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = String.valueOf(et_search.getText());
                    if (!text.equals(null) && !text.equals("")) {
                        ol_slider frag = new ol_slider();
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.rlo_screen, frag, null);
                        transaction.commit();

                        Bundle arguments = new Bundle();
                        arguments.putString("search_string", text);
                        frag.setArguments(arguments);

                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rlo_screen);
        if(fragment instanceof ol_slider)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }

                rlo_search_bar.setVisibility(View.GONE);
                lil_action_bar.setVisibility(View.VISIBLE);
                bottom_navigation.setVisibility(View.VISIBLE);
                et_search.setText(null);
        }else
        {
            startActivity(new Intent(online.this,home.class));
            finish();


        }
    }

    private void setSliderViews() {
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        sliderLayout.setScrollTimeInSec(1);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, SLIDER_JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray tutorialsArrays = obj.getJSONArray("items");
                            for (int i = 0; i < tutorialsArrays.length(); i++) {

                                JSONObject tutorialsObject = tutorialsArrays.getJSONObject(i);
                                SliderView sliderView = new SliderView(online.this);
                                sliderView.setImageUrl("http://img.youtube.com/vi/" + tutorialsObject.getString("id") + "/0.jpg");
                                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                new CommonUtilities.LongOperation(sliderView, tutorialsObject.getString("id")).execute(tutorialsObject.getString("id"));
                                final String finalI = tutorialsObject.getString("id");
                                sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(SliderView sliderView) {
                                        Intent intent = new Intent(online.this, online_player.class);
                                        intent.putExtra("url_id", finalI);
                                        startActivity(intent);

                                    }
                                });
                                sliderLayout.addSliderView(sliderView);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        RequestQueue requestQueue = Volley.newRequestQueue(online.this);
        requestQueue.add(stringRequest);
    }

    public void recyclerView_code() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(false);
        View header = LayoutInflater.from(this).inflate(R.layout.slider_header, (ViewGroup) findViewById(android.R.id.content), false);
        sliderLayout = (SliderLayout) header.findViewById(R.id.imageSlider);
        setSliderViews();
        recyclerView.addHeaderView(header);
        recyclerView.setAdapter(adapter);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
                fetch_new_data();
            }
        });
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
                                    tutorialList.add(new home_category_list(tutorialsObject.getString("id")));
                                }
                            }
                            recyclerView.loadMoreComplete();
                            adapter.notifyDataSetChanged();
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
                                    tutorialList.add(new home_category_list(tutorialsObject.getString("id")));
                                }
                            }
                            adapter = new ol_home_adapter(online.this, tutorialList);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}

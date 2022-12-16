package com.mezdev.player.video_player.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mezdev.player.video_player.Adapter.DrawerItemCustomAdapter;
import com.mezdev.player.video_player.AdsManager.AdsController;
import com.mezdev.player.video_player.Equilizer.VideoPlayerGestures.equlizer;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.SquareFrameLayout;
import com.mezdev.player.video_player.Utils.AppPref;
import com.mezdev.player.video_player.fragment.f_folder;
import com.mezdev.player.video_player.fragment.f_video;
import com.mezdev.player.video_player.fragment.folder_video_list;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {
    private Toolbar toolbar;
    private int mSortingType = 2;

    public static TabLayout tabLayout;
    private ViewPager viewPager;
    public static AppCompatActivity activity;
    DrawerLayout drawer;
    private static final String SORT_TYPE_PREFERENCE_KEY = "sort_type";

    Toolbar toolbar_2;
    public static int[] drawer_icons = {R.drawable.ic_video,
            R.drawable.ic_directory,
            R.drawable.ic_equalizer,
            R.drawable.settings,
            R.drawable.ic_share,
            R.drawable.information,
            R.drawable.rate
    };

    TextView tv_selected_navigation;

    ArrayList<String> navigation_items;
    private com.mezdev.player.video_player.Adapter.DrawerItemCustomAdapter DrawerItemCustomAdapter;
    private ListView lv_drawer;
    private ImageView img_ic_menu;
    private ImageView img_ic_menu2;
    private ImageView img_ic_search;
    private ImageView img_gift;
    private TextView tv_title;

    private SquareFrameLayout layout_close;
    private SquareFrameLayout layout_gift;
    private SquareFrameLayout layout_search;
    private SquareFrameLayout layout_menu2;

    RelativeLayout search_layout;
    private EditText et_search;
    private ImageView img_close;

    private ImageView iv_color;
    private TextView tv_color;


    public static final int NAME_ASC = 0;
    public static final int NAME_DESC = 1;
    public static final int DATE_ASC = 2;
    public static final int DATE_DESC = 3;
    public static final int SIZE_ASC = 4;
    public static final int SIZE_DESC = 5;

    AppPref objpref;

    LinearLayout lil_local, lil_online;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        objpref = new AppPref(home.this);
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
        setContentView(R.layout.activity_home);


        FrameLayout BannerAds   = findViewById(R.id.banner);
        AdsController.LoadAdsBanner(this, BannerAds);
        AdsController.LoadAdsInterstitial(this);



        String[] videoParams = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Thumbnails.DATA};

        activity = home.this;

        find_view_by_id();
        bottom_navigation();
        change_theem();
        init();
        view_pager();
        search_code();


    }

    private void find_view_by_id() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lv_drawer = (ListView) findViewById(R.id.lv_drawer);
        img_ic_menu = findViewById(R.id.img_ic_menu);
        img_ic_menu2 = findViewById(R.id.img_ic_menu2);
        img_gift = findViewById(R.id.img_gift);
        img_ic_search = (ImageView) findViewById(R.id.img_ic_search);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_search = (EditText) findViewById(R.id.et_search);
        layout_close = (SquareFrameLayout) findViewById(R.id.layout_close);
        layout_gift = (SquareFrameLayout) findViewById(R.id.layout_gift);
        layout_search = (SquareFrameLayout) findViewById(R.id.layout_search);
        layout_menu2 = (SquareFrameLayout) findViewById(R.id.layout_menu2);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);
        img_close = (ImageView) findViewById(R.id.img_close);
        lil_local = (LinearLayout) findViewById(R.id.lil_local);
        lil_online = (LinearLayout) findViewById(R.id.lil_online);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        tv_color = (TextView) findViewById(R.id.tv_color);
    }

    private void bottom_navigation() {
        lil_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, online.class);
                AdsController.adCounter++;
                AdsController.ShowAdsInterstitial(home.this, intent, 0);
            }
        });
        lil_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdsController.adCounter++;
                AdsController.ShowAdsInterstitial(home.this, null , 0);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void change_theem() {
        objpref = new AppPref(home.this);
        if (objpref.getTheem() == 1) {
            this.setTheme(R.style.AppTheme);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setBackgroundResource(R.color.colorPrimary);
            iv_color.setColorFilter(R.color.colorPrimary);
            tv_color.setTextColor(R.color.colorPrimary);
        } else if (objpref.getTheem() == 2) {
            this.setTheme(R.style.AppTheme_2);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_2));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_2));
            toolbar.setBackgroundResource(R.color.colorPrimary_2);
            iv_color.setColorFilter(R.color.colorPrimary_2);
            tv_color.setTextColor(R.color.colorPrimary_2);
        } else if (objpref.getTheem() == 3) {
            this.setTheme(R.style.AppTheme_3);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_3));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_3));
            toolbar.setBackgroundResource(R.color.colorPrimary_3);
            iv_color.setColorFilter(R.color.colorPrimary_3);
            tv_color.setTextColor(R.color.colorPrimary_3);
        } else if (objpref.getTheem() == 4) {
            this.setTheme(R.style.AppTheme_4);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_4));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_4));
            toolbar.setBackgroundResource(R.color.colorPrimary_4);
            iv_color.setColorFilter(R.color.colorPrimary_4);
            tv_color.setTextColor(R.color.colorPrimary_4);
        } else if (objpref.getTheem() == 5) {
            this.setTheme(R.style.AppTheme_5);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_5));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_5));
            toolbar.setBackgroundResource(R.color.colorPrimary_5);
            iv_color.setColorFilter(R.color.colorPrimary_5);
            tv_color.setTextColor(R.color.colorPrimary_5);
        } else if (objpref.getTheem() == 6) {
            this.setTheme(R.style.AppTheme_6);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_6));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_6));
            toolbar.setBackgroundResource(R.color.colorPrimary_6);
            iv_color.setColorFilter(R.color.colorPrimary_6);
            tv_color.setTextColor(R.color.colorPrimary_6);
        } else {

        }


    }

    private void init() {

        setSupportActionBar(toolbar);

        navigation_items = new ArrayList<>();



        navigation_items.add("Video");
        navigation_items.add("Directory");
        navigation_items.add("Equalizer");
        navigation_items.add("Settings");
        navigation_items.add("Share");
        navigation_items.add("Privacy & Policy");
        navigation_items.add("Rate");


        DrawerItemCustomAdapter = new DrawerItemCustomAdapter(home.this, navigation_items, drawer_icons);
        lv_drawer.setAdapter(DrawerItemCustomAdapter);

        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (navigation_items.get(position).equalsIgnoreCase("Video")) {
                    viewPager.setCurrentItem(1, true);
                } else if (navigation_items.get(position).equalsIgnoreCase("Directory")) {
                    viewPager.setCurrentItem(0, true);
                } else if (navigation_items.get(position).equalsIgnoreCase("Equalizer")) {
                    toolbar.setVisibility(View.GONE);
                    equlizer fragment = new equlizer();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.MainContainer, fragment);
                    fragmentTransaction.commit();



                } else if (navigation_items.get(position).equalsIgnoreCase("Settings")) {

                    Intent intent = new Intent(home.this, setting.class);
                    startActivity(intent);

                } else if (navigation_items.get(position).equalsIgnoreCase("Share")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out my app at: https://play.google.com/store/apps/details?id="+home.this.getPackageName());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);


                } else if (navigation_items.get(position).equalsIgnoreCase("Privacy & Policy")) {

                    String url = "https://www.google.co.in/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);


                } else if (navigation_items.get(position).equalsIgnoreCase("Rate")) {
                    launchMarket();
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        img_ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        img_ic_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(home.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_main, popup.getMenu());
                if (mSortingType == 0) {
                    popup.getMenu().findItem(R.id.sort_name_asc).setChecked(true);
                } else if (mSortingType == 1) {
                    popup.getMenu().findItem(R.id.sort_name_dsc).setChecked(true);
                } else if (mSortingType == 2) {
                    popup.getMenu().findItem(R.id.sort_date_asc).setChecked(true);
                } else if (mSortingType == 3) {
                    popup.getMenu().findItem(R.id.sort_date_dsc).setChecked(true);
                } else if (mSortingType == 4) {
                    popup.getMenu().findItem(R.id.sort_size_asc).setChecked(true);
                } else if (mSortingType == 5) {
                    popup.getMenu().findItem(R.id.sort_size_dsc).setChecked(true);
                } else {
                }

                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.show();
            }
        });
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.sort_name_asc:
                    updateSharedPreferenceAndGetNewList(NAME_ASC);
                    menuItem.setChecked(true);
                    break;
                case R.id.sort_name_dsc:
                    updateSharedPreferenceAndGetNewList(NAME_DESC);
                    menuItem.setChecked(true);
                    break;
                case R.id.sort_date_asc:
                    updateSharedPreferenceAndGetNewList(DATE_ASC);
                    menuItem.setChecked(true);
                    break;
                case R.id.sort_date_dsc:
                    updateSharedPreferenceAndGetNewList(DATE_DESC);
                    menuItem.setChecked(true);
                    break;
                case R.id.sort_size_asc:
                    updateSharedPreferenceAndGetNewList(SIZE_ASC);
                    menuItem.setChecked(true);
                    break;
                case R.id.sort_size_dsc:
                    updateSharedPreferenceAndGetNewList(SIZE_DESC);
                    menuItem.setChecked(true);
                    break;
                default:
                    break;
            }

            return false;
        }
    }

    private void updateSharedPreferenceAndGetNewList(int sortType) {
        objpref.setShort_list(sortType);
        mSortingType = sortType;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
        if (fragment instanceof folder_video_list) {
            folder_video_list.adapter.getSortedArray();
        } else if (viewPager.getCurrentItem() == 1) {
            f_video.adapter.getSortedArray();
        } else if (viewPager.getCurrentItem() == 0) {
            f_folder.adapter.getSortedArray();
        }

    }


    private void view_pager() {
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
    }

    private void setupViewPager(final ViewPager viewPager) {

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new f_folder(), "Folder");
        adapter.addFragment(new f_video(), "Video");
        viewPager.setAdapter(adapter);
        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter1) {

            }
        });
/*
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!et_search.getText().toString().equals("") && et_search.getVisibility() == View.VISIBLE) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
                    if (fragment instanceof folder_video_list) {
                        folder_video_list.adapter.filter(et_search.getText().toString());
                    } else if (viewPager.getCurrentItem() == 1) {
                        if (f_video.adapter != null) {
                            f_video.adapter.filter(et_search.getText().toString());
                        }
                    } else if (viewPager.getCurrentItem() == 0) {
                        f_folder.adapter.filter(et_search.getText().toString());
                    } else {
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (et_search.getText().toString() != null && et_search.getVisibility() == View.VISIBLE) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
                    if (fragment instanceof folder_video_list) {
                        folder_video_list.adapter.filter(et_search.getText().toString( ));
                    } else if (viewPager.getCurrentItem() == 1) {
                        if (f_video.adapter != null) {
                            f_video.adapter.filter(et_search.getText().toString());
                        }
                    } else if (viewPager.getCurrentItem() == 0) {
                        f_folder.adapter.filter(et_search.getText().toString());
                    } else {
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (et_search.getText().toString() != null && et_search.getVisibility() == View.VISIBLE) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
                    if (fragment instanceof folder_video_list) {
                        folder_video_list.adapter.filter(et_search.getText().toString());
                    } else if (viewPager.getCurrentItem() == 1) {
                        if (f_video.adapter != null) {
                            f_video.adapter.filter(et_search.getText().toString());
                        }
                    } else if (viewPager.getCurrentItem() == 0) {
                        f_folder.adapter.filter(et_search.getText().toString());
                    } else {
                    }
                }

            }
        });
*/
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    private void search_code() {
        img_ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_title.setVisibility(View.GONE);
                layout_menu2.setVisibility(View.GONE);
                layout_gift.setVisibility(View.GONE);
                layout_search.setVisibility(View.GONE);

                search_layout.setVisibility(View.VISIBLE);

            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
                tv_title.setVisibility(View.VISIBLE);
                layout_gift.setVisibility(View.VISIBLE);
                layout_search.setVisibility(View.VISIBLE);
                layout_menu2.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                et_search.setText("");
                if (fragment instanceof folder_video_list) {
                    folder_video_list.adapter.filter("");
                } else if (viewPager.getCurrentItem() == 1) {
                    f_video.adapter.filter("");
                } else if (viewPager.getCurrentItem() == 0) {
                    f_folder.adapter.filter("");
                } else {
                }
//                InputMethodManager inputManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(home.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        img_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Capture Text in EditText
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                String text = et_search.getText().toString().toLowerCase(Locale.getDefault());
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
                if (fragment instanceof folder_video_list) {
                    folder_video_list.adapter.filter(text);
                } else if (viewPager.getCurrentItem() == 1) {
                    f_video.adapter.filter(text);
                } else if (viewPager.getCurrentItem() == 0) {
                    f_folder.adapter.filter(text);
                } else {
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onBackPressed() {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.MainContainer);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragment instanceof folder_video_list) {
                Log.e("call", "'visiblity");
                tabLayout.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
                f_folder.adapter.notifyDataSetChanged();
            } else if (fragment instanceof equlizer) {
                toolbar.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                equlizer.mVisualizer.release();
                equlizer.mEqualizer.release();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            } else {
                showAlertDialogButtonClicked();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            f_video.adapter.notifyDataSetChanged();
        }
    }

    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are You Sure You Want Exit?");

        // add the buttons
        builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchMarket();
            }
        });
        builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}

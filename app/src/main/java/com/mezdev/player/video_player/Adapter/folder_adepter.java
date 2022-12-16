package com.mezdev.player.video_player.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdev.player.video_player.AdsManager.AdsController;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.Utils.AppPref;
import com.mezdev.player.video_player.Utils.CommonUtilities;
import com.mezdev.player.video_player.activity.home;
import com.mezdev.player.video_player.fragment.folder_video_list;
import com.mezdev.player.video_player.model.folder_l;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class folder_adepter extends RecyclerView.Adapter<folder_adepter.MyViewHolder> {
    private final ArrayList<folder_l> folderList;
    private ArrayList<String> video_list_t;

    private ArrayList<folder_l> arraylist = null;


    private final Activity activity;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView folder_name, items, txt_newtab;
        RelativeLayout folder_item_parent;
        ImageView img_angle;

        MyViewHolder(View view) {
            super(view);
            txt_newtab = (TextView) view.findViewById(R.id.txt_newtab);
            folder_name = (TextView) view.findViewById(R.id.txt_folder_name);
            items = (TextView) view.findViewById(R.id.txt_folder_video_no);
            img_angle = (ImageView) view.findViewById(R.id.img_angle);
            folder_item_parent = (RelativeLayout) view.findViewById(R.id.folder_item_parent);
        }
    }


    public folder_adepter(Activity activity, Context context, ArrayList<folder_l> folder_list) {

        this.activity = activity;
        this.mContext = context;
        this.folderList = new ArrayList<folder_l>();
        this.arraylist = folder_list;
        this.folderList.addAll(folder_list);
        objpref = new AppPref(context);

        getSortedArray();


    }

    private AppPref objpref;

    public void getSortedArray() {

        final int sortType = objpref.getShort_list();
        Log.e("video_adepter_short", String.valueOf(sortType));

        Collections.sort(folderList, new Comparator<folder_l>() {
            public int compare(folder_l obj1, folder_l obj2) {
                if (sortType == 0) {
                    return obj1.getFolder_name().compareToIgnoreCase(obj2.getFolder_name());
                } else if (sortType == 1) {
                    return obj2.getFolder_name().compareToIgnoreCase(obj1.getFolder_name());
                } else if (sortType == 4) {
                    try {
                        return Integer.valueOf(obj1.getItems()).compareTo(Integer.valueOf(obj2.getItems()));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return 0;
                    }
                } else if (sortType == 5) {
                    try {
                        return Integer.valueOf(obj2.getItems()).compareTo(Integer.valueOf(obj1.getItems()));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return obj1.getFolder_name().compareToIgnoreCase(obj2.getFolder_name());
                }

            }
        });
        notifyDataSetChanged();
    }

    @Override
    public folder_adepter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_l, parent, false);

        return new folder_adepter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final folder_adepter.MyViewHolder holder, final int position) {
        final folder_l folder_l = folderList.get(position);
        AppPref objpref = new AppPref(activity);
        video_list_t = new ArrayList<>();
        int count = 0;

        video_list_t = CommonUtilities.getFolderMedia(mContext, folder_l.getFolder_name());

        for (int i = 0; i < video_list_t.size(); i++) {
            File f = new File(video_list_t.get(i));


            if (objpref.getNewtab() != null) {
                for (int z = 0; z < objpref.getNewtab().size(); z++) {

                    if (objpref.getNewtab().get(z).equals(f.getPath())) {
                        count = count + 1;
                    }
                }
            }
        }
        if (count == video_list_t.size()) {
            folder_l.setNewtab(true);
        }

        if (folder_l.isNewtab()) {
            holder.txt_newtab.setVisibility(View.GONE);
        } else {
            holder.txt_newtab.setVisibility(View.VISIBLE);
        }


        holder.folder_name.setText(new File(folder_l.getFolder_name()).getName());
        holder.items.setText(String.valueOf(folder_l.getItems()) + " items");

        holder.folder_item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.tabLayout.setVisibility(View.GONE);
                folder_video_list fragment = new folder_video_list();

                FragmentManager fragmentManager = home.activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.MainContainer, fragment);
                fragmentTransaction.commit();

                Bundle arguments = new Bundle();
                arguments.putString("FolderPath", folder_l.getFolder_name());
                fragment.setArguments(arguments);


            }
        });

    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        folderList.clear();
        if (charText.length() == 0) {
            folderList.addAll(arraylist);
        } else {
            for (int i = 0; i < arraylist.size(); i++) {
                if (arraylist.get(i).getFolder_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    folderList.add(arraylist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

}
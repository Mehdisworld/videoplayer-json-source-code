package com.mezdev.player.video_player.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdev.player.video_player.Adapter.video_adepter;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.Utils.CommonUtilities;
import com.mezdev.player.video_player.model.video_l;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class folder_video_list extends Fragment {
    private RecyclerView recyclerView;
    public static video_adepter adapter;
    private ArrayList<video_l> video_list;
    private ArrayList<String> video_list_t;
    private String FolderPath;


    private Cursor videocursor;
    private int video_column_index;
    ListView videolist;
    int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_folder_video_list, container, false);

        FolderPath = getArguments().getString("FolderPath");

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        video_list = new ArrayList<>();
        video_list_t = new ArrayList<>();
        video_list_t = CommonUtilities.getFolderMedia(getActivity(), FolderPath);
        System.gc();
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.RESOLUTION};
        videocursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);
        count = videocursor.getCount();


        for (int i = 0; i < count; i++) {
            video_column_index = videocursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            videocursor.moveToPosition(i);
            String path = videocursor.getString(video_column_index);
            File f = new File(path);
            File f1 = new File(f.getParent());

            if (f1.getPath().equals(FolderPath)) {

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                videocursor.moveToPosition(i);
                String name = videocursor.getString(video_column_index);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                videocursor.moveToPosition(i);
                String duration = videocursor.getString(video_column_index);

                video_column_index = videocursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION);
                videocursor.moveToPosition(i);
                String resolution = videocursor.getString(video_column_index);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                videocursor.moveToPosition(i);
                int size = videocursor.getInt(video_column_index);

                video_column_index = videocursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED);
                videocursor.moveToPosition(i);
                String date_fetch = videocursor.getString(video_column_index);


                File file = new File(path);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
                Date lastModDate1 = new Date(file.lastModified());
                String date = format.format(lastModDate1);

                video_list.add(new video_l(path, name, duration, resolution, size, date));

            }
        }

        adapter = new video_adepter(getActivity(), video_list, false);
        recyclerView_code();
        return v;
    }

    public void recyclerView_code() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


}

package com.mezdev.player.video_player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdev.player.video_player.Adapter.folder_adepter;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.Utils.CommonUtilities;
import com.mezdev.player.video_player.model.folder_l;

import java.util.ArrayList;


public class f_folder extends Fragment {
    private RecyclerView recyclerView;
    public static folder_adepter adapter;
    private ArrayList<folder_l> folder_list;
    private ArrayList<String> folder_list_t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_folder, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        folder_list = new ArrayList<>();
        folder_list_t = new ArrayList<>();
        folder_list_t = CommonUtilities.getAllMedia(getActivity());

        adapter = new folder_adepter(getActivity(), getActivity(), CommonUtilities.getmediaperent(folder_list_t, folder_list)/*folder_list*/);
        recyclerView_code();

        return v;
    }

    public void recyclerView_code() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
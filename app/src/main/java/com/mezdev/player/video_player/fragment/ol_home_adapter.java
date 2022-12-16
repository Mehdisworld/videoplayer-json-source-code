package com.mezdev.player.video_player.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mezdev.player.video_player.AdsManager.AdsController;
import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.activity.online_player;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class ol_home_adapter extends XRecyclerView.Adapter<ol_home_adapter.MyViewHolder> {
    private Activity mContext;
    private ArrayList<home_category_list> category_list;

    public class MyViewHolder extends XRecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView video_img;
        public LinearLayout lil_category_title;


        public MyViewHolder(View view) {
            super(view);
            video_img = (ImageView) view.findViewById(R.id.video_img);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            lil_category_title = (LinearLayout) view.findViewById(R.id.lil_category_title);
        }
    }

    public ol_home_adapter(final Activity mContext, ArrayList<home_category_list> category_list) {

        this.mContext = mContext;
        this.category_list = category_list;

        AdsController.LoadAdsInterstitial(mContext);


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ol_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final home_category_list home_category_list = category_list.get(position);
        new LongOperation(holder.tv_title, holder.video_img, home_category_list.getVideoId()).execute(home_category_list.getVideoId());
        RequestOptions options = new RequestOptions()
                .centerCrop();

        Glide.with(mContext)
                .load("http://img.youtube.com/vi/" + home_category_list.getVideoId() + "/0.jpg")
                .apply(options)
                .into(holder.video_img);

        holder.lil_category_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, online_player.class);
                intent.putExtra("url_id", home_category_list.getVideoId());

                AdsController.adCounter++;
                AdsController.ShowAdsInterstitial(mContext, intent, 0);
            }
        });


    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        ImageView video_img;
        TextView tv_title;
        String str_id;
        final String[] title = {null};

        public LongOperation(TextView tv_title, ImageView video_img, String str_id) {
            this.video_img = video_img;
            this.tv_title = tv_title;
            this.str_id = str_id;
        }

        @Override
        protected String doInBackground(String... params) {
            String youtubeVideoId = params[0];
            try {
                if (youtubeVideoId != null) {
                    InputStream is = new URL("http://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=" + youtubeVideoId + "&format=json").openStream();
                    Log.e("is", String.valueOf(is));
                    title[0] = new JSONObject(IOUtils.toString(is)).getString("title");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return title[0];
        }

        @Override
        protected void onPostExecute(String result) {
            tv_title.setText(title[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
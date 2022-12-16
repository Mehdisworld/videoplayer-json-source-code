package com.mezdev.player.video_player.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import com.mezdev.player.video_player.Utils.AppPref;
import com.mezdev.player.video_player.activity.play;
import com.mezdev.player.video_player.flloting_view.minivideomodel;


public class ServiceFloating extends Service {


    private WindowManager windowManager;
    private VideoView chatHead;
    boolean mHasDoubleClicked = false;
    long lastPressTime;
    AppPref objpref;


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objpref = new AppPref(getApplication());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new VideoView(this);
        Uri uri = Uri.parse(objpref.getVideo_path());

        chatHead.setVideoURI(uri);
        if (objpref.getVideo_position() > 0) {
            chatHead.seekTo(objpref.getVideo_position());
        }
        chatHead.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.selectTrack(objpref.getLanguage());
                chatHead.start();
            }
        });

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;


        windowManager.addView(chatHead, params);




        try {
            chatHead.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsF = params;
                int initialX = paramsF.x;
                int initialY = paramsF.y;
                float initialTouchX;
                float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:


                            long pressTime = System.currentTimeMillis();



                            if (pressTime - lastPressTime <= 300) {
                                ServiceFloating.this.stopSelf();

                                minivideomodel.mini_video_path = objpref.getVideo_path();
                                minivideomodel.mini_video_positipn = chatHead.getCurrentPosition();

                                Intent intent = new Intent(getApplicationContext(), play.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                getApplicationContext().startActivity(intent);

                                mHasDoubleClicked = true;
                            } else {
                                mHasDoubleClicked = false;
                                lastPressTime = pressTime;
                                initialX = paramsF.x;
                                initialY = paramsF.y;
                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                            }

                            break;
                        case MotionEvent.ACTION_UP:



                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, paramsF);
                            break;
                    }
                    return true;
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }












    }


    /*
        private void initiatePopupWindow(View anchor) {
            try {
                Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                ListPopupWindow popup = new ListPopupWindow(this);
                popup.setAnchorView(anchor);
                popup.setWidth((int) (display.getWidth()/(1.5)));
                //ArrayAdapter<String> arrayAdapter =
                //new ArrayAdapter<String>(this,R.layout.list_item, myArray);
                popup.setAdapter(new CustomAdapter(getApplicationContext(), R.layout.row, listCity));
                popup.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id3) {
                        //Log.w("tag", "package : "+apps.get(position).pname.toString());
                        Intent i;
                        PackageManager manager = getPackageManager();
                        try {
                            i = manager.getLaunchIntentForPackage(apps.get(position).pname.toString());
                            if (i == null)
                                throw new PackageManager.NameNotFoundException();
                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            startActivity(i);
                        } catch (PackageManager.NameNotFoundException e) {

                        }
                    }
                });
                popup.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void createNotification(){
            Intent notificationIntent = new Intent(getApplicationContext(), ServiceFloating.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.gift).setTicker("Click to start launcher").setWhen(System.currentTimeMillis())
                    .setContentTitle("Start launcher")
                    .setContentText("Click to start launcher");

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_NOTIFICATION, builder.build());
        }

    */
    @Override
    public void onDestroy() {
        minivideomodel.mini_video_path = objpref.getVideo_path();
        minivideomodel.mini_video_positipn = chatHead.getCurrentPosition();
        Log.e("flate_mini", minivideomodel.mini_video_path);
        Log.e("flate_mini", String.valueOf(minivideomodel.mini_video_positipn));

        if (chatHead != null) windowManager.removeView(chatHead);

        Intent intent = new Intent();
        intent.setAction("com.journaldev.broadcastreceiver.SOME_ACTION");
        sendBroadcast(intent);

        super.onDestroy();


    }

}
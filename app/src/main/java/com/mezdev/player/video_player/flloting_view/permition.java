package com.mezdev.player.video_player.flloting_view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.mezdev.player.video_player.R;
import com.mezdev.player.video_player.Utils.DeanandDanCaten;
import com.mezdev.player.video_player.activity.splash_scr;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

import static com.mezdev.player.video_player.activity.play.OVERLAY_PERMISSION_REQ_CODE;

public class permition extends MaterialIntroActivity {
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    DeanandDanCaten objPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
        objPref = new DeanandDanCaten(this);

        if (android.os.Build.VERSION.SDK_INT > 22) {
            permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

            if (ActivityCompat.checkSelfPermission(permition.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(permition.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {

                checkPermission_mini();

                getBackButtonTranslationWrapper()
                        .setEnterTranslation(new IViewTranslation() {
                            @Override
                            public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                                view.setAlpha(percentage);
                            }
                        });

                addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.textColorPrimary)

                        .buttonsColor(R.color.colorPrimary)
                        .image(R.drawable.on_boarding)
                        .title("TONSE OF CONTENT")
                        .description("Watch tonsof HD Content of all your device,everyday,everytime,and antwhere while no cost.")
                        .build());

                addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.textColorPrimary)
                        .buttonsColor(R.color.colorPrimary)
                        .image(R.drawable.on_boarding2)
                        .title("SHARE COLLECTION")
                        .buttonsColor(R.color.colorPrimary)
                        .description("Recommend your anime collection to your friend and makeyour fanbase bigger.")
                        .build());



                addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.textColorPrimary)
                        .buttonsColor(R.color.colorPrimary)
                        .possiblePermissions(new String[]{})
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.ACCESS_COARSE_LOCATION})
                        .image(R.drawable.on_boarding3)
                        .title("STAY UPDATE")
                        .description("Get the most updated episcode of your favourite anime everyday.Pick your now\nby continuing you agree our privacy and policy.")
                        .build());


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();

            } else {

                Log.e("already", "already permition call");
                proceedAfterPermission();
            }
        } else {
            Log.e("Lower Version", "fully granted permition ");
            this.finish();
            startActivity(new Intent(permition.this, splash_scr.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {

            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(permition.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(permition.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(permition.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(permition.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(permition.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(permition.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {

                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {

        Log.e("hi", "after permition");
        this.finish();
        startActivity(new Intent(permition.this, splash_scr.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(permition.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {

                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent(permition.this, splash_scr.class);
        startActivity(intent);
        finish();
    }

    public void checkPermission_mini() {
        if (Build.VERSION.SDK_INT >= 28) {
            if (!Settings.canDrawOverlays(permition.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(permition.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getApplicationContext().getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
            }
        }
    }
}

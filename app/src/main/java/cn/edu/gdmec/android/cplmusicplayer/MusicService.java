package cn.edu.gdmec.android.cplmusicplayer;

/**
 * Created by Administrator on 2018/6/10.
 */

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.view.animation.LinearInterpolator;


public class MusicService extends Service {
    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public static int isReturnTo = 0;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static ObjectAnimator animator;
    public MusicService() {
        initMediaPlayer();

    }

    public void initMediaPlayer() {
        try {
            String file_path = "/storage/sdcard/07160604.mp3";

            mediaPlayer.setDataSource(file_path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);  // 设置循环播放
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void AnimatorAction() {
        if (mediaPlayer.isPlaying()) {
            animator.setDuration(5000);
            animator.setInterpolator(new LinearInterpolator()); // 均速旋转
            animator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
            //animator.setRepeatMode(ValueAnimator.INFINITE);
            animator.start();
        }
    }
    private int flag = 0;
    public static String which = "";
    @SuppressLint("WrongConstant")
    public void play() {
        flag++;
        if (flag >= 1000) flag = 2;

        which = "pause";

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            animator.pause();
        } else {
            mediaPlayer.start();

            if ((flag == 1) || (isReturnTo == 1)) {
                animator.setDuration(5000);
                animator.setInterpolator(new LinearInterpolator()); // 均速旋转
                animator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
                animator.setRepeatMode(ValueAnimator.INFINITE);
                animator.start();
            } else {
                animator.resume();
            }
        }
    }

    public void stop() {
        which = "stop";
        animator.pause();
        if(mediaPlayer != null) {
            mediaPlayer.pause();

            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
    /**
     * onBind 是 Service 的虚方法，因此不得不实现它。
     * 返回 null，表示客服端不能建立到此服务的连接。
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
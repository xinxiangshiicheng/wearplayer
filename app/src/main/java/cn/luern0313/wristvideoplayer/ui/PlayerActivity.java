package cn.luern0313.wristvideoplayer.ui;

import static android.media.AudioManager.STREAM_MUSIC;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import cn.luern0313.wristvideoplayer.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private OkHttpClient okHttpClient;
    private Timer progresstimer;
    private Timer timer2;
    private Timer sound;
    private String url;
    private String danmaku;
    private String title;
    private int historytime;
    private RelativeLayout relativeLayout;
    private MediaPlayer mediaPlayer;
    private TextureView textureView;
    private String basedirectory;//手机储存路径
    private String videoname;//临时储存的视频路径
    private String danmakuname;//临时储存的弹幕路径
    private File delete0;//视频文件
    private File delete1;
    private float TotalFileSize;//视频文件总文件大小
    private float CompleteFileSize;//已经下完的文件大小
    private float DownProgress;//下载进度
    private String ShowProgress;
    private long Complete;
    private TextView Showtitle;
    private Button ControlButton;
    private SeekBar progressBar;
    private TextView downed;
    private float Screenwidth;
    private float Screenheight;
    private int showheight;
    private int showwidth;
    private FrameLayout downloadprogress;
    private LinearLayout linearLayouttop;
    private LinearLayout linearLayoutbottom;
    private boolean ischanging = false;
    private TextView timenow;
    private TextView alltime;
    private int videoall;
    private int videonow;
    private TextView showsound;
    private AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        timenow = findViewById(R.id.timenow);
        alltime = findViewById(R.id.alltime);
        ControlButton = findViewById(R.id.control);//找到播放控制按钮
        progressBar = findViewById(R.id.videoprogress);//找到视频进度条
        downed = findViewById(R.id.downedprogess);
        Showtitle = findViewById(R.id.showtitle);//找到标题控件
        showsound = findViewById(R.id.showsound);
        textureView = findViewById(R.id.textureview);
        ControlButton = findViewById(R.id.control);
        downloadprogress = (FrameLayout)findViewById(R.id.downloadprogress);
        relativeLayout = (RelativeLayout)findViewById(R.id.realtive);
        WindowManager windowManager = this.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Screenwidth = displayMetrics.widthPixels;//获取屏宽
        Screenheight = displayMetrics.heightPixels;//获取屏高
        File basedir = Environment.getExternalStorageDirectory();
        basedirectory = basedir.toString();
        okHttpClient = new OkHttpClient();
        File creat = new File(basedirectory+"/wearplayerdata");//创建视频临时存储文件夹
        creat.mkdir();
        videoname = basedirectory+"/wearplayerdata/TemporaryVideo.mp4";
        danmakuname = basedirectory+"/wearplayerdata/TemporaryDanmu.xml";
        delete0 = new File(videoname);
        delete1 = new File(danmakuname);
        linearLayouttop = (LinearLayout)findViewById(R.id.Topcontrol);
        linearLayoutbottom = (LinearLayout)findViewById(R.id.Bottomcontrol);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");//功能暂不清楚
        url = intent.getStringExtra("url");//视频链接
        String url_backup = intent.getStringExtra("url_backup");//功能暂不清楚
        danmaku = intent.getStringExtra("danmaku");//弹幕链接
        title = intent.getStringExtra("title");//视频标题
        String identity_name = intent.getStringExtra("identity_name");//暂时不知道干嘛的
        String time = intent.getStringExtra("time");//估计是历史记录的时间
        if (time != null) historytime = Integer.parseInt(time);
        String headers = intent.getStringExtra("headers");//暂时不知道干嘛的
        Showtitle.setText(title);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ischanging = true;
                if (timer2 !=null)timer2.cancel();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null){
                    mediaPlayer.seekTo(progressBar.getProgress());
                    ischanging = false;
                }
                timer2 =new Timer();
                TimerTask timerTask =new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linearLayouttop.setVisibility(View.GONE);
                                linearLayoutbottom.setVisibility(View.GONE);
                            }
                        });
                    }
                };
                timer2.schedule(timerTask,3000);
            }
        });
        timer2 =new Timer();
        TimerTask timerTask =new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayouttop.setVisibility(View.GONE);
                        linearLayoutbottom.setVisibility(View.GONE);
                    }
                });
            }
        };
        timer2.schedule(timerTask,4000);
        if (url != null || url.length()>0){
//            downdanmu();
            //弹幕下载暂时关掉
            downvideo();
        }else {
            //播放错误
        }
    }

    public void showcon(View view){
        if ((linearLayouttop.getVisibility())==View.GONE){
            linearLayouttop.setVisibility(View.VISIBLE);
            linearLayoutbottom.setVisibility(View.VISIBLE);
            timer2 = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayouttop.setVisibility(View.GONE);
                            linearLayoutbottom.setVisibility(View.GONE);
                        }
                    });

                }
            };
            timer2.schedule(timerTask,4000);
        }else {
            if (timer2!=null)timer2.cancel();
            linearLayouttop.setVisibility(View.GONE);
            linearLayoutbottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progresstimer.cancel();
        if (mediaPlayer != null) mediaPlayer.release();
        delete0 = new File(videoname);
        delete1 = new File(danmakuname);
        delete0.delete();
        delete1.delete();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            String con = ControlButton.getText().toString();
            if (con=="| |"){
                mediaPlayer.start();
            }
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mediaPlayer.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
                }
            };
            timer.schedule(timerTask,5000);
        }
    }

    private void downvideo() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(url)
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                        .addHeader("Referer","https://www.bilibili.com/")
                        .addHeader("Origin","https://www.bilibili.com/")
                        .build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    InputStream inputStream = response.body().byteStream();
                    final long lengh = response.body().contentLength();
                    TotalFileSize = (float)lengh;
                    //获取总文件大小
                    FileOutputStream fileOutputStream = new FileOutputStream(videoname);
                    int len,losing = 0;
                    byte[] bytes = new byte[8192];
                    while ((len = inputStream.read(bytes))!=-1){
                        fileOutputStream.write(bytes,0,len);
                        Complete = delete0.length();
                        CompleteFileSize = (float)Complete;
                        DownProgress = (CompleteFileSize/TotalFileSize)*100;
                        ShowProgress = DownProgress+"%";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downed.setText(ShowProgress);
                            }
                        });
                    }
                    fileOutputStream.flush();
                    inputStream.close();
                    fileOutputStream.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadprogress.removeAllViews();
                        }
                    });
                    playing();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void playing() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);//循环播放
        mediaPlayer.setSurface(new Surface(textureView.getSurfaceTexture()));
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(videoname);
        }catch (IOException e){
            e.printStackTrace();
        }
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                changeVideoSize(i,i1);
            }
        });
        mediaPlayer.prepareAsync();
    }

    private void changeVideoSize(int width, int height) {
            float multiplewidth = Screenwidth/width;
            float multipleheight = Screenheight/height;
            float endhi1 = height * multipleheight;
            float endwi1 = width * multipleheight;
            float endhi2 = height * multiplewidth;
            float endwi2 = width * multipleheight;
            if (endhi1<=Screenheight && endwi1 <= Screenwidth){
                showheight = (int)(endhi1+0.5);
                showwidth = (int)(endwi1+0.5);
            } else {
                showheight = (int)(endhi2+0.5);
                showwidth = (int)(endwi2+0.5);
            }
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(showwidth,showheight));
    }

    private void downdanmu() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(danmaku).build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    InputStream inputStream = response.body().byteStream();
                    final long lengh = response.body().contentLength();
                    //获取文件大小
                    FileOutputStream fileOutputStream = new FileOutputStream(danmakuname);
                    int len,losing = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = inputStream.read(bytes))!=-1){
                        fileOutputStream.write(bytes,0,len);
                    }
                    fileOutputStream.flush();
                    inputStream.close();
                    fileOutputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //弹幕没有refer
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        videoall = mediaPlayer.getDuration();
        progressBar.setMax(videoall);
        int minutes = videoall/60000;
        int seconds = videoall%60000/1000;
        ControlButton.setText("| |");
        if (historytime >0) mediaPlayer.seekTo(historytime);
        alltime.setText(minutes+":"+seconds);
        progresschange();
    }

    private void progresschange() {
        progresstimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (ischanging == false ){
                       videonow = mediaPlayer.getCurrentPosition();
                       progressBar.setProgress(videonow);
                       int minute = videonow/60000;
                       int second = videonow%60000/1000;
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               timenow.setText(minute+":"+second);
                           }
                       });
                }
            }
        };
        progresstimer.schedule(task,0,400);
    }

    public void controlvideo(View view){
        if ((ControlButton.getText().toString())=="| |"){
            mediaPlayer.pause();
            ControlButton.setText("‣");

        }else {
            if (mediaPlayer != null){
                mediaPlayer.start();
                ControlButton.setText("| |");
            }
        }
        timer2 =new Timer();
        TimerTask timerTask1 =new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayouttop.setVisibility(View.GONE);
                        linearLayoutbottom.setVisibility(View.GONE);
                    }
                });
            }
        };
        timer2.schedule(timerTask1,3000);
    }
    public void addsound(View view){
        timer2.cancel();
        if (sound != null)sound.cancel();
        int soundnow = audioManager.getStreamVolume(STREAM_MUSIC);
        int maxsound = audioManager.getStreamMaxVolume(STREAM_MUSIC);
        int added = maxsound;
        if (soundnow != maxsound){
            added = soundnow+1;
        }
        audioManager.setStreamVolume(STREAM_MUSIC,added,0);
        float show = (float)(added)/(float)maxsound*100;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showsound.setVisibility(View.VISIBLE);
                showsound.setText("音量："+(int)show+"%");
            }
        });
        sound = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showsound.setVisibility(View.GONE);
                    }
                });
            }
        };
        sound.schedule(timerTask,3000);
        }
    public void cutsound(View view){
        timer2.cancel();
        if (sound!=null)sound.cancel();
        int soundnow = audioManager.getStreamVolume(STREAM_MUSIC);
        int maxsound = audioManager.getStreamMaxVolume(STREAM_MUSIC);
        int added = 0;
        if (soundnow != 0){
            added = soundnow-1;
        }
        audioManager.setStreamVolume(STREAM_MUSIC,added,0);
        float show = (float)added/(float)maxsound*100;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showsound.setVisibility(View.VISIBLE);
                showsound.setText("音量："+(int)show+"%");
            }
        });
        sound = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showsound.setVisibility(View.GONE);
                    }
                });
            }
        };
        sound.schedule(timerTask,3000);
    }
}
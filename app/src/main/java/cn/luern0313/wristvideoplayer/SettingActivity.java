package cn.luern0313.wristvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.luern0313.wristvideoplayer.about.OpenSourceActivity;

public class SettingActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        textView = findViewById(R.id.text);
    }
    public void back(View view){
        finish();
    }
    public void updata(View view){
        if (textView.getVisibility()==View.GONE || textView.length()>0){
            textView.setVisibility(View.VISIBLE);
            String data = "更新日志：\n" +
                    "Beat0.1 小电视播放器推出，可以正常播放腕上哔哩的视频\n\n" +
                    "Beat0.2 修复了SurfaceView在退出重进后被销毁导致视频有声无画面bug，修复了进度条和标题重复点击后鬼畜的bug，新增音量控制功能\n\n" +
                    "Beat0.3 修复了一些bug，新增音量控制时实时显示当前音量功能，增大了音量控制按钮大小，修改了音量控制按钮图标\n\n" +
                    "v1.0 修复了一些bug，完成了主页及主页一部分功能的制作\n" +
                    "*目前弹幕播放.本地视频播放.小游戏功能未完成\n....初三累成狗 :(";
            textView.setText(data);
        }else {
            textView.setVisibility(View.GONE);
        }
    }
    public void authowords(View view){
        if (textView.getVisibility()==View.GONE || textView.length()>0){
            textView.setVisibility(View.VISIBLE);
            String words = "因为不想付费，于是研究腕上哔哩开源的代码并开发了这个小电视播放器，其实我感觉luern的代码写的挺好的，估计人家大学是计算机专业的，代码写的很整齐，不像我的代码一样乱成一片:(\n\n" +
                    "其实说实话我对Android开发几乎是没什么基础的，java会的也不多，了解比较多的还是H5开发，开发小电视播放器的时候我基本是边学边写的，踩了特备多的坑(这也就是为啥开发了这么久的原因)";
            textView.setText(words);
        }else {
            textView.setVisibility(View.GONE);
        }
    }
    public void source(View view){
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, OpenSourceActivity.class);
        startActivity(intent);
    }
}
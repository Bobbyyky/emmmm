package com.example.shinelon.notebook;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.shinelon.notebook.R;
import com.example.shinelon.notebook.adapter.FragmentAdapter;
import com.example.shinelon.notebook.fragment.ListDiaryFragment;
import com.example.shinelon.notebook.fragment.PersonFragment;
import com.example.shinelon.notebook.fragment.WriteDiaryFragment;
import com.example.shinelon.notebook.util.Configure;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private long exitTime = 0;
    private int fromWhich = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViewForFragment();
    }

    private void initData() {
        fromWhich = getIntent().getIntExtra("from",0);
    }

    private void initViewForFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WriteDiaryFragment.newInstance(this));
        fragments.add(ListDiaryFragment.newInstance(this));
        fragments.add(PersonFragment.newInstance(this));

        List<String> tabs = new ArrayList<>();
        tabs.add("写日记");
        tabs.add("日记");
        tabs.add("个人");

        FragmentAdapter fragmentAdapter = new  FragmentAdapter(fragmentManager,fragments,tabs);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        if (fromWhich == Configure.FROM_SHOW_DIARY_ACTIVITY){
            tabLayout.getTabAt(1).select();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis() - exitTime > 2000){
                exitTime = System.currentTimeMillis();
                Toast.makeText(this,"再按退出",Toast.LENGTH_SHORT).show();
            }else {
                finish();
                System.exit(0);
            }
        }
        return true;
    }
}

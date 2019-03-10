package com.example.shinelon.notebook.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.shinelon.notebook.MainActivity;
import com.example.shinelon.notebook.R;
import com.example.shinelon.notebook.activity.ShowDiaryActivity;
import com.example.shinelon.notebook.database.DiaryDao;
import com.example.shinelon.notebook.database.OpenHelper;
import com.example.shinelon.notebook.entity.Diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shinelon on 2019/3/10.
 */

public class ListDiaryFragment extends Fragment implements AdapterView.OnItemLongClickListener{

    private static AppCompatActivity activity;
    private List<Map<String,String>> dataList;
    private SimpleAdapter simpleAdapter;
    private OpenHelper openHelper;
    private List<Diary>diaryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.list_diary,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ListView listView = (ListView)view.findViewById(R.id.diary_item);
        simpleAdapter = new SimpleAdapter(
                activity,
                dataList,
                R.layout.diary_item,
                new String[]{"title","date"},
                new int[]{R.id.item_title,R.id.item_date});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity,ShowDiaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("diary",diaryList.get(position));
                intent.putExtras(bundle);
                activity.startActivity(intent);
                activity.finish();
            }
        });
        listView.setOnItemLongClickListener(this);
    }

    private void initData() {
        openHelper = new OpenHelper(activity);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
        diaryList = diaryDao.queryAll();
        sqLiteDatabase.close();

        dataList = new ArrayList<>();
        for (Diary diary:diaryList){
            Map<String,String> map = new HashMap<>();
            map.put("title",diary.getTitle());
            map.put("date",diary.getDate());
            dataList.add(map);
        }
    }

    public static ListDiaryFragment newInstance(MainActivity activity) {
        ListDiaryFragment.activity = activity;

        ListDiaryFragment fragment = new ListDiaryFragment();
        return fragment;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int location = position;
        new AlertDialog.Builder(activity)
                .setTitle("警告")
                .setMessage("确认删除？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
                        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
                        diaryDao.delete(diaryList.get(location));
                        diaryList.remove(location);
                        sqLiteDatabase.close();
                        dataList.remove(location);
                        simpleAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("否",null)
                .show();
        return true;
    }
}

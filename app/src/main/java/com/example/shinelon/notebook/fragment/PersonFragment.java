package com.example.shinelon.notebook.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinelon.notebook.MainActivity;
import com.example.shinelon.notebook.R;

/**
 * Created by Shinelon on 2019/3/10.
 */

public class PersonFragment extends Fragment implements View.OnClickListener,View.OnLongClickListener{

    private static AppCompatActivity activity;
    private SharedPreferences sharedPreferences;
    private TextView personDate;
    private TextView personJob;
    private TextView personLove;
    private TextView personConstruction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        sharedPreferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        TextView nameText = (TextView)view.findViewById(R.id.person_name);
        nameText.setText(sharedPreferences.getString("name","") + "简介");
        personDate = (TextView)view.findViewById(R.id.person_date);
        personJob = (TextView)view.findViewById(R.id.person_job);
        personLove = (TextView)view.findViewById(R.id.person_love);
        personConstruction = (TextView)view.findViewById(R.id.person_construction);

        personDate.setTag("出生日期");
        personJob.setTag("职业");
        personLove.setTag("兴趣爱好");
        personConstruction.setTag("个人说明");

        personDate.setText(sharedPreferences.getString("date",""));
        personJob.setText(sharedPreferences.getString("job",""));
        personLove.setText(sharedPreferences.getString("love",""));
        personConstruction.setText(sharedPreferences.getString("construction",""));

        personDate.setOnClickListener(this);
        personJob.setOnClickListener(this);
        personLove.setOnClickListener(this);
        personConstruction.setOnClickListener(this);

        personDate.setOnLongClickListener(this);
        personJob.setOnLongClickListener(this);
        personLove.setOnLongClickListener(this);
        personConstruction.setOnLongClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"长按可修改内容",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.person_date:
                dialogForChangeContent("date",personDate);
                break;
            case R.id.person_job:
                dialogForChangeContent("job",personJob);
                break;
            case R.id.person_love:
                dialogForChangeContent("love",personLove);
                break;
            case R.id.person_construction:
                dialogForChangeContent("construction",personConstruction);
                break;
        }
        return true;
    }

    private void dialogForChangeContent(final String key, final TextView text) {
        final EditText changeText = new EditText(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("修改" + text.getTag());
        builder.setView(changeText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = changeText.getText().toString();

                if (value != null && "".equals(value)){
                    text.setText(value);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(key,value);
                    editor.commit();
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    public static PersonFragment newInstance(MainActivity activity) {
        PersonFragment.activity = activity;
        PersonFragment fragment = new PersonFragment();
        return fragment;
    }
}

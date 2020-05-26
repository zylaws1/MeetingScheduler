package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.database.NoteDBManager;

/**
 * Note fragment, list the notes in database
 *
 * @author Xin Shen, Shaocong Lang
 */
public class NoteListFragment extends Fragment {
    // regex ((?!(\*|//)).)+[\u4e00-\u9fa5]
    private Cursor listItemCursor = null;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes_listview, null);

        // 设置添加笔记按钮事件，切换activity
        view.findViewById(R.id.addNote).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                        transaction.replace(R.id.main_linear, MainActivity.instance.getNoteEditFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

        // 查询所有笔记，并将笔记展示出来
        listItemCursor = NoteDBManager.queryAll();
        Log.i("shenxin", "listItemCursor: " + listItemCursor.getCount());
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                R.layout.note_item, listItemCursor, new String[]{"_id",
                "title", "createTime"}, new int[]{R.id.noteId,
                R.id.noteTitle, R.id.noteCreateTime},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lv = (ListView) view.findViewById(R.id.listNote);
        lv.setAdapter(adapter);
        initListNoteListener();
        return view;
    }

    /**
     * 初始化笔记列表的长按和点击事件
     */
    private void initListNoteListener() {
        // 长按删除
        ((ListView) view.findViewById(R.id.listNote))
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, final long id) {
                        new AlertDialog.Builder(MainActivity.instance)
                                .setTitle("Tip:")
                                .setMessage("Delete the note?")
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                NoteDBManager.deleteNoteById((int) id);
                                                //删除后刷新列表
                                                MainActivity.instance.onResume();
                                                Toast.makeText(
                                                        MainActivity.instance,
                                                        "note deleted!",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }).setNegativeButton("Cancel", null).show();
                        return true;
                    }
                });

        //点击进行修改操作
        ((ListView) view.findViewById(R.id.listNote))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent in = new Intent();
                        in.setClassName(view.getContext(), "cn.lger.notebook.NoteEditActivity");
                        // 将id数据放置到Intent，切换视图后可以将数据传递过去
                        in.putExtra("id", id);
                        startActivity(in);
                    }
                });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    /**
     * 当从另一个视图进入该视图会调用该方法
     */
    @Override
    public void onResume() {
        super.onResume();
        // 要求刷新主页列表笔记
        if (listItemCursor != null) {
            listItemCursor.requery();
        }
    }

}

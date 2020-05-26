package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.database.NoteDBManager;

/**
 * Note fragment, list the notes in database
 *
 * @author Xin Shen, Shaocong Lang
 */
public class NoteEditFragment extends Fragment {
    private EditText titleEditText = null;
    private EditText contentEditText = null;
    private String noteId = null;
    View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.fragment_edit_note, null);
            titleEditText = (EditText) root_view.findViewById(R.id.title);
            contentEditText = (EditText) root_view.findViewById(R.id.content);
            initNoteEditValue();
            root_view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final String title = titleEditText.getText().toString();
                    final String content = contentEditText.getText().toString();

                    //判断标题和内容是否为空，不为空才能保存
                    if ("".equals(title) || "".equals(content)) {
                        Toast.makeText(MainActivity.mContext, "Title or content cannot be empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    //提示保存
                    new AlertDialog.Builder(getContext())
                            .setTitle("Tips")
                            .setMessage("Do you want to save note?")
                            .setPositiveButton("Yes!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            ContentValues values = new ContentValues();
                                            values.put("title", title);
                                            values.put("content", content);

                                            //如果noteId不为空那么就是更新操作，为空就是添加操作
                                            if (null == noteId || "".equals(noteId))
                                                NoteDBManager.addNote(values);
                                            else
                                                NoteDBManager.updateNoteById(Integer.valueOf(noteId), values);
                                            //结束当前activity
                                            getActivity().onBackPressed();
                                            Toast.makeText(MainActivity.mContext, "Save successfully! ",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    })
                            .setNegativeButton("Cancel", null).show();

                }
            });
        }
        return root_view;
    }

    /**
     * 初始化编辑页面的值（如果进入该页面时存在一个id的话），比如标题，内容。
     */
    private void initNoteEditValue() {
        // 从Intent中获取id的值
        long id = MainActivity.instance.getIntent().getLongExtra("id", -1L);
        // 如果有传入id那么id！=-1
        if (id != -1L) {
            // 使用noteId保存id
            noteId = String.valueOf(id);
            // 查询该id的笔记
            Cursor cursor = NoteDBManager.queryNoteById((int) id);
            if (cursor.moveToFirst()) {
                // 将内容提取出来
                titleEditText.setText(cursor.getString(1));
                contentEditText.setText(cursor.getString(2));
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}

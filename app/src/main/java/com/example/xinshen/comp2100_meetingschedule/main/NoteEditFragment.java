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

                    // check whether the title and content are empty
                    if ("".equals(title) || "".equals(content)) {
                        Toast.makeText(MainActivity.mContext, "Title or content cannot be empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Saving tips
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

                                            // If noteId is not empty, then it is an update operation, and empty is an add operation
                                            if (null == noteId || "".equals(noteId))
                                                NoteDBManager.addNote(values);
                                            else
                                                NoteDBManager.updateNoteById(Integer.valueOf(noteId), values);
                                            // finish fragment
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
     * Initialize the value of the edit page
     * (if there is an id when entering the page), such as the title and content.
     */
    private void initNoteEditValue() {
        // get id from intent
        long id = MainActivity.instance.getIntent().getLongExtra("id", -1L);
        if (id != -1L) {
            // use noteId as id
            noteId = String.valueOf(id);
            // find note by id
            Cursor cursor = NoteDBManager.queryNoteById((int) id);
            if (cursor.moveToFirst()) {
                // get content values
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

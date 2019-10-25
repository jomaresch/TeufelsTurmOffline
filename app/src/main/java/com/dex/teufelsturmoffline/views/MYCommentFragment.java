package com.dex.teufelsturmoffline.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.activities.CommentActivity;
import com.dex.teufelsturmoffline.database.DatabaseHelper;

public class MYCommentFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {


    String routeId;
    ToggleButton doneButton;
    EditText myComment;
    DatabaseHelper db;
    CommentActivity commentActivity;

    @Override
    public void setArguments(@Nullable Bundle args) {
        this.routeId = args.getString("ID","0");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commentActivity = (CommentActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_my_comment_view, container, false);
        doneButton = view.findViewById(R.id.toggle_button_done);
        doneButton.setChecked(commentActivity.getDone());
        doneButton.setOnClickListener(this);

        myComment = view.findViewById(R.id.edit_my_comment);
        myComment.setOnFocusChangeListener(this);

        db = new DatabaseHelper(view.getContext());
        String commentString = db.getMyComment(routeId).second;
        myComment.setText(commentString);
        return view;
    }

    @Override
    public void onClick(View view) {

        String dialogText;

        if(!doneButton.isChecked()) {
            dialogText = "Bist du dir sicher, dass du den Weg auf 'noch nicht geklettert ' setzen willst ?";

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        commentActivity.setDoneIcon(doneButton.isChecked());
                    } else {
                        doneButton.setChecked(!doneButton.isChecked());
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setMessage(dialogText).setPositiveButton("JA", dialogClickListener)
                    .setNegativeButton("NEIN", dialogClickListener).show();
        }
        else {
            commentActivity.setDoneIcon(doneButton.isChecked());
        }

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
           db.setMyComment(routeId,myComment.getText().toString());
        }
    }
}

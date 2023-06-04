package com.dex.teufelsturmoffline.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.adapter.CommentRecycleAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.Comment;

import java.util.List;

public class TTCommentFragment extends Fragment {

    List<Comment> commentList;
    DatabaseHelper db;

    CommentRecycleAdapter commentRecycleAdapter;
    RecyclerView recyclerView;

    String routeId;

    @Override
    public void setArguments(@Nullable Bundle args) {
        this.routeId = args.getString("ID","0");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tt_comment_view, container, false);
        db = new DatabaseHelper(view.getContext());

        this.commentList = db.getCommentByRoute(routeId);
        this.recyclerView = view.findViewById(R.id.recycler_list_comment);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL, false);


        commentRecycleAdapter = new CommentRecycleAdapter(
                view.getContext(),
                commentList,
                new CommentRecycleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Comment item) {}
                });

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(commentRecycleAdapter);

        return view;
    }
}

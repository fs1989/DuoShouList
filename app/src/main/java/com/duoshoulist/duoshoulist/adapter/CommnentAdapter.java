package com.duoshoulist.duoshoulist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.DetailActivity;
import com.duoshoulist.duoshoulist.activity.UserProfileActivity;
import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by Dan on 2016/2/1.
 */
public class CommnentAdapter extends RecyclerView.Adapter<CommnentAdapter.ViewHolder> {

    Context context;
    private List<Comment> commentsData;

    public CommnentAdapter(Context context, List<Comment> commentsData) {
        this.commentsData = commentsData;
        this.context = context;
    }

    @Override
    public CommnentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comments, viewGroup, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = new ViewHolder(holder.mView);

        Comment comment = commentsData.get(position);

        User user = new User();
        user = user.getUser(context, comment.getUserID());

        viewHolder.userName.setText(user.getUsername().toString());
        viewHolder.time.setText(comment.getCreatedAt().toString());
        viewHolder.text.setText(comment.getText().toString());

        String avatar = user.getUserAvatar();
        Glide.with(holder.userAvatar.getContext()).load(avatar).into(viewHolder.userAvatar);

        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userID", commentsData.get(position).getUserID());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return commentsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView userName;
        public final CircleImageView userAvatar;
        public final TextView time;
        public final TextView text;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            userName = (TextView) view.findViewById(R.id.comment_userName);
            time = (TextView) view.findViewById(R.id.comment_time);
            text = (TextView) view.findViewById(R.id.comment_text);
            userAvatar = (CircleImageView) view.findViewById(R.id.comment_avatar);
        }

    }


}

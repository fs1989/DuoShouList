package com.duoshoulist.duoshoulist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.ProfileActivity;
import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Dan on 2016/2/1.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    final String TAG = "CommentAdapter";
    Context context;
    private List<Comment> commentsData;
    private Handler handler = new Handler();




    public CommentAdapter(Context context, List<Comment> commentsData) {
        this.commentsData = commentsData;
        this.context = context;

    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comments, viewGroup, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = new ViewHolder(holder.mView);

        Comment comment = commentsData.get(position);
        viewHolder.text.setText(comment.getText());

        String nickName = comment.getUser().getNickName();
        String avatar = comment.getUser().getAvatar();

        if (nickName != null) {
            viewHolder.userName.setText(nickName);
        }
        if (avatar != null) {
            Glide.with(viewHolder.userAvatar.getContext()).load(avatar).into(viewHolder.userAvatar);
        }


        int min = 60000; // 分钟
        int hour = min * 60; // 小时
        int day = hour * 24; // 天
        int month = day * 30; // 月
        int year = month * 12; // 年

        Long currentTime = System.currentTimeMillis();
        String commentTime = comment.getCreatedAt();
        Long unixTime = (currentTime - BmobDate.getTimeStamp(commentTime));
        Long timeCondition = unixTime / 3600000;
//        Log.i(TAG, "unixTime " + unixTime.toString() + " timeCondition " + timeCondition.toString() +" " + comment.getText());
        if (timeCondition < 1) {
            int rate = min;
            Long time = unixTime / rate;
            int finalTime = Integer.parseInt(time.toString());
            viewHolder.time.setText(finalTime + "分钟前");
        } else if (timeCondition >= 1 && timeCondition < 60) {
            int rate = hour;
            Long time = unixTime / rate;
            int finalTime = Integer.parseInt(time.toString());
            viewHolder.time.setText(finalTime + "小时前");
        } else if (timeCondition >= 60 && timeCondition < 60 * 24 * 30) {
            int rate = day;
            Long time = unixTime / rate;
            int finalTime = Integer.parseInt(time.toString());
            viewHolder.time.setText(finalTime + "天前");
        } else if (timeCondition >= 60 * 24 * 30 && timeCondition < 60 * 24 * 30 * 12) {
            int rate = month;
            Long time = unixTime / rate;
            int finalTime = Integer.parseInt(time.toString());
            viewHolder.time.setText(finalTime + "月前");
        } else if (timeCondition >= 60 * 24 * 30 * 12) {
            int rate = year;
            Long time = unixTime / rate;
            int finalTime = Integer.parseInt(time.toString());
            viewHolder.time.setText(finalTime + "年前");
        } else {
            viewHolder.time.setText("N久以前");
        }

        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userID", commentsData.get(position).getUser());
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

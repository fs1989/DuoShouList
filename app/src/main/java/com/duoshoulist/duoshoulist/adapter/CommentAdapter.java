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
import com.duoshoulist.duoshoulist.bmob.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
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

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
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

        final Comment comment = commentsData.get(position);


        viewHolder.time.setText(comment.getCreatedAt());
        viewHolder.text.setText(comment.getText());

        String nickName = comment.getNickName();
        String avatar = comment.getAvatar();

        viewHolder.userName.setText(nickName);
        Glide.with(viewHolder.userAvatar.getContext()).load(avatar).into(viewHolder.userAvatar);

        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
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

    void loadData(final ViewHolder viewHolder, Comment comment) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", comment.getUserID());
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                // 加载昵称
                if (list.get(0).getNickName() != null) {
//                    viewHolder.userName.setText(list.get(0).getNickName());
                }

                // 加载头像
                if (list.get(0).getAvatar() != null) {
//                    Glide.with(viewHolder.userAvatar.getContext()).load(list.get(0).getAvatar()).into(viewHolder.userAvatar);
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.v(TAG, "查询用户失败：" + msg);

            }
        });
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

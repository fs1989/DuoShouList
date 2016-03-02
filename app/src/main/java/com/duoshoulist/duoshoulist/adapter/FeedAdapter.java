package com.duoshoulist.duoshoulist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.DetailActivity;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.duoshoulist.duoshoulist.bmob.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dan on 2016-01-15.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private String TAG = "FeedAdapter";

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<FeedItem> mdata;
    Context context;

    public FeedAdapter(Context context, List<FeedItem> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mdata = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_feed, viewGroup, false);
        itemLayout.setBackgroundResource(mBackground);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = new ViewHolder(holder.mView);

        holder.objectId = mdata.get(position).getObjectId();

        viewHolder.likes.setText(mdata.get(position).getLikes().toString());

        viewHolder.desc.setText(mdata.get(position).getDesc().toString());

        String image = mdata.get(position).getImage();
        String userId = mdata.get(position).getUserId();

        // Load image
        if (image != null) {
            Glide.with(holder.image.getContext()).load(image).crossFade().into(viewHolder.image);
        }

        // Load Avatar
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", userId);
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                User user = list.get(0);
                String name = user.getNickName();
                String avatar = user.getAvatar();
                if (avatar != null) {
                    viewHolder.name.setText(name);
                    Glide.with(holder.avatar.getContext()).load(avatar).into(viewHolder.avatar);
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, "获取user失败: " + msg);
            }
        });



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedItem", mdata.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView likes;
        public final TextView name;
        public final TextView desc;
        public final TextView time;
        public final CircleImageView avatar;
        public final ImageView image;
        public String objectId;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            likes = (TextView) view.findViewById(R.id.likes);
            name = (TextView) view.findViewById(R.id.name);
            desc = (TextView) view.findViewById(R.id.desc);
            time = (TextView) view.findViewById(R.id.time);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            image = (ImageView) view.findViewById(R.id.image);
        }

    }


}

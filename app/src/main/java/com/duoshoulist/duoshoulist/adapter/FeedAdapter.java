package com.duoshoulist.duoshoulist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dan on 2016-01-15.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<FeedItem> mdata;

    public FeedAdapter(Context context, List<FeedItem> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mdata = items;
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
        holder.id = mdata.get(position).getId();

        viewHolder.likes.setText(mdata.get(position).getLikes().toString());
        viewHolder.name.setText(mdata.get(position).getName().toString());
        viewHolder.desc.setText(mdata.get(position).getDesc().toString());

        String image = mdata.get(position).getImage();
        String avatar = mdata.get(position).getAvatar();
        Glide.with(holder.avatar.getContext()).load(avatar).into(viewHolder.avatar);
        Glide.with(holder.image.getContext()).load(image).crossFade().into(viewHolder.image);

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
        public Integer id;


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

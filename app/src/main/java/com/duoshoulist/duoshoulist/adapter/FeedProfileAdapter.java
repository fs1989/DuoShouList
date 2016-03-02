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

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.DetailActivity;
import com.duoshoulist.duoshoulist.bmob.FeedItem;

import java.util.List;

/**
 * Created by Dan on 2016-01-15.
 */
public class FeedProfileAdapter extends RecyclerView.Adapter<FeedProfileAdapter.ViewHolder> {

    private String TAG = "FeedAdapter";

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<FeedItem> mdata;
    Context context;

    public FeedProfileAdapter(Context context, List<FeedItem> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mdata = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_feed_profile, viewGroup, false);
        itemLayout.setBackgroundResource(mBackground);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = new ViewHolder(holder.mView);

        holder.objectId = mdata.get(position).getObjectId();

        String image = mdata.get(position).getImage();

        // Load image
        if (image != null) {
            Glide.with(holder.image.getContext()).load(image).crossFade().into(viewHolder.image);
        }

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
        public final ImageView image;
        public String objectId;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            image = (ImageView) view.findViewById(R.id.image);
        }

    }


}

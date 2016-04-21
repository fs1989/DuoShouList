package com.duoshoulist.duoshoulist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by Dan on 2016/2/1.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    final String TAG = "ImagesAdapter";
    Context context;
    private List<BmobFile> imagePaths;

    OnImageClickListener onImageClickListener;

    public ImagesAdapter(Context context, List<BmobFile> imagePaths) {
        this.imagePaths = imagePaths;
        this.context = context;
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_image, viewGroup, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = new ViewHolder(holder.mView);

        BmobFile bmobFile = imagePaths.get(position);
        Log.i(TAG, "toString: " + bmobFile.toString());
        Log.i(TAG, "getUrl: " + bmobFile.getUrl());

        if (bmobFile != null) {
            Glide.with(viewHolder.imageView.getContext()).load(bmobFile.getUrl()).into(viewHolder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickListener.onImageClicked(v);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            imageView = (ImageView) view.findViewById(R.id.item_image);
        }

    }

    public interface OnImageClickListener {
        void onImageClicked(View v);
        void onImageLoaded();
    }

}

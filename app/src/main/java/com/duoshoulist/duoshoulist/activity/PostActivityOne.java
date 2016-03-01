package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dan on 2016-02-29.
 */

public class PostActivityOne extends AppCompatActivity {

    private String TAG = "PostActivityOne";

    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    StringBuffer stringBuffer;

    Toolbar toolbar;

    @Bind(R.id.post_one_name)
    TextView name;
    @Bind(R.id.post_one_brand)
    TextView brand;
    @Bind(R.id.post_one_price)
    TextView price;

    @Bind(R.id.post_one_add)
    ImageButton imageButton;

    @Bind(R.id.gridView)
    GridView gridView;

    private int columnWidth;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_one);
        ButterKnife.bind(this);

        setupToolbar();

        stringBuffer = new StringBuffer();

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // Item Width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;

        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(PostActivityOne.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(PostActivityOne.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(9); // 最多选择照片数量，默认为9
                intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
//                ImageConfig config = new ImageConfig();
//                config.minHeight = 400;
//                config.minWidth = 400;
//                config.mimeType = new String[]{"image/jpeg", "image/png"};
//                config.minSize = 1 * 1024 * 1024; // 1Mb
//                intent.setImageConfig(config);
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
            }
        });

    }

    private void setupToolbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.post_one_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.post_one_next:
                        Log.i(TAG, "下一步被点击了");
                        stringBuffer.setLength(0);

                        if (name.getText().length() == 0) {
                            stringBuffer.append("请输入商品名称\r\n");
                            Log.i(TAG, "1: " + stringBuffer.toString());
                        }

                        if (price.getText().length() == 0) {
                            stringBuffer.append("请输入商品价格\r\n");
                            Log.i(TAG, "2: " + stringBuffer.toString());
                        }

                        if (imagePaths != null) {
                            if (imagePaths.size() < 3) {
                                stringBuffer.append("请至少上传3张图片");
                                Log.i(TAG, "3: " + stringBuffer.toString());
                            }
                        } else {
                            stringBuffer.append("请至少上传3张图片");
                            Log.i(TAG, "3: " + stringBuffer.toString());
                        }

                        if (stringBuffer.length() > 0) {
                            new MaterialDialog.Builder(PostActivityOne.this)
                                    .title("信息不完整")
                                    .content(stringBuffer.toString())
                                    .positiveText("好的")
                                    .show();
                            Log.i(TAG, "if方法执行了");
                        } else {
                            Log.i(TAG, "else方法执行了");
                            Intent intent = new Intent(PostActivityOne.this, PostActivityTwo.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("name", name.getText().toString());
                            bundle.putSerializable("price", price.getText().toString());
                            bundle.putSerializable("list", imagePaths);
                            if (brand.getText() != null) {
                                bundle.putSerializable("brand", brand.getText().toString());
                            } else {
                                bundle.putSerializable("brand", null);
                            }
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    // 选择照片
                    case REQUEST_CAMERA_CODE:
                        loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                        break;
                    // 预览
                    case REQUEST_PREVIEW_CODE:
                        loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                        break;
                    // 调用相机拍照
                    case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                        if (captureManager.getCurrentPhotoPath() != null) {
                            captureManager.galleryAddPic();

                            ArrayList<String> paths = new ArrayList<>();
                            paths.add(captureManager.getCurrentPhotoPath());
                            loadAdpater(paths);
                        }
                        break;

                }
            }
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.meun_post_one, menu);
            return true;
        }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);

        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }


    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_post, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            } else {
                imageView = (ImageView) convertView.getTag();
            }

            Glide.with(PostActivityOne.this)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }
}

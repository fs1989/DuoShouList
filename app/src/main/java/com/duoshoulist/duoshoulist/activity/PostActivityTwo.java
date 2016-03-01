package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.Category;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Dan on 2016-02-29.
 */
public class PostActivityTwo extends AppCompatActivity {

    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private int columnWidth;
    private GridAdapter gridAdapter;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private String TAG = "PostActivityTwo";
    Toolbar toolbar;

    @Bind(R.id.post_two_desc)
    AppCompatEditText desc;
    @Bind(R.id.post_two_link)
    AppCompatEditText link;

    @Bind(R.id.post_two_textInputLayout_des)
    TextInputLayout textInputLayoutDes;
    @Bind(R.id.post_two_textInputLayout_link)
    TextInputLayout TextInputLayoutLink;

    @Bind(R.id.post_two_category)
    TextView textViewCategory;
    @Bind(R.id.post_two_wechat)
    SwitchCompat switchCompat;
    @Bind(R.id.post_two_grid)
    GridView gridView;

    @Bind(R.id.category_linear_layout)
    LinearLayout categoryLinearLayout;

    List<Category> categoryList = new ArrayList<>();

    String name;
    String price;
    String brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_two);
        ButterKnife.bind(this);

        name = (String) getIntent().getSerializableExtra("name");
        price = (String) getIntent().getSerializableExtra("price");
        brand = (String) getIntent().getSerializableExtra("brand");

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(PostActivityTwo.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });

        getCategory();
        textInputLayoutDes.getEditText().addTextChangedListener(new MyTextWatcher(textInputLayoutDes, "请输入至少20字的点评"));
        categoryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(PostActivityTwo.this)
                        .title("请选择一个分类")
                        .items("123，123，123")
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                textViewCategory.setText(text);
                                return true;
                            }
                        })
                        .positiveText("取消")
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.meun_post_two, menu);
        return true;
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

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);

        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
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
                    case R.id.post_two_submit:
                        check();
                        break;
                }
                return true;
            }
        });
    }

    private void getCategory() {
        BmobQuery<Category> query = new BmobQuery<Category>();
        query.addQueryKeys("categoryName");
        query.findObjects(this, new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> object) {
                categoryList = object;
            }

            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, "查询失败：" + msg);
            }
        });
    }

    private void check() {

        int desLength = textInputLayoutDes.getEditText().toString().length();
        int categoryLength = textInputLayoutDes.getEditText().toString().length();

        if (desLength < 20 && categoryLength != 0) {
            new MaterialDialog.Builder(this)
                    .title("信息不完整")
                    .content("请输入至少20字的点评")
                    .positiveText("好的")
                    .build();
        } else if (categoryLength == 0 && desLength >= 20) {
            new MaterialDialog.Builder(this)
                    .title("信息不完整")
                    .content("请选择一个分类")
                    .positiveText("好的")
                    .build();
        } else if (desLength < 20 && categoryLength == 0) {
            new MaterialDialog.Builder(this)
                    .title("信息不完整")
                    .content("请输入至少20字的点评" + "\r\n" +"请选择一个分类")
                    .positiveText("好的")
                    .build();
        } else if (desLength >= 20 && categoryLength != 0) {
//            submit();
            Log.i(TAG, "模拟上传执行了");
        }
    }

    private void submit() {
        FeedItem feedItem = new FeedItem();

//注意：不能调用gameScore.setObjectId("")方法
        feedItem.setTitle(name);
        feedItem.setDesc(desc.getText().toString());
        feedItem.setBrand(brand);
//        feedItem.setImage();

        feedItem.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                Log.i(TAG, "feedItem上传成功");
            }

            @Override
            public void onFailure(int code, String arg0) {
                // 添加失败
            }
        });
    }


    class MyTextWatcher implements TextWatcher {
        private TextInputLayout mTextInputLayout;
        private String errorInfo;

        public MyTextWatcher(TextInputLayout textInputLayout, String errorInfo) {
            this.mTextInputLayout = textInputLayout;
            this.errorInfo = errorInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mTextInputLayout.getEditText().getText().toString().length() < 20) {
                mTextInputLayout.setErrorEnabled(true);//是否设置错误提示信息
                mTextInputLayout.setError(errorInfo);//设置错误提示信息
            } else {
                mTextInputLayout.setErrorEnabled(false);//不设置错误提示信息
            }
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
            Glide.with(PostActivityTwo.this)
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

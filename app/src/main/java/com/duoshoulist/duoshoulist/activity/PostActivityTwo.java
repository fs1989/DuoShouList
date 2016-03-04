package com.duoshoulist.duoshoulist.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import com.bmob.BmobProFile;
import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.Category;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.duoshoulist.duoshoulist.bmob.MyUser;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Dan on 2016-02-29.
 */
public class PostActivityTwo extends AppCompatActivity {
    Handler handler = new Handler();
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private int columnWidth;
    private ArrayList<String> imagePaths;
    private GridAdapter gridAdapter;

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
    @Bind(R.id.gridView)
    GridView gridView;

    @Bind(R.id.category_linear_layout)
    LinearLayout categoryLinearLayout;

    List<String> categoryList = new ArrayList<>();

    String title;
    String price;
    String brand;
    private FeedItem feedItem;
    private StringBuffer stringBuffer = new StringBuffer();
    private boolean isUploading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_two);
        ButterKnife.bind(this);
        setupToolbar();

        title = (String) getIntent().getSerializableExtra("name");
        price = (String) getIntent().getSerializableExtra("price");
        brand = (String) getIntent().getSerializableExtra("brand");
        imagePaths = (ArrayList<String>) getIntent().getSerializableExtra("list");

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
                PhotoPreviewIntent intent = new PhotoPreviewIntent(PostActivityTwo.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });

        loadAdapter(imagePaths);

        getCategory();

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


        textInputLayoutDes.getEditText().addTextChangedListener(new MyTextWatcher(textInputLayoutDes, "请输入至少20字的点评"));
        categoryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(PostActivityTwo.this)
                        .title("请选择一个分类")
                        .items(categoryList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                textViewCategory.setText(text);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_post_two, menu);
        return true;
    }

    private void setupToolbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.post_two_toolbar);
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
                for (Category categoryName : object)
                    categoryList.add(categoryName.getCategoryName());
            }

            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, "查询失败：" + msg);
            }
        });
    }

    private void check() {

        stringBuffer.setLength(0);
        int desLength = textInputLayoutDes.getEditText().toString().length();
        int categoryLength = textInputLayoutDes.getEditText().toString().length();

        if (desLength < 20) {
            stringBuffer.append("请输入至少20字的点评\n");
        }

        if (categoryLength == 0) {
            stringBuffer.append("请选择一个分类\n");
        }

        if (stringBuffer.length() > 0) {
            new MaterialDialog.Builder(this)
                    .title("信息不完整")
                    .content(stringBuffer.toString())
                    .positiveText("好的")
                    .build();
        } else {
            if (isUploading != true) {
                uploadPics();
            }
        }
    }

    private void uploadPics() {
        isUploading = true;
        String[] files = new String[imagePaths.size()];

        for (int i = 0; i < imagePaths.size(); i++) {
            files[i] = imagePaths.get(i);
        }

        BmobProFile.getInstance(PostActivityTwo.this).uploadBatch(files, new com.bmob.btp.callback.UploadBatchListener() {
            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] bmobFiles) {
                Log.i(TAG, "图片上传成功");
                List paths = Arrays.asList(bmobFiles);
                if (isFinish) {
                    createFeedItem(paths, bmobFiles[0].getUrl());
                }
                // isFinish ：批量上传是否完成
                // fileNames：文件名数组
                // urls : url：文件地址数组
                // files : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                // 注：若上传的是图片，url(s)并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`版本可直接从BmobFile中获得可访问的文件地址。
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
                Log.i("bmob", "onProgress :" + curIndex + "---" + curPercent + "---" + total + "----" + totalPercent);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob", "批量上传出错：" + statuscode + "--" + errormsg);
            }
        });
    }

    private void createFeedItem(List<BmobFile> list, String url) {
        feedItem = new FeedItem();

        feedItem.setTitle(title);
        feedItem.setLikeCount(0);
        feedItem.setDesc(desc.getText().toString());
        feedItem.setPrice(price);
        feedItem.setBrand(brand);
        feedItem.setImage(url);
        feedItem.setImagePaths(list);
        feedItem.setUser(BmobUser.getCurrentUser(PostActivityTwo.this, MyUser.class));

        feedItem.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                feedItem.getObjectId();
                Log.i(TAG, "feedItem上传成功");
                Snackbar.make(gridView, "提交成功", Snackbar.LENGTH_LONG).show();
                if (PostActivityOne.class != null) {
                    PostActivityOne.postActivityOne.finish();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PostActivityTwo.this.finish();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(int code, String arg0) {
                // 添加失败
                // TODO: 2016/3/2
                Snackbar.make(gridView, "提交失败", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void loadAdapter(ArrayList<String> paths) {

        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
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

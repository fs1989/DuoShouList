package com.duoshoulist.duoshoulist.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Dan on 2016/3/1.
 */
public class Category extends BmobObject{
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

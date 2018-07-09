package com.vmloft.develop.app.timeline.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVUser;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppActivity;
import com.vmloft.develop.app.timeline.common.bean.Account;
import com.vmloft.develop.app.timeline.common.C;

import com.vmloft.develop.app.timeline.widget.VMTopBar;
import com.vmloft.develop.library.tools.utils.VMFile;
import com.vmloft.develop.library.tools.widget.VMToast;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by lzan13 on 2015/4/23.
 */
public class AccountActivity extends AppActivity {

    @BindView(R.id.widget_top_bar) VMTopBar topBar;

    private Account account;

    private File tempFile;
    private Uri tempUri;
    private boolean isAvatar = true;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_account;
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        ButterKnife.bind(activity);

        initInfo();

        initTopBar();
        initSwipeRefreshLayout();
    }

    /**
     * 初始化账户数据
     */
    private void initInfo() {
        account = AVUser.getCurrentUser(Account.class);
    }

    private void initTopBar() {
        topBar.setTitle(R.string.account_info);
        topBar.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initSwipeRefreshLayout() {

        //        mSwipeRefreshLayout.setProgressViewOffset(true, 0, MLScreen.dp2px(R.dimen.dimen_48));
        //mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.orange, R.color.green, R.color.red);
        //mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //    @Override
        //    public void onRefresh() {
        //    }
        //});
    }

    /**
     * 设置显示当前账户数据
     */
    private void showUserInfo() {

    }

    /**
     * 从图库获取图片
     */
    private void startGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是图库，返回选择图片的地址
        startActivityForResult(intent, C.PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相机获取图片
     */
    private void startCamera() {
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (VMFile.hasSdcard()) {
            tempFile = new File(VMFile.getCacheFromSDCard(), C.TEMP_PHOTO);
            // 从文件中创建uri
            tempUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        }
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是相机，返回选择图片的地址
        startActivityForResult(intent, C.PHOTO_REQUEST_CAMERA);
    }

    /**
     * 剪切图片，根据传入的宽高决定图片大小，使用Uri 不返回数据
     *
     * @param uri
     * @param width
     * @param height
     */
    private void clipImage(Uri uri, int width, int height) {
        String tempPath = VMFile.getCacheFromSDCard() + C.TEMP_PHOTO;
        tempUri = Uri.fromFile(new File(tempPath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if (isAvatar) {
            // 设置剪切框大小为1:1
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        } else {
            // 设置剪切框大小为1:1
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 2);
        }
        // 设置输出图片大小
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);

        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, C.PHOTO_REQUEST_CLIP);
    }

    /**
     * 通过Uri获取Bitmap
     *
     * @param uri
     */
    private Bitmap decodeUriToBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case C.PHOTO_REQUEST_CAMERA:
            if (VMFile.hasSdcard()) {
                Uri uri = Uri.fromFile(tempFile);
                if (isAvatar) {
                    clipImage(uri, 512, 512);
                } else {
                    clipImage(uri, 900, 600);
                }
            }
            break;
        case C.PHOTO_REQUEST_GALLERY:
            if (data != null) {
                Uri uri = data.getData();
                if (isAvatar) {
                    clipImage(uri, 512, 512);
                } else {
                    clipImage(uri, 900, 600);
                }
            }
            break;
        case C.PHOTO_REQUEST_CLIP:
            Bitmap bitmap = null;
            try {
                bitmap = decodeUriToBitmap(tempUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 拦截返回按键
     *
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}

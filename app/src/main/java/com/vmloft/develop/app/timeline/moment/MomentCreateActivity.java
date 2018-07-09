package com.vmloft.develop.app.timeline.moment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import com.avos.avoscloud.AVUser;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.base.AppMVPActivity;
import com.vmloft.develop.app.timeline.common.bean.Account;
import com.vmloft.develop.app.timeline.common.C;

import com.vmloft.develop.app.timeline.common.bean.Moment;
import com.vmloft.develop.app.timeline.moment.MomentCreateContract.IMomentCreateView;
import com.vmloft.develop.app.timeline.moment.MomentCreateContract.IMomentCreatePresenter;

import com.vmloft.develop.app.timeline.widget.VMTopBar;
import com.vmloft.develop.library.tools.utils.VMFile;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap;
import com.vmloft.develop.library.tools.widget.VMToast;

/**
 * Created by lzan13 on 2015/5/10.
 */
public class MomentCreateActivity extends AppMVPActivity<IMomentCreateView, IMomentCreatePresenter<IMomentCreateView>> implements IMomentCreateView {

    @BindView(R.id.widget_top_bar) VMTopBar topBar;
    @BindView(R.id.img_image) ImageView imageView;
    @BindView(R.id.edit_content) EditText contentEdit;

    private Account account;
    private String imagePath;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_create_note;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initView() {
        initTopBar();
        account = AVUser.getCurrentUser(Account.class);
        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                VMLog.d("beforeTextChanged: s " + s + "; start " + start + "; count " + count + "; after " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VMLog.d("onTextChanged: s " + s + "; start " + start + "; before " + before + "; count " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                VMLog.d("onTextChanged: s " + s.toString());
            }
        });
    }

    private void initTopBar() {
        topBar.setTitle(R.string.moment_new);
        topBar.setLeftIcon(R.drawable.ic_close);
        topBar.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });
        topBar.setRightIcon(R.drawable.ic_send);
        topBar.setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTextMoment();
            }
        });
    }

    @Override
    public IMomentCreatePresenter<IMomentCreateView> createPresenter() {
        return new MomentCreatePresenterImpl();
    }

    /**
     * 控件点击事件监听器
     */
    @OnClick({ R.id.img_image })
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.img_image:
            startGallery();
            break;
        }
    }

    /**
     * 添加一条文本 Moment
     */
    private void createTextMoment() {
        String content = contentEdit.getText().toString();
        Moment moment = new Moment();
        moment.setAuthor(account);
        moment.setContent(content);
        presenter.createMoment(moment);
    }

    /**
     * 创建新的 note 结果
     */
    @Override
    public void onCreateMomentResult(CResult result) {
        if (result.code == 0) {
            VMToast.make("保存完成").showDone();
            onFinish();
        } else {
            VMToast.make(result.msg).showError();
        }
    }

    /**
     * 调用系统图库选择图片
     */
    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是图库，返回选择图片的地址
        startActivityForResult(intent, C.PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case C.PHOTO_REQUEST_GALLERY:
            if (data != null) {
                Uri uri = data.getData();
                imagePath = VMFile.getPath(uri);
                Bitmap bitmap = VMBitmap.loadBitmapThumbnail(imagePath, 256);
                imageView.setImageBitmap(bitmap);
            }
            break;
        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 取消编辑确认
     */
    private void cancelEdit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(VMStr.strByResId(R.string.cancel_edit));
        dialog.setMessage(VMStr.strByResId(R.string.cancel_edit_msg));
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFinish();
            }
        });
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
    }

    /**
     * 拦截返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            cancelEdit();
            break;
        }
        return true;
    }
}

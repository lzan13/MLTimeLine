package net.melove.app.ml.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import net.melove.app.ml.utils.MLLog;


/**
 * Created by lzan13 on 2014/12/17.
 */
public class MLScrollView extends ScrollView {


    private View mTopView;       // 头部控件
    private Rect mTopRect = null;
    private View mBottomView;    // 去除头部控件外，此ScrollView下半部分包含的所有控件
    private Rect mBottomRect = null;  //保存正常状态喜爱的底部View的矩形框
    private View mContentView;
    private Rect mContentRect = null;

    private float MOVE_RATIO = 0.3f;

    private float mStartTouchY, mEndTouchY, mDragDistance;
    private int mStartTop, mStartBottom;
    private int mCurrentTop, mCurrentBottom;

    private DragState mDragState = DragState.DRAG_NORMAL;
    private boolean isDragAble = false;

    // 记录当前拖动状态
    private enum DragState {
        DRAG_NORMAL,
        DRAG_DOWN,
        DRAG_UP,
    }

    public MLScrollView(Context context) {
        super(context);
    }

    public MLScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    public void setTopView(View view) {
        mTopView = view;
    }

    public void setBottomView(View view) {
        mBottomView = view;
        int w = this.getContext().getResources().getDisplayMetrics().widthPixels;
        int h = this.getContext().getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams lp = mBottomView.getLayoutParams();
        lp.width = w;
        lp.height = h - mTopView.getMeasuredHeight()/2;
        MLLog.i("mBottomView.w." + lp.width + "mBottomView.h." + lp.height);
        mBottomView.setLayoutParams(lp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mBottomView != null) {
            mlTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    private void mlTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartTouchY = event.getY();
                mCurrentTop = mStartTop = mBottomView.getTop();
                mCurrentBottom = mStartBottom = mBottomView.getBottom();

                break;
            case MotionEvent.ACTION_MOVE:
                mEndTouchY = event.getY();
                mDragDistance = mEndTouchY - mStartTouchY;

                if (isDragEnable()) {
                    dragDown(event);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                backNormal();
                break;
            default:
                break;
        }
    }

    private void dragDown(MotionEvent event) {
        if (mTopRect == null) {
            mTopRect = new Rect();
            mTopRect.set(mTopView.getLeft(),
                    mTopView.getTop(),
                    mTopView.getRight(),
                    mTopView.getBottom());
        }
        int top = (int) (mTopRect.top + mDragDistance * MOVE_RATIO * MOVE_RATIO);
        int bottom = (int) (mTopRect.bottom + mDragDistance * MOVE_RATIO * MOVE_RATIO);
        mTopView.layout(mTopRect.left, top, mTopRect.right, bottom);

        if (mBottomRect == null) {
            mBottomRect = new Rect();
            mBottomRect.set(mBottomView.getLeft(),
                    mBottomView.getTop(),
                    mBottomView.getRight(),
                    mBottomView.getBottom());
        }
        mCurrentTop = (int) (mBottomRect.top + mDragDistance * MOVE_RATIO);
        mCurrentBottom = (int) (mBottomRect.bottom + mDragDistance * MOVE_RATIO);
        mBottomView.layout(mBottomRect.left, mCurrentTop, mBottomRect.right, mCurrentBottom);
        MLLog.i("mCurrentTop." + mCurrentTop + "; mCurrentBottom." + mCurrentBottom);

    }

    private void backNormal() {
        TranslateAnimation topAnim = new TranslateAnimation(0, 0,
                mTopView.getTop() - mTopRect.top, 0);
        topAnim.setDuration(300);
        mTopView.startAnimation(topAnim);
        mTopView.layout(mTopRect.left, mTopRect.top, mTopRect.right, mTopRect.bottom);

        TranslateAnimation bottomAnim = new TranslateAnimation(0, 0,
                mBottomView.getTop() - mBottomRect.top, 0);
        bottomAnim.setDuration(300);
        mBottomView.startAnimation(bottomAnim);
        mBottomView.layout(mBottomRect.left, mBottomRect.top, mBottomRect.right, mBottomRect.bottom);
    }

    private boolean isDragEnable() {
//        if(mTopView.getTop() > mTopRect.top || mBottomView.getTop() < mBottomRect.top){
//            return true;
//        }
        float scrollActualHeight = mContentView.getMeasuredHeight();
        float scrollShowHeight = getHeight();
        float scrollY = getScrollY();
        float offset = scrollActualHeight - scrollShowHeight;

        return scrollY == 0 || scrollY == offset;
    }


}

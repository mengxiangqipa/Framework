package com.framework.customview;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.framework.R;

/**
 * @author Yobert Jomi
 * className AutoTextView
 * created at  2016/7/22  15:55
 */
public class CustomTextSwitcher extends TextSwitcher implements
        ViewSwitcher.ViewFactory {

    private float mHeight = 14;
    private Context mContext;
    //mInUp,mOutUp分离构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    //mInDown,mOutDown分离构成向下翻页的进出动画
    private Rotate3dAnimation mInDown;
    private Rotate3dAnimation mOutDown;
    private View customView;

    public CustomTextSwitcher(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public CustomTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedValue typedValue = new TypedValue();
//        context.getTheme().resolveAttribute(android.R.attr.textAppearanceLarge, typedValue, true);
//        int[] attribute = new int[] { android.R.attr.textSize };
//        TypedArray array = context.obtainStyledAttributes(typedValue.resourceId, attribute);
//        int textSize = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
//        array.recycle();

//        TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TextView);
//        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize});
//        mHeight = a.getDimension(android.R.attr.textSize, 36);
//        a.recycle();
//        mHeight = Utils.dip2px(context, 12);
        mContext = context;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        setFactory(this);
        mInUp = createAnim(-90, 0, true, true);
        mOutUp = createAnim(0, 90, false, true);
        mInDown = createAnim(90, 0, true, false);
        mOutDown = createAnim(0, -90, false, false);
        //TextSwitcher重要用于文件切换，比如 从文字A 切换到 文字 B，
        //setInAnimation()后，A将执行inAnimation，
        //setOutAnimation()后，B将执行OutAnimation
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }

    private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    /**
     * <pre>
     *     //这里返回的TextView，就是我们看到的View
     *     @return
     *     lastModified 2016/7/22  16:30
     *     </pre>
     */
    @Override
    public View makeView() {
        if (customView != null) {
            return customView;
        }
        LayoutParams parmas = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        parmas.gravity = Gravity.CENTER;
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.CENTER);
        t.setLayoutParams(parmas);
        t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mHeight);
        t.setTextColor(getResources().getColor(R.color.colorCustomTextSwitcher));
        t.setSingleLine();
        return t;
    }

    public void makeViewCustom(View customView) {
        this.customView = customView;
        makeView();
    }

    /**
     * <pre>
     *     @param //定义动作，向下滚动翻页
     *     @return
     *     lastModified 2016/7/22  16:00
     *     </pre>
     */
    public void previous() {
        if (getInAnimation() != mInDown) {
            setInAnimation(mInDown);
        }
        if (getOutAnimation() != mOutDown) {
            setOutAnimation(mOutDown);
        }
    }

    /**
     * <pre>
     *     @param //定义动作，向上滚动翻页
     *     @return
     *     lastModified 2016/7/22  16:00
     *     </pre>
     */
    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private float mCenterX;
        private float mCenterY;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}

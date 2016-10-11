package com.github.sdw8001.materialspinner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

/**
 * Created by sdw80 on 2016-10-10.
 */
public class FlippableView extends FrameLayout {

    /**
     * A tag to display on log/debugging messages for this class
     */
    private static final String LOG_TAG = "FlippableView";

    /**
     * Stores a context object which can be accessed throughout this
     * widget class for tasks such as inflating Views.
     */
    private Context mContext;

    /**
     * The View used as a side of the card/widget.
     */
    private View mFrontView, mBackView;

    /**
     * This is responsible for containing the Views for both sides of
     * the card/widget. It is used to animate the flip when changing
     * from showing one side of the card to another.
     */
    private ViewFlipper mViewFlipper;

    /**
     * Whether or not the reverse side of the card/widget is currently
     * being displayed
     */
    private boolean mIsBackShowing;

    /**
     * If there should be an animation when this view is 'flipped'
     */
    private boolean mAnimate = true;


    public FlippableView(Context context) {
        this(context, null);
    }

    public FlippableView(Context context, View frontView, View backView) {
        this(context, null, frontView, backView);
    }

    public FlippableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlippableView(Context context, AttributeSet attrs, View frontView, View backView) {
        this(context, attrs, 0, frontView, backView);
    }

    public FlippableView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, null, null);
    }

    public FlippableView(Context context, AttributeSet attrs, int defStyleAttr,
                         View frontView, View backView) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mFrontView = frontView;
        mBackView = backView;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.FlippableView, defStyleAttr, 0);

        initializeView(a);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlippableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr, defStyleRes, null, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlippableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes,
                         View frontView, View backView) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mContext = context;
        mFrontView = frontView;
        mBackView = backView;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.FlippableView, defStyleAttr, defStyleRes);

        initializeView(a);
    }


    /**
     * Sets up views and widget attributes
     *
     * @param a The {@link android.content.res.TypedArray} passed from the
     *          constructor(s) used to retrieve XML attributes
     */
    private void initializeView(final TypedArray a) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View[] views = new View[] {mFrontView, mBackView};
        int[] styleables = new int[] {R.styleable.FlippableView_viewFront,
                R.styleable.FlippableView_viewBack};
        for (int i = 0; i < 2; i++) {
            if (views[i] != null) {
                continue;
            }

            int viewResId = a.getResourceId(styleables[i], -1);
            if (viewResId == -1) {
                Log.d(LOG_TAG, "Front and/or back view not set yet (via constructor " +
                        "or XML attribute - will be ignored for now)");
                views[i] = null;
            } else {
                views[i] = inflater.inflate(viewResId, null);
            }
        }
        a.recycle();

        inflater.inflate(R.layout.widget_flippable_view, this, true);
        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mViewFlipper = (ViewFlipper) getChildAt(0);

        if (mFrontView != null && mBackView != null) {
            updateFrontAndBack();
        }
    }


    /**
     * Updates both sides of the card/widget by removing both Views, and
     * re-adding them using the global variables.
     *
     * Listeners for click callbacks are set to these views, and they are
     * made sure to have the same height (as a piece of card could not be
     * longer on one side).
     */
    private void updateFrontAndBack() {
        mViewFlipper.removeAllViews();

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                flip();
            }
        };
        View[] views = new View[] {mFrontView, mBackView};
        for (int i = 0; i < 2; i++) {
            views[i].setOnClickListener(onClickListener);
            mViewFlipper.addView(views[i]);
        }
        mIsBackShowing = false;

        mViewFlipper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int flipperHeight = mViewFlipper.getHeight();
                mFrontView.setMinimumHeight(flipperHeight);
                mBackView.setMinimumHeight(flipperHeight);
            }
        });
    }


    /**
     * Flips the widget so that it displays the View on the opposite
     * side to what is currently being displayed. Whether or not it
     * animates depends on the boolean {@link #mAnimate}.
     *
     * @see #flip(boolean)
     */
    public void flip() {
        flip(mAnimate);
    }


    /**
     * Flips the widget so that it displays the View on the opposite
     * side to what is currently being displayed. Whether or not it
     * animates depends on the parameter passed.
     *
     * @param animate Whether or not the flip should be animated
     *
     * @see #flip()
     */
    public void flip(boolean animate) {
        if (mFrontView == null || mBackView == null) {
            throw new NullPointerException("You must specify a front and back view for the " +
                    "FlippableView, through either a constructor, XML attribute, or method");
        }

        if (!mIsBackShowing) {
            if (mViewFlipper.getDisplayedChild() == 1) {
                return;  // If there is a child (to the left), stop
            }

            if (animate) {
                mViewFlipper.setInAnimation(mContext, R.anim.grow_from_middle);
                mViewFlipper.setOutAnimation(mContext, R.anim.shrink_to_middle);
            }
            mViewFlipper.showPrevious();  // Display previous screen
        } else {
            if (mViewFlipper.getDisplayedChild() == 0) {
                return;  // If there aren't any other children, stop
            }

            if (animate) {
                mViewFlipper.setInAnimation(mContext, R.anim.grow_from_middle);
                mViewFlipper.setOutAnimation(mContext, R.anim.shrink_to_middle);
            }
            mViewFlipper.showNext();  // Display next screen
        }

        mIsBackShowing = !mIsBackShowing;
    }


    /**
     * @return the View used as the front of the card/widget
     */
    public View getFrontView() {
        return mFrontView;
    }

    /**
     * @return the View used as the back of the card/widget
     */
    public View getBackView() {
        return mBackView;
    }

    /**
     * @return the ViewFlipper associated with this widget
     */
    public ViewFlipper getViewFlipper() {
        return mViewFlipper;
    }

    /**
     * @return whether or not the back side of the card is currently
     * being displayed
     */
    public boolean isBackShowing() {
        return mIsBackShowing;
    }

    /**
     * Changes the View shown on the front side of the card
     *
     * @param frontView The View for the front side of the card
     *
     * @see #getFrontView()
     * @see #setBackView(View)
     * @see #setFrontAndBackViews(View, View)
     * @attr ref R.styleable#FlippableView_viewFront
     */
    public void setFrontView(View frontView) {
        mFrontView = frontView;
        updateFrontAndBack();
    }

    /**
     * Changes the View shown on the back side of the card
     *
     * @param backView The view for the back side of the card
     *
     * @see #getBackView()
     * @see #setFrontView(View)
     * @see #setFrontAndBackViews(View, View)
     * @attr ref R.styleable#FlippableView_viewBack
     */
    public void setBackView(View backView) {
        mBackView = backView;
        updateFrontAndBack();
    }

    /**
     * Changes the View shown on both sides of the card
     *
     * @param frontView The View for the front side of the card
     * @param backView The View for the back side of the card
     *
     * @see #setFrontView(View)
     * @see #setBackView(View)
     * @attr ref R.styleable#FlippableView_viewFront
     * @attr ref R.styleable#FlippableView_viewBack
     */
    public void setFrontAndBackViews(View frontView, View backView) {
        mFrontView = frontView;
        mBackView = backView;
        updateFrontAndBack();
    }

    /**
     * Removes the front and back Views from the card/widget
     *
     * @see #setFrontAndBackViews(View, View)
     */
    public void removeFrontAndBack() {
        mViewFlipper.removeAllViews();
    }


    /**
     * @return whether or not this view will display animations
     */
    public boolean isAnimated() {
        return mAnimate;
    }

    /**
     * Sets the boolean determining whether or this view should
     * animate when clicked
     *
     * @param animate Whether or not flipping this widget should
     *                display an animation
     *
     * @see #isAnimated()
     */
    public void setAnimate(boolean animate) {
        if (animate == mAnimate) {
            return;
        }
        mAnimate = animate;
        updateFrontAndBack();  // updating OnClickListeners
    }
}


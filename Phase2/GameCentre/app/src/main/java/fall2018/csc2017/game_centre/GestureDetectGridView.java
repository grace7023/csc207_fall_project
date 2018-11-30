package fall2018.csc2017.game_centre;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

import java.util.HashMap;

public class GestureDetectGridView<T extends Game> extends GridView {
    public static final int SWIPE_MIN_DISTANCE = 100;
    public static boolean detectFling = false;
    private GestureDetector gDetector;
    private MovementController<T> mController;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;


    /**
     * HashMap which lets the code associate the arg with an actual move rather
     * than a magic number
     */
    public static final HashMap<String, Integer> SWIPE_DIRECTION_ARG;

    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Up", 1);
        map.put("Down", 2);
        map.put("Left", 3);
        map.put("Right", 4);
        SWIPE_DIRECTION_ARG = map;
    }

    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mController = new MovementController<>();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                if (!detectFling) {
                    int position = GestureDetectGridView.this.pointToPosition
                            (Math.round(event.getX()), Math.round(event.getY()));
                    if (position >= 0) {
                        mController.processGameMovement(context, position);
                    }
                }
                return !detectFling;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            /**
             * Detect swiping movement
             *
             * Inspired from Section: Bonus: Detecting Swipe/Fling Direction section
             * http://codetheory.in/android-gesturedetector/
             * @param e1 Initial motion (started swiping)
             * @param e2 Final motion (stopped swiping)
             * @param velocityX velocity in x direction
             * @param velocityY velocity in y direction
             * @return true if swipe was successfully detected
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (detectFling) {
                    int moveArg = 0;
                    if (Math.abs(e1.getY() - e2.getY()) >= SWIPE_MIN_DISTANCE ||
                            Math.abs(e1.getX() - e2.getX()) >= SWIPE_MIN_DISTANCE) {
                        if (Math.abs(e1.getY() - e2.getY()) > Math.abs(e1.getX() - e2.getX())) {
                            moveArg = SWIPE_DIRECTION_ARG.get("Down");
                            if (e1.getY() - e2.getY() > 0) {
                                moveArg = SWIPE_DIRECTION_ARG.get("Up");
                            }

                        } else {
                            moveArg = SWIPE_DIRECTION_ARG.get("Left");
                            if (e1.getX() - e2.getX() < 0) {
                                moveArg = SWIPE_DIRECTION_ARG.get("Right");
                            }
                        }
                        mController.processGameMovement(context, moveArg);
                        return true;
                    }
                }
                return false;
                // return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    public void setGame(T game) {
        mController.setGame(game);
    }
}

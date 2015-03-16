package app.eggorchicken.eggorchicken;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

/**
 * Created by XXX on 3/16/2015.
 */
public class AnimationsContainer {
    public interface OnAnimationStoppedListener {

        public void AnimationStopped();

    }



    public int FPS = 60;  // animation FPS

    // single instance procedures
    private static AnimationsContainer mInstance;

    private AnimationsContainer() {
    };

    public static AnimationsContainer getInstance() {
        if (mInstance == null)
            mInstance = new AnimationsContainer();
        return mInstance;
    }

    // animation progress dialog frames
    private int[] mProgressAnimFrames = { R.drawable.reloj20, R.drawable.reloj19, R.drawable.reloj18, R.drawable.reloj17, R.drawable.reloj16,
            R.drawable.reloj15, R.drawable.reloj14, R.drawable.reloj13, R.drawable.reloj12, R.drawable.reloj11,
            R.drawable.reloj10, R.drawable.reloj9, R.drawable.reloj8, R.drawable.reloj7,R.drawable.reloj6,
            R.drawable.reloj5, R.drawable.reloj4, R.drawable.reloj3, R.drawable.reloj2, R.drawable.reloj1,
            R.drawable.reloj0 };

    /**
     * @param imageView
     * @return progress dialog animation
     */
    public FramesSequenceAnimation createProgressDialogAnim(ImageView imageView) {
        return new FramesSequenceAnimation(imageView, mProgressAnimFrames, 30);
    }

    /**
     * AnimationPlayer. Plays animation frames sequence in loop
     */
    public class FramesSequenceAnimation {
        private int[] mFrames; // animation frames
        private int mIndex; // current frame
        private boolean mShouldRun; // true if the animation should continue running. Used to stop the animation
        private boolean mIsRunning; // true if the animation currently running. prevents starting the animation twice
        private SoftReference<ImageView> mSoftReferenceImageView; // Used to prevent holding ImageView when it should be dead.
        private Handler mHandler;
        private int mDelayMillis;
        private OnAnimationStoppedListener mOnAnimationStoppedListener;

        private Bitmap mBitmap = null;
        private BitmapFactory.Options mBitmapOptions;

        public void setOnClickListener(OnAnimationStoppedListener l) {
            mOnAnimationStoppedListener=l;
        }

        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps) {
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference<ImageView>(imageView);
            mShouldRun = false;
            mIsRunning = false;
            mDelayMillis = 1000 / fps;
            mDelayMillis = 1000;

            imageView.setImageResource(mFrames[0]);

            // use in place bitmap to save GC work (when animation images are the same size & type)
            if (Build.VERSION.SDK_INT >= 11) {
                Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                Bitmap.Config config = bmp.getConfig();
                mBitmap = Bitmap.createBitmap(width, height, config);
                mBitmapOptions = new BitmapFactory.Options();
                // setup bitmap reuse options.
                mBitmapOptions.inBitmap = mBitmap;
                mBitmapOptions.inMutable = true;
                mBitmapOptions.inSampleSize = 1;
            }
        }

        private int getNext() {
            mIndex++;
//            if (mIndex >= mFrames.length)
//                mIndex = 0;
            if (mIndex >= mFrames.length) {
                mIndex = mFrames.length-1;
                stop();
//                mOnAnimationStoppedListener.AnimationStopped();
            }
            return mFrames[mIndex];
        }

        /**
         * Starts the animation
         */
        public synchronized void start() {
            mShouldRun = true;
            if (mIsRunning)
                return;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null) {
                        mIsRunning = false;
                        if (mOnAnimationStoppedListener != null) {
                            mOnAnimationStoppedListener.AnimationStopped();
                        }
                        return;
                    }

                    mIsRunning = true;
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown()) {
                        int imageRes = getNext();
                        if (mBitmap != null) { // so Build.VERSION.SDK_INT >= 11
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, mBitmapOptions);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageResource(imageRes);
                                mBitmap.recycle();
                                mBitmap = null;
                            }
                        } else {
                            imageView.setImageResource(imageRes);
                        }
                    }

                }
            };

            mHandler.post(runnable);
        }

        /**
         * Stops the animation
         */
        public synchronized void stop() {
            mShouldRun = false;
        }
    }
}

package toa.toa;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * TextView extended class with transparent carved-through-background text. <br/>
 * However, drawables from TextView are considered as part of background. <br/>
 * <br/>
 * Text can be seen again after call to  {@link android.widget.TextView#setTextColor(int)}
 * as it's color is set to Color.TRANSPARENT after carving. <br/>
 * <br/>
 * Consider using android:freezesText="true" to save programmatically done text changes (if any).
 * Manually save other changes (background, textSize).
 * <br/>
 * DO NOT OVERRIDE {@link android.widget.TextView#getBackground()}!
 *
 * @author Margarita Litkevych
 * @version 1.0
 */
public class ClearTextView extends TextView {

    /**
     * Saves initial background drawable
     * in order to allow Android to care
     * about it's size on text, background
     * and configuration changes.
     */
    private Drawable mBackground;

    private Paint mEraserPaint;

    public ClearTextView(Context context) {
        super(context);
        setup();
        mBackground = getBackground();
    }

    public ClearTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
        mBackground = getBackground();
    }

    public ClearTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
        mBackground = getBackground();
    }

    private void setup() {
        mEraserPaint = new Paint();
        mEraserPaint.set(getPaint());
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public Paint getEraserPaint() {
        return mEraserPaint;
    }

    public void setEraserPaint(Paint eraserPaint) {
        if (eraserPaint != null) {
            mEraserPaint = eraserPaint;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {

            if (mBackground == null) {
                mBackground = getBackground();
            } else {
                setBackgroundDrawable(mBackground);
            }

            if (mBackground != null) {
                mBackground.setBounds(0, 0, w, h);
                carveText();
            }
        }
    }

    /**
     * Copies text as bitmap and applies it as mask to the bitmap background.
     * Sets result as background.
     * Set text color to transparent in order to let {@link android.widget.TextView#getText()} work correctly.
     */
    private void carveText() {
        if (mBackground != null) {
            Rect rect = mBackground.getBounds();
            if (rect != null && rect.isEmpty() == false) {
                //copy background to bitmap
                Bitmap background = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(background);
                mBackground.draw(canvas);

                //copy text to bitmap
                Bitmap text = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                setBackgroundDrawable(null);
                if (getCurrentTextColor() == Color.TRANSPARENT) {
                    setTextColor(Color.BLACK);
                }
                draw(new Canvas(text));

                //apply text bitmap as mask
                if (mEraserPaint == null) {
                    setup();
                }
                canvas.drawBitmap(text, 0, 0, mEraserPaint);

                //set result as background drawable
                setBackgroundDrawable(new BitmapDrawable(getResources(), background));

                //set text color transparent and let getText work correctly
                setTextColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (mBackground != null) {
            setBackgroundDrawable(mBackground);
            carveText();
        } else {
            Log.e("ClearTextView", "background cannot be restored.");
        }
    }

    /**
     * Sets background to null. Call {@link android.widget.TextView#setTextColor(int)} to make text visible.
     */
    public void clearBackground() {
        super.setBackgroundDrawable(null);
        mBackground = null;
    }

    private void onBackgroundChanged(Drawable d) {
        if (d == null) {
            clearBackground();
        } else {
            if (d.equals(mBackground) == false) {
                mBackground = d;
                carveText();
            }
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        onBackgroundChanged(getBackground());
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        onBackgroundChanged(getBackground());
    }

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            super.setBackground(background);
            onBackgroundChanged(getBackground());
        } else {
            setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        onBackgroundChanged(getBackground());
    }

    /**
     * Get initial background
     *
     * @return background without carved-through text
     */
    public Drawable getFilledBackground() {
        return mBackground;
    }
}
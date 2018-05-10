package fifthgen.com.brotherandroidprinter.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageUtil {

    private static final float TEXT_SIZE = 36.0f;
    private static final int TEXT_COLOR = Color.BLACK;

    public static Bitmap textAsBitMap(String... text) {

        if (text.length > 0) {
            Paint paint = new Paint();
            paint.setTextSize(TEXT_SIZE);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.LEFT);
            float baseline = -paint.ascent();
            int width = (int) (paint.measureText(text[0]) + 0.5f);
            int height = (int) (baseline + paint.descent() + 0.5f);
            Bitmap image = Bitmap.createBitmap(width + 500, height + 350, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(image);
            canvas.drawRect(0, 0, width + 500, height + 350, paint);
            paint.setColor(TEXT_COLOR);

            for (int i = 0; i < text.length; i++) {
                canvas.drawText(text[i], 0, baseline + (i * 100), paint);
            }

            return image;
        }

        return null;
    }
}

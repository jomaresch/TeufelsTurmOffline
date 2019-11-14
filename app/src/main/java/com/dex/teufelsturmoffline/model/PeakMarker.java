package com.dex.teufelsturmoffline.model;


import com.dex.teufelsturmoffline.activities.MapActivity;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

public class PeakMarker extends Marker {
    private Peak peak;
    private MapActivity mapActivity;
    private Paint paintText = AndroidGraphicFactory.INSTANCE.createPaint();
    private Paint paintOutline = AndroidGraphicFactory.INSTANCE.createPaint();
    private Paint paintMarker = AndroidGraphicFactory.INSTANCE.createPaint();

    public PeakMarker(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset, Peak peak, MapActivity mapActivity) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.peak = peak;
        this.mapActivity = mapActivity;
        paintOutline.setColor(Color.RED);
        paintOutline.setStrokeWidth(2);
        paintOutline.setStyle(Style.STROKE);

        paintMarker.setColor(Color.WHITE);

        paintText.setTextSize(55);
    }

    @Override
    public boolean onTap(LatLong geoPoint, Point viewPosition,
                         Point tapPoint) {
        if (contains(viewPosition, tapPoint)) {
            mapActivity.openDialog(peak.getTt_name());
        }
        return false;
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        if (this.getLatLong() == null || this.getBitmap() == null || this.getBitmap().isDestroyed()) {
            return;
        }

        long mapSize = MercatorProjection.getMapSize(zoomLevel, this.displayModel.getTileSize());
        double pixelX = MercatorProjection.longitudeToPixelX(this.getLatLong().longitude, mapSize);
        double pixelY = MercatorProjection.latitudeToPixelY(this.getLatLong().latitude, mapSize);

        int halfBitmapWidth = this.getBitmap().getWidth() / 2;
        int halfBitmapHeight = this.getBitmap().getHeight() / 2;

        int left = (int) (pixelX - topLeftPoint.x - halfBitmapWidth + this.getHorizontalOffset());
        int top = (int) (pixelY - topLeftPoint.y - halfBitmapHeight + this.getVerticalOffset());
        int right = left + this.getBitmap().getWidth();
        int bottom = top + this.getBitmap().getHeight();

        Rectangle bitmapRectangle = new Rectangle(left, top, right, bottom);
        Rectangle canvasRectangle = new Rectangle(0, 0, canvas.getWidth(), canvas.getHeight());
        if (!canvasRectangle.intersects(bitmapRectangle)) {
            return;
        }

        canvas.drawBitmap(this.getBitmap(), left, top);

       if (this.paintText != null && (int) zoomLevel > 16) {
            String text = this.peak.getTt_name();
  //          canvas.drawLine(0,0,40,40, this.paintText);
            canvas.drawText(text, left + this.getBitmap().getWidth() , top + halfBitmapHeight + 20, this.paintText);
  //          canvas.drawCircle(left+halfBitmapWidth,top+halfBitmapWidth,halfBitmapWidth, paintMarker);
  //          canvas.drawCircle(left+halfBitmapWidth,top+halfBitmapWidth,halfBitmapWidth, paintOutline);
        }

 //       if (this.paintText != null && (int) zoomLevel <= 16) {
 //           canvas.drawCircle(left+halfBitmapWidth,top+halfBitmapWidth,halfBitmapWidth/2, paintMarker);
 //           canvas.drawCircle(left+halfBitmapWidth,top+halfBitmapWidth,halfBitmapWidth/2, paintOutline);
        //}
    }
}

package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.List;

/**
 * Scroller for meeting ListView
 *
 * @author Xin Shen, Shaocong Lang
 */
public class ScrolledMeetingAdapter extends ArrayAdapter {

    public ScrolledMeetingAdapter(Context context, int resource, List<MeetingModel> objects) {
        super(context, resource, objects);
    }

    // Init the meeting item with parameters
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MeetingModel meeting = (MeetingModel) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.scrolled_meetings_listview, null);

        ImageView icon = (ImageView) view.findViewById(R.id.scroll_meeting_list_icons);
        TextView name = (TextView) view.findViewById(R.id.scroll_courses_list_names);
        TextView description = (TextView) view.findViewById(R.id.scroll_courses_list_description);
        TextView room = (TextView) view.findViewById(R.id.scroll_courses_list_category);
        TextView venue = (TextView) view.findViewById(R.id.scroll_courses_list_price);

        Bitmap ic_bmp = BitmapFactory.decodeResource(getContext().getResources(), meeting.getIcon());
        ic_bmp = getCircleBitmapByShader(ic_bmp, ic_bmp.getWidth(), ic_bmp.getHeight(), 0);
        icon.setImageBitmap(ic_bmp);
        name.setText(meeting.getName());
        description.setText(meeting.getDescription());
        room.setText(meeting.getRoom());
        venue.setText(meeting.getVenue());

        return view;
    }

    // Crop the meeting icon to have a round corner
    public static Bitmap getCircleBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, float boarder) {
        int radius;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (outHeight > outWidth) {
            radius = (int) (outWidth * 0.75);
        } else {
            radius = (int) (outWidth * 0.75);
        }
        // create canvas
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, paint);
        if (boarder > 0) {
            //paint boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, boarderPaint);
        }
        return desBitmap;
    }
}

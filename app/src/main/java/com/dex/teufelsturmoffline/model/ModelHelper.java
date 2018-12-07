package com.dex.teufelsturmoffline.model;

import android.graphics.drawable.Drawable;

import com.dex.teufelsturmoffline.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelHelper {

    public static boolean dates(String before, String after){
        Date date_1 = convertDate(before);
        Date date_2 = convertDate(after);
        return date_1.compareTo(date_2) < 0;
    }

    public static Date convertDate(String stringDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static int convertRatingToImage(int rating ){
        switch (rating){
            case -3: return R.drawable.b3_v;
            case -2: return R.drawable.b2_v;
            case -1: return R.drawable.b1_v;
            case 0: return R.drawable.n_v;
            case 1: return R.drawable.s1_v;
            case 2: return R.drawable.s2_v;
            case 3: return R.drawable.s3_v;
            default: return R.drawable.n_v;
        }
    }
}

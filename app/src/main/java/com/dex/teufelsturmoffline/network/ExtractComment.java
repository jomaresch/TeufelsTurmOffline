package com.dex.teufelsturmoffline.network;

import android.widget.ArrayAdapter;

import com.dex.teufelsturmoffline.model.Comment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractComment {
    public static List<Comment> run(String routeId) throws IOException {
        Connection connect = Jsoup.connect("http://teufelsturm.de/wege/bewertungen/anzeige.php?wegnr=" + routeId);
        Document doc = connect.post();
        String name, comment, date = "";
        List commentList = new ArrayList<Comment>();
        int rating;
        Elements newsHeadlines = doc.select("table[bgcolor=#1A3C64] > tbody > tr > td > table > tbody > tr:has(td[bgcolor=#274C8C])");
        for (Element element : newsHeadlines) {

            name = element.child(0).child(0).child(0).text().trim();
            date = parseDate(element.child(0).child(1).child(1).text());
            comment = parseComment(element.child(1).child(0).text());
            rating = parseRating(element.child(2).child(0).text());

            Comment commentObj = new Comment(name,comment, date,routeId,rating);
            commentList.add(commentObj);
        }
        return commentList;
    }


    private static String parseComment(String text){
        return text.trim();
    }

    private static int parseRating(String rating){
        if(rating.contains("(Kamikaze)"))
            return -3;
        if(rating.contains("(sehr schlecht)"))
            return -2;
        if (rating.contains("(schlecht)"))
            return -1;
        if(rating.contains("(Normal)"))
            return 0;
        if(rating.contains("(gut)"))
            return 1;
        if (rating.contains("(sehr gut)"))
            return 2;
        if (rating.contains("(Herausragend)"))
            return 3;
        return 0;
    }

    private static String parseDate(String line){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd");

        Pattern pattern = Pattern.compile("\\d\\d.\\d\\d.\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()){
            Calendar myCal = Calendar.getInstance();
            Date date = myCal.getTime();
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
            try {
                date = format.parse(matcher.group());
                return dt.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        pattern = Pattern.compile("Gestern");
        matcher = pattern.matcher(line);
        if (matcher.find()){
            Calendar myCal = Calendar.getInstance();
            myCal.add(Calendar.DATE, -1);
            Date date = myCal.getTime();
            return dt.format(date);
        }
        pattern = Pattern.compile("Heute");
        matcher = pattern.matcher(line);
        if (matcher.find()){
            Calendar myCal = Calendar.getInstance();
            Date date = myCal.getTime();
            return dt.format(date);
        }
        return "";
    }
}

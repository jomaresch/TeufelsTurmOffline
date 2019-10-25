package com.dex.teufelsturmoffline.network;

import com.dex.teufelsturmoffline.model.Route;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractRoute {

    final static String URL = "http://teufelsturm.de/wege/suche.php";

    public static List<Route> run(int count) throws IOException, InterruptedException {

        List routeList = new ArrayList<Route>();

        Document doc = getDocument(count);
        String name, id, mountain,  scale, area, date  = "";
        int rating;
        Elements newsHeadlines = doc.select("table[bgcolor=#1A3C64] > tbody > tr > td > div > table > tbody > tr:has(td[bgcolor=#376CAC])");

        for (Element element : newsHeadlines) {
            mountain = element.child(1).child(0).text().trim();
            name = element.child(2).child(0).text().trim();
            id = parseRouteUrl(element.child(2).child(0).child(0).attr("href"));
            rating = parseRating(element.child(3).child(0).child(0).attr("src"));
            scale = parseScale(element.child(4).child(0).text());
            area = element.child(5).child(0).text();
            date = parseDate(element.child(6).child(0).text());
            Route route = new Route(name,id,mountain,scale,area,date, rating,0,0);
            routeList.add(route);
        }
        return routeList;
    }

    private static String parseRouteUrl(String url){
        return url.replace("/wege/bewertungen/anzeige.php?wegnr=","").trim();
    }

    private static int parseRating(String rating){
        switch (rating){
            case "/img/symbole/arrow-downright3.gif":
                return -3;
            case "/img/symbole/arrow-downright2.gif":
                return -2;
            case "/img/symbole/arrow-downright.gif":
                return -1;
            case "/img/symbole/arrow-right.gif":
                return 0;
            case "/img/symbole/arrow-upright.gif":
                return 1;
            case "/img/symbole/arrow-upright2.gif":
                return 2;
            case "/img/symbole/arrow-upright3.gif":
                return 3;

        }
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

    private static String parseScale(String line){
        return line.trim();
    }

    private static Document getDocument(int length) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(100000);
        conn.setConnectTimeout(150000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String query =
                "gebiet=0&text=&skala_von=&skala_bis=&datum=1&sortiert=1&benutzer=&bewertungen=&avgbewertung=&gipfelnr=&volltext=&anzahl="+
                String.valueOf(length)
                +"&abschicken=anzeigen";

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        InputStream in = new BufferedInputStream(conn.getInputStream());
        return Jsoup.parse(in,"ISO-8859-1", URL );
    }
}

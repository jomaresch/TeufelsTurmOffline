package com.dex.teufelsturmoffline.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.activities.SettingsActivity;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.Comment;
import com.dex.teufelsturmoffline.model.ModelHelper;
import com.dex.teufelsturmoffline.model.ProcessUpdate;
import com.dex.teufelsturmoffline.model.Route;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpTask extends AsyncTask<Integer, ProcessUpdate, String> {

    Context context;
    Activity activity;
    TextView head,sub;
    ProgressBar progressBar;
    Button cancelButton, runButton;
    DatabaseHelper db;
    int counter;
    int newCommentsAmount;
    int ROUTE_CRAWL_AMOUNT = 0;

    public HttpTask(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.progressBar = activity.findViewById(R.id.progressBar_settings);
        this.cancelButton = activity.findViewById(R.id.button_settings_cancel);
        this.runButton = activity.findViewById(R.id.button_settings_run);
        this.head = activity.findViewById(R.id.text_settings_head);
        this.sub = activity.findViewById(R.id.text_settings_subline);
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        sub.setVisibility(View.VISIBLE);
        cancelButton.setEnabled(true);
        runButton.setEnabled(false);
    }

    @Override
    public String doInBackground(Integer... amountNumber) {
        ROUTE_CRAWL_AMOUNT = amountNumber[0];
        db = new DatabaseHelper(this.context);
        try {
            List<Route> routeList = ExtractRoute.run(ROUTE_CRAWL_AMOUNT);

            List<Route> updateRouteList = new ArrayList<>();
            List<Route> newRouteList = new ArrayList<>();

            for(Route entry : routeList){
                Route dbRoute = db.getRouteById(entry.getId());
                if(ModelHelper.dates(dbRoute.getDate(), entry.getDate()) && dbRoute.getName().equals("")){
                    newRouteList.add(entry);
                }
                if(ModelHelper.dates(dbRoute.getDate(), entry.getDate()) && !dbRoute.getName().equals("")){
                    updateRouteList.add(entry);
                }
            }

            newCommentsAmount = newRouteList.size() + updateRouteList.size() - 1;
            startProgress(newCommentsAmount);

            counter = 0;

            for(Route entry : newRouteList){
                db.addRoute(entry);
                saveComment(entry);
                if(isCancelled()) return "Das Update wurde abgebrochen";
            }

            for(Route entry : updateRouteList){
                db.updateRoute(entry);
                saveComment(entry);
                if(isCancelled()) return "Das Update wurde abgebrochen";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Updates wurden erfolgreich geladen";
    }

    @Override
    protected void onCancelled(String s) {
        progressBar.setVisibility(View.INVISIBLE);
        sub.setVisibility(View.INVISIBLE);
        head.setText("");
        cancelButton.setEnabled(false);
        runButton.setEnabled(true);
        dialog(s);
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.INVISIBLE);
        sub.setVisibility(View.INVISIBLE);
        head.setText("");
        cancelButton.setEnabled(false);
        runButton.setEnabled(true);
        dialog(s);
    }

    protected void onProgressUpdate(ProcessUpdate... progress){
        if(progress.length > 0){
            ProcessUpdate p = progress[0];
            if(p.firstUpdate){
                this.head.setText(p.head);
                this.progressBar.setMax(p.progressMax);
            } else {
                this.sub.setText(p.sub);
                this.progressBar.setProgress(p.progress);
            }
        }
    }

    private void dialog(String s){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("INFO");
        alert.setMessage(s);
        alert.setPositiveButton("OK",null);
        alert.show();
    }

    private boolean checkConnection() {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnectedOrConnecting ()) {
            return true;
        } else return false;
    }

    private void newProgress(Route entry){
        publishProgress(new ProcessUpdate(
                false,
                "",
                "Lade Route ("+
                        String.valueOf(counter)+
                        "/"+
                        String.valueOf(newCommentsAmount+1)+
                        "):\n" +
                        entry.getName() +
                        ", " +
                        entry.getMountain(),
                counter,
                0));
    }

    private void saveComment(Route entry) throws IOException, InterruptedException {
        List<Comment> commentList = ExtractComment.run(entry.getId());
        newProgress(entry);
        for(Comment newComment: commentList){
            Comment dbComment = db.getCommentByNameAndDate(
                    newComment.getName(),
                    newComment.getDate(),
                    newComment.getRouteId());
            if(dbComment.getName().equals("")){
                db.addComment(newComment);
            }
        }
        counter++;
        TimeUnit.SECONDS.sleep(0);
    }

    private void startProgress(int amount){
        publishProgress(new ProcessUpdate(
                true,
                "Es wurden " + String.valueOf(amount+1) + " neue Updates gefunden",
                "",
                0,
                amount));
    }

}
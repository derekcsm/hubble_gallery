package com.derek_s.spacegallery.api;

import android.os.AsyncTask;
import android.util.Log;

import com.derek_s.spacegallery.model.DetailsObject;
import com.derek_s.spacegallery.model.TileObject;
import com.derek_s.spacegallery.ui.fragments.FragMain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dereksmith on 15-03-15.
 */
public class GetDetails extends AsyncTask<Void, Void, DetailsObject> {

    private static String TAG = "GetDetails";
    private OnTaskComplete onTaskComplete;
    String newsUrl;
    String href;

    public GetDetails(String href) {
        this.href = href;
        Log.i(TAG, "href: " + href);
    }

    public interface OnTaskComplete {
        public void setTaskComplete(DetailsObject result, String newsUrl);
    }

    public void setGetDetailsCompleteListener(OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DetailsObject doInBackground(Void... arg0) {
        DetailsObject detailsObject = new DetailsObject();
        try {
            Document doc = null;
            Element infoHolder = null;

            if (!href.contains("newscenter")) {
                // get the newscenter url
                doc = Jsoup.connect("http://hubblesite.org" + href).timeout(8*1000).get();
                infoHolder = doc.getElementsByClass("info-holder").first();
            }

            if (infoHolder != null) {
                newsUrl = infoHolder.attr("href");
            } else {
                // probably from a redirect
                newsUrl = href;
            }
            Log.i(TAG, "newsUrl " + newsUrl);

            // with the new url then get the info
            doc = Jsoup.connect("http://hubblesite.org" + newsUrl).get();

            Elements p = doc.getElementsByTag("p");
            detailsObject.setDescription(p.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailsObject;
    }

    @Override
    protected void onPostExecute(DetailsObject result) {
        onTaskComplete.setTaskComplete(result, newsUrl);
    }


}
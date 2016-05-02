package com.derek_s.hubble_gallery.api;

import android.os.AsyncTask;
import android.util.Log;

import com.derek_s.hubble_gallery._shared.model.DetailsObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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
    void setTaskComplete(DetailsObject result, String newsUrl);
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
      Document doc;
      Element infoHolder = null;

      if (!href.contains("newscenter")) {
        // get the newscenter url
        doc = Jsoup.connect("http://hubblesite.org" + href).timeout(8 * 1000).get();
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
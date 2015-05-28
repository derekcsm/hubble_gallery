package com.derek_s.hubble_gallery.api;

import android.os.AsyncTask;

import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.ui.fragments.FragMain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dereksmith on 15-03-01.
 */
public class GetAlbum extends AsyncTask<Void, Void, ArrayList<TileObject>> {

    private OnTaskComplete onTaskComplete;
    int limit;
    int page;
    String query;
    boolean hiRes = false;

    public GetAlbum(int limit, int page, String query, boolean hiRes) {
        this.limit = limit;
        this.page = page;
        this.query = query;
        this.hiRes = hiRes;
        FragMain.currentPage = page;
    }

    public interface OnTaskComplete {
        void setTaskComplete(ArrayList<TileObject> result);
    }

    public void setGetAlbumCompleteListener(OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<TileObject> doInBackground(Void... arg0) {
        ArrayList<TileObject> mArray = new ArrayList<>();
        try {
            Document doc = null;

            if (hiRes) {
                doc = Jsoup.connect("http://hubblesite.org/gallery/album/" + query + "/npp/" + limit + "/hires/true/" + "+" + page).get();
            } else {
                doc = Jsoup.connect("http://hubblesite.org/gallery/album/" + query + "/npp/" + limit + "/" + "+" + page).get();
            }
            Elements links = doc.select("div#ListBlock");
            for (int i = 0; i < limit; i++) {
                try {
                Element link = links.select("a").get(i);
                Element img = link.select("img").first();

                //Log.i("apod", "src: " + img.attr("src"));

                TileObject t = new TileObject();
                t.setId(link.id());
                t.setTitle(link.attr("title"));
                t.setHref(link.attr("href"));

                // uses higher res image for thumbnails
                String src = img.attr("src");
                    if (src.contains(".gif"))
                        src = src.replace(".gif", ".jpg");
                src = src.replace("-thumb", "-web");

                t.setSrc(src);

                mArray.add(t);
                } catch (IndexOutOfBoundsException e) {
                    // stop the iteration
                    i = limit;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mArray;
    }

    @Override
    protected void onPostExecute(ArrayList<TileObject> result) {
        onTaskComplete.setTaskComplete(result);
    }


}
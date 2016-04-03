package com.huongtt.flickrdemo.download;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.huongtt.flickrdemo.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 03/04/2016.
 */
public class DownloadRecentsTask extends AsyncTask<String,Integer,Integer> {

    private String flickApiURL = "https://api.flickr.com/services/rest/";
    private String method = "flickr.photos.getRecent";
    private String api_key = "ba0d55d25fae2fd7ee94d6695278adf5";
    //private String api_key= "6159a012421730ff2c85960a2218f938";
    private String per_page = "5";
    private String format = "json";
    private String nojsoncallback = "1";
    private Context context;
    private List<Photo> listPhotos;


    public DownloadRecentsTask(Context context){
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        URL url;
        int status = 0;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String query = "?method="+method+"&api_key="+api_key+"&per_page="+per_page+"&format="+format+"&nojsoncallback="+nojsoncallback;
        try {
            url = new URL(flickApiURL+query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            status = connection.getResponseCode();
            String result = "";
            File recentPhotos = new File(context.getExternalFilesDir(null)+"/"+"recent.json");
            if(status==200){
                inputStream = new BufferedInputStream(connection.getInputStream());
                Reader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder stringBuilder = new StringBuilder();
                String stringReadLine = null;

                while ((stringReadLine = bufferedReader.readLine()) != null){
                    stringBuilder.append(stringReadLine);
                }

                result = stringBuilder.toString();

                listPhotos = new ArrayList<>();

                JSONObject all = new JSONObject(result);
                JSONObject photos = all.getJSONObject("photos");
                JSONArray photo = photos.getJSONArray("photo");

                for(int index =0 ; index<photo.length(); index ++){
                    Photo item = new Photo();
                    item.setId(photo.getJSONObject(index).getString("id"));
                    listPhotos.add(item);
                }

            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        }


        return status;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer == 200){

        }


    }
}

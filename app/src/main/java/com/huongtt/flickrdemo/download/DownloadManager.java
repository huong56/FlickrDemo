package com.huongtt.flickrdemo.download;

import android.os.AsyncTask;

import com.huongtt.flickrdemo.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class DownloadManager {

    private String flickApiURL = "https://api.flickr.com/services/rest/";
    private String api_key = "9f6e34f484297f22c46cb330d940c5f8";
    private String per_page = "15";
    private String format = "json";
    private String nojsoncallback = "1";
    private List<Photo> listPhotos;
    private String photosJSONDir;
    private String photosDir;
    private String localPath;

    public static DownloadManager instance;
    static{
        instance = new DownloadManager();
    }
    public static DownloadManager getInstance(){
        return instance;
    }


    public void init(String localPath){
        this.localPath = localPath;
        this.photosJSONDir = localPath+"/photosJSON/";
        this.photosDir = localPath+"/photos/";
        createFileDirectory();
    }


    private boolean createFileDirectory(){

        File jsonDirectory = new File(photosJSONDir);
        if(!jsonDirectory.exists()){
            jsonDirectory.mkdir();
        }

        File photosDirectory = new File(photosDir);
        if(!photosDirectory.exists()){
            photosDirectory.mkdir();
        }



        return true;
    }

    public void downloadRecentPhotosJSON(DownloadResponseHandler handler){

        if(handler != null){
            handler.onStart();
        }
        DownloadPhotosJSONTask downloadRecentPhotosTask = new DownloadPhotosJSONTask(handler);
        downloadRecentPhotosTask.execute();


    }

    public void downloadPhotoJSON(Photo photo, DownloadResponseHandler handler){

        if(handler != null){
            handler.onStart();
        }
        DownloadPhotoJSONTask downloadPhotoTask = new DownloadPhotoJSONTask(photo, handler);
        downloadPhotoTask.execute();


    }

    public void downloadPhotoSource(Photo photo, DownloadResponseHandler handler){

        if(handler != null){
            handler.onStart();
        }
        DownloadPhotoSourceTask downloadPhotoSourceTask = new DownloadPhotoSourceTask(photo, handler);
        downloadPhotoSourceTask.execute();


    }

    private class DownloadPhotosJSONTask extends AsyncTask<String,Integer,Integer> {
        private DownloadResponseHandler handler;
        private String method = "flickr.photos.getRecent";

        public DownloadPhotosJSONTask(DownloadResponseHandler handler){
            this.handler = handler;
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
                    connection.setRequestProperty("Accept-Charset","UTF-8");
                    connection.setDoInput(true);
                    status = connection.getResponseCode();
                    String result = "";
                    File recentPhotos = new File(localPath+"/"+"photos.json");
                    if(!recentPhotos.exists())
                        recentPhotos.createNewFile();
                    if(status==200){
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        outputStream = new FileOutputStream(recentPhotos);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);

                        }
                        setListPhoto(recentPhotos);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(inputStream!=null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(outputStream!=null){
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }


                return status;
            }
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer == 200){
                    for(Photo photo : listPhotos){
                        downloadPhotoJSON(photo, new DownloadResponseHandler());
                    }
                }


            }
    }

    private void setListPhoto(File file){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String stringReadLine = null;

            while ((stringReadLine = bufferedReader.readLine()) != null){
                stringBuilder.append(stringReadLine);
            }

            String result = stringBuilder.toString();

            listPhotos = new ArrayList<>();

            JSONObject all = new JSONObject(result);
            JSONObject photos = all.getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");

            for(int index =0 ; index<photo.length(); index ++){
                Photo item = new Photo();
                JSONObject photoJSON = photo.getJSONObject(index);
                item.setId(photoJSON.getString("id"));
                item.setFarm(photoJSON.getString("farm"));
                item.setServer(photoJSON.getString("server"));
                item.setSecret(photoJSON.getString("secret"));
                listPhotos.add(item);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void setPhotoFormat(File file, Photo photo){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String stringReadLine = null;

            while ((stringReadLine = bufferedReader.readLine()) != null){
                stringBuilder.append(stringReadLine);
            }

            String result = stringBuilder.toString();

            JSONObject resultObject = new JSONObject(result);
            JSONObject  photoJSON = resultObject.getJSONObject("photo");
            photo.setOriginalformat(photoJSON.optString("originalformat"));

            JSONObject urls = photoJSON.getJSONObject("urls");
            JSONArray urlArray = urls.getJSONArray("url");
            for(int index =0 ; index<urlArray.length(); index ++){
                JSONObject object = urlArray.getJSONObject(index);
                photo.setUrl(object.getString("_content"));


            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DownloadPhotoJSONTask extends AsyncTask<String, Integer, Integer>{

        private String method = "flickr.photos.getInfo";
        private DownloadResponseHandler handler;
        private String photoID;
        private Photo photo;

        public DownloadPhotoJSONTask(Photo photo, DownloadResponseHandler handler){
            this.photoID = photo.getId();
            this.handler = handler;
            this.photo = photo;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            int status = 0;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String query = "?method="+method+"&api_key="+api_key+"&photo_id="+photoID+"&format="+format+"&nojsoncallback="+nojsoncallback;
            try {
                url = new URL(flickApiURL+query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Charset","UTF-8");
                connection.setDoInput(true);
                status = connection.getResponseCode();
                String result = "";
                File photoFile = new File(photosJSONDir+photoID+".json");
                if(status==200){
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    outputStream = new FileOutputStream(photoFile);
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);

                    }
                    setPhotoFormat(photoFile, photo);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(inputStream!=null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(outputStream!=null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 200){
                downloadPhotoSource(photo, new DownloadResponseHandler());
            }


        }
    }

    private class DownloadPhotoSourceTask extends AsyncTask<String, Integer, Integer>{

        private Photo photo;
        private DownloadResponseHandler handler;

        public DownloadPhotoSourceTask(Photo photo, DownloadResponseHandler handler){
            this.photo = photo;
            this.handler = handler;
        }


        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            int status = 0;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                url = new URL(photo.getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                status = connection.getResponseCode();
                String result = "";
                File photoFile = new File(photosDir+photo.getId()+".jpg");
                if(status==200){
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    outputStream = new FileOutputStream(photoFile);
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);

                    }
                    setPhotoFormat(photoFile, photo);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(inputStream!=null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(outputStream!=null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return status;
        }
    }





}

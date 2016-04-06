package com.huongtt.flickrdemo.download;

/**
 * Created by admin on 06/04/2016.
 */
public class DownloadResponseHandler {

    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart( ) {
    }

    public void onProgress(Integer... values) {
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     * @param path path of the locale XML file
     */
    public void onSuccess(String path) {
        // Successfully got a response
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     */
    public void onFailure(Throwable error) {
        // Response failed :(
    }

}

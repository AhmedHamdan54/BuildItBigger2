package com.udacity.gradle.builditbigger.paid;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


class EndpointAsyncTask extends AsyncTask< com.udacity.gradle.builditbigger.paid.MainActivityFragment, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private  com.udacity.gradle.builditbigger.paid.MainActivityFragment mainActivityFragment;

    @Override
    protected String doInBackground(MainActivityFragment... params) {

        mainActivityFragment = params[0];
        context = mainActivityFragment.getActivity();
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new
                    MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // ­ 10.0.2.2 is localhost's IP address in Android emulator
                    // ­ turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override                         public void initialize(AbstractGoogleClientRequest<?>
                                                                                         abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }


        try {
            return myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }

    }


    @Override
    protected void onPostExecute(String result) {

        mainActivityFragment.loadedJoke = result;
        mainActivityFragment.launchDisplayJokeActivity();
    }
}
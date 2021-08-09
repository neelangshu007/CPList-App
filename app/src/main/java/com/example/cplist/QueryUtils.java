package com.example.cplist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){

    }

    private static URL createUrl(String StringUrl){
        URL url = null;
        try{
            url = new URL(StringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Contest> extractContestFromJson(String contestJSON){
        if(TextUtils.isEmpty(contestJSON)){
            return null;
        }
        List<Contest> contest = new ArrayList<>();

        try{
            JSONArray contestArray = new JSONArray(contestJSON);

            for(int i = 0; i < contestArray.length(); i++){
                JSONObject currentContest = contestArray.getJSONObject(i);

                String eventName = currentContest.getString("name");

                String eventStarTime = currentContest.getString("start_time");

                String eventEndTime = currentContest.getString("end_time");

                String eventDuration = currentContest.getString("duration");

                String eventUrl = currentContest.getString("url");

                String eventIn24Hours = currentContest.getString("in_24_hours");

                Contest ContestObj = new Contest(eventName, eventStarTime, eventEndTime, eventDuration, eventUrl, eventIn24Hours);

                contest.add(ContestObj);
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the Contest JSON results", e);
        }

        return contest;
    }

    public static List<Contest> fetchContestData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
            Log.e(LOG_TAG, "NEEL JSON RESPONSE: "+jsonResponse);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Contest> contest = extractContestFromJson(jsonResponse);
        return contest;
    }


}

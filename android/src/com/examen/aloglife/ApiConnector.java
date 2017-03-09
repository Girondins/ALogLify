package com.examen.aloglife;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Girondins on 30/01/17.
 */
public class ApiConnector {
    private ExecuteThread thread;
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    String TAG = ((Object) this).getClass().getSimpleName();
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private Long refresh_expires;
    private String headerValue;
    private volatile boolean hasHeader = false;
    private Controller cont;
    private volatile boolean isForUpload = true;
    private volatile boolean waitForCheck = true;
    private volatile boolean isRefresh = false;
    private final String CLIENT_ID ="daacd362-05d2-4d15-ab2c-ed07848469d4";
    private final String CLIENT_SECRET="PEHQvGvZ7ml0qP9sgKoXiDw_Vqw";
    private String authCode;

    private FETCH fetch;
    public static enum FETCH{
        UPDATE,LOGIN
    }



    public ApiConnector(String authCode, Controller cont, FETCH fetch){
        this.fetch = fetch;
        thread = new ExecuteThread();
        thread.start();
        this.authCode = authCode;
        this.cont = cont;
        Timer time = new Timer();
    }

    public ApiConnector(String authCode, String refToken, String headerValue, Controller cont, FETCH fetch){
        this.fetch = fetch;
        this.headerValue = headerValue;
        Log.d("HEADERVALUE ", headerValue);
        this.refreshToken = refToken;
        hasHeader = true;
        thread = new ExecuteThread();
        thread.start();
        this.authCode = authCode;
        this.cont = cont;
        Timer time = new Timer();
    }
    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    private String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    private String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 2000;
          //  HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 2000;
         //   HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params

                if(hasHeader == true){
                    httpPost.setHeader("Authorization", headerValue);
                    hasHeader = false;
                    Log.d("Authen",headerValue);
                }
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);


            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                Log.e("Request: ", "> " + url);
                HttpGet httpGet = new HttpGet(url);

                if(hasHeader == true){
                    httpGet.setHeader("Authorization", headerValue);
                    hasHeader = false;
                    Log.d("Authen",headerValue);
                }

                httpResponse = httpClient.execute(httpGet);

            }
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            } else {
                Log.e(TAG, "httpResponse is null");
            }
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public void extractAuth(String access){
        try {
            accessToken = (String)((JSONObject) JSONValue.parse(access)).get("access_token");
            tokenType = (String) ((JSONObject) JSONValue.parse(access)).get("token_type");
            expiresIn = (Long)((JSONObject) JSONValue.parse(access)).get("expires_in");
            refreshToken = (String)((JSONObject) JSONValue.parse(access)).get("refresh_token");
            refresh_expires = (Long)((JSONObject) JSONValue.parse(access)).get("refresh_token_expires_in");
            Log.d("extracting","Access " + accessToken);
            Log.d("extracting","Type " + tokenType);
            Log.d("extracting","Expires " + expiresIn);
            Log.d("extracting","Refresh Token " + refreshToken);
            Log.d("extracting","Refresh Expires " + refresh_expires);
            headerValue = "Bearer " + accessToken;
            hasHeader = true;
            cont.setHeader(headerValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractRenew(String access){
        try {
            accessToken = (String)((JSONObject) JSONValue.parse(access)).get("access_token");
            tokenType = (String) ((JSONObject) JSONValue.parse(access)).get("token_type");
            expiresIn = (Long)((JSONObject) JSONValue.parse(access)).get("expires_in");
            refreshToken = (String)((JSONObject) JSONValue.parse(access)).get("refresh_token");
            refresh_expires = (Long)((JSONObject) JSONValue.parse(access)).get("refresh_token_expires_in");
            Log.d("extracting","Access " + accessToken);
            Log.d("extracting","Type " + tokenType);
            Log.d("extracting","Expires " + expiresIn);
            Log.d("extracting","Refresh Token " + refreshToken);
            Log.d("extracting","Refresh Expires " + refresh_expires);
            headerValue = "Bearer " + accessToken;
            hasHeader = true;
            cont.setRefreshToken(refreshToken);
         /**   if(fetch == FETCH.LOGIN && isForUpload == true) {
                getToday();
            }
          **/
            if(isForUpload == true){
                getToday();
            }

            if(isForUpload == false){
                isForUpload = true;
                renewToken();
                Log.d("GET TODAY", "IS FALSE");
            }
            Log.d(" For Update is" , isForUpload + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractPersonal(String personalInfo){
        String userName,birthday;
        Double height,weight,bmr;
        try {
            JSONArray extractResult = (JSONArray) ((JSONObject) JSONValue.parse(personalInfo)).get("result");
            JSONObject results = (JSONObject) extractResult.get(0);
            Log.d("TESTING ",results.toString());
            userName = (String) results.get("username");
            birthday = (String) results.get("birthday");
            height = (Double) results.get("height");
            weight = (Double) results.get("weight");
            bmr = (Double) results.get("bmr");

            Log.d("Personal Extract", "Username: " + userName +
                                    "\n Birthday: " + birthday +
                                    "\n Height: " + height +
                                    "\n Weight: " + weight +
                                    "\n Bmr: " + bmr);

            cont.setPersonalInfo(userName,height,weight,bmr,birthday);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractActivity(String activityInfo){
        int stepsCount = 0;
        Double aeeCount = 0.00;
        String type;


        try {
            JSONArray extractResult = (JSONArray) ((JSONObject) JSONValue.parse(activityInfo)).get("result");
            Log.d("TESTING ACTIVITY ",extractResult.size() + "");
            for(int i = 0; i<extractResult.size(); i++) {
                JSONObject results = (JSONObject) extractResult.get(i);
                type = (String) results.get("type");

                switch (type){
                    case "physical":
                        JSONObject details = (JSONObject) results.get("details");
                        Log.d("Details ", details.toString());
                        JSONArray steps = (JSONArray) details.get("steps");
                        JSONArray aee = (JSONArray) details.get("aee");
                        for(int j = 0 ; j<steps.size(); j++){
                            stepsCount += (Long) steps.get(j);
                        }
                        for(int y = 0; y<aee.size(); y++){
                            aeeCount += (Double) aee.get(y);
                        }
                        cont.setSteps(stepsCount);
                        cont.setAee(aeeCount);
                        cont.calcCalories();
                        break;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(this.isRefresh == true){
            renewToken();
            isRefresh = false;
        }

        Log.d("StepsTotal", stepsCount + "");
        if(this.fetch == FETCH.LOGIN){
            cont.enterLifeLog();
        }

        if(this.fetch == FETCH.UPDATE){
            cont.refreshComplete();
           // renewToken();
            Log.d("WTF STUCK AGAIN", "NOOO");
        }
    }

    public void extractYesterday(String yesterdayInfo){
        int stepsCount = 0;
        Double aeeCount = 0.00;
        String type;
        try {
            JSONArray extractResult = (JSONArray) ((JSONObject) JSONValue.parse(yesterdayInfo)).get("result");
            Log.d("TESTING ACTIVITY ",extractResult.size() + "");
            for(int i = 0; i<extractResult.size(); i++) {
                JSONObject results = (JSONObject) extractResult.get(i);
                type = (String) results.get("type");

                switch (type){
                    case "physical":
                        JSONObject details = (JSONObject) results.get("details");
                        Log.d("Details ", details.toString());
                        JSONArray steps = (JSONArray) details.get("steps");
                        JSONArray aee = (JSONArray) details.get("aee");
                        for(int j = 0 ; j<steps.size(); j++){
                            stepsCount += (Long) steps.get(j);
                        }
                        for(int y = 0; y<aee.size(); y++){
                            aeeCount += (Double) aee.get(y);
                        }
                        break;
                }


            }

            cont.uploadYesterday(stepsCount,aeeCount);
       //     getToday();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void authorize(String accessCode){
        thread.execute(new Authenticate());
    }

    public void getYesterdayActivties(){
        thread.execute(new GetYesterdayActivties());
    }

    public void getPersonal()
    {
        thread.execute(new GetPersonalInfo());
    }

    public void getToday(){
        thread.execute(new GetTodaysActivites());
    }

    public void renewToken(){
        Log.d(" RENEW UPDATE", isForUpload + "");
        if(isForUpload == false){
            Thread t = new Thread(new RenewToken());
            t.start();
            Log.d(" RENEW UPDATE", isForUpload + "");
        }else {
            thread.execute(new RenewToken());
            Log.d(" FAAALSE", isForUpload + "");
        }
    }

    public void forRefresh(){
        isRefresh = true;
        getToday();
    }


    private class Authenticate implements Runnable {

        @Override
        public void run() {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("client_id",CLIENT_ID));
            nameValuePairs.add(new BasicNameValuePair("client_secret",CLIENT_SECRET));
            nameValuePairs.add(new BasicNameValuePair("grant_type","authorization_code"));
            nameValuePairs.add(new BasicNameValuePair("code",authCode));
            String res = makeServiceCall("https://platform.lifelog.sonymobile.com/oauth/2/token",2,nameValuePairs);
     //       Log.d("UserToken Success",res);
            extractAuth(res);
            getPersonal();
        }
    }

    private class RenewToken implements Runnable {

        @Override
        public void run() {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("client_id",CLIENT_ID));
            nameValuePairs.add(new BasicNameValuePair("client_secret",CLIENT_SECRET));
            nameValuePairs.add(new BasicNameValuePair("grant_type","refresh_token"));
            nameValuePairs.add(new BasicNameValuePair("refresh_token",refreshToken));

            Log.d("RE REFF", refreshToken + " HEADER  " + headerValue + " is Ture" + hasHeader);
            String res = makeServiceCall("https://platform.lifelog.sonymobile.com/oauth/2/refresh_token",2,nameValuePairs);
            Log.d("RefreshToken Success" + isForUpload,res);
            extractRenew(res);
        }
    }

    private class GetPersonalInfo implements Runnable {

        @Override
        public void run() {
            String res = makeServiceCall("https://platform.lifelog.sonymobile.com/v1/users/me/",1);
            Log.d("Me", res);
            extractPersonal(res);
            //  renewToken();
        }
    }

    private class GetYesterdayActivties implements Runnable {
        private String yesterday,today;

        public GetYesterdayActivties(){
            this.yesterday = cont.getYesterday();
            this.today = cont.getDate();
        }

        @Override
        public void run() {
            isForUpload = false;
            renewToken();
            while(isForUpload == false){
                try {
                    Thread.sleep(500);
                    Log.d("STUCK", "HELP");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String res = makeServiceCall("https://platform.lifelog.sonymobile.com/v1/users/me/activities?start_time="+ this.yesterday +"T00:00:01.000Z&end_time="+ this.today +"T00:00:01.000Z",1);
            Log.d("Act", res);
            Log.d("EXtract Yesteday", " JEPP");
            extractYesterday(res);
          //  renewToken();
        }
    }

    private class GetTodaysActivites implements Runnable {
        private String today;

        public GetTodaysActivites(){
            today = cont.getDate();
        }

        @Override
        public void run() {
            String res = makeServiceCall("https://platform.lifelog.sonymobile.com/v1/users/me/activities?start_time="+ this.today +"T00:00:01.000Z",1);
            Log.d("Today" , res);
            extractActivity(res);
          //  renewToken();
        }
    }


}

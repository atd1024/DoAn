package com.example.bkzalo.WebService;

import android.util.Log;

import com.example.bkzalo.Model.Chat;
import com.example.bkzalo.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class WebService {

    //fields
    private static WebService instance = new WebService();

    //constructor
    private WebService(){}


    // methods
    public static WebService getInstance(){
        if(instance == null){
            instance = new WebService();
        }
        return instance;
    }

    // gửi data sang cho server xử lý và nhận lại hồi đáp
    public String Login(String[] values) {
        String jsonString="";
        try
        {

            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/CheckLoginServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("username", values[0]));
            list.add(new BasicNameValuePair("password",values[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);

        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String Register(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/RegisterServlet");


            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("username", values[0]));
            list.add(new BasicNameValuePair("password",values[1]));
            list.add(new BasicNameValuePair("displayname",values[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String GetAllUser() {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/getAllUserServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
//            list.add(new BasicNameValuePair("username", values[0]));
//            list.add(new BasicNameValuePair("password",values[1]));
//            list.add(new BasicNameValuePair("displayname",values[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();

        }
        return jsonString;
    }

    public String GetUserByID(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/getUserByIDServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("idUser", values[0]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String SetUserTableState(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/UserTableStateServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("state", values[0]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String GetUserTableState() {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/GetUserTableStateServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String SetChatTableState(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/ChatTableStateServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("state", values[0]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String GetChatTableState() {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/GetChatTableStateServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String SendMessage(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/SendMessageServlet");


            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("sender", values[0]));
            list.add(new BasicNameValuePair("receiver",values[1]));
            list.add(new BasicNameValuePair("message",values[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }

    public String GetAllChat(String[] values) {
        String jsonString="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            // ip Tuấn
            // HttpPost httpPost=new HttpPost("http://192.168.1.9:8080/MVCsample/CheckLoginServlet");

            HttpPost httpPost=new HttpPost("http://192.168.0.106:8080/WebService/getMessageServlet");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("sender", values[0]));
            list.add(new BasicNameValuePair("receiver",values[1]));
//            list.add(new BasicNameValuePair("displayname",values[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            jsonString = readResponse(httpResponse);
        }
        catch(Exception exception)  {
            exception.getCause();
            exception.printStackTrace();
            exception.toString();
            Log.e("myApp", exception.toString());
        }
        return jsonString;
    }


    // nhận data từ bên server trả về
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String jsonString="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            jsonString=sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return jsonString;
    }

    // hàm chuyển từ json string về object User
    public User parserUser(String jsonString) throws JSONException {

        User user;

        JSONObject jsonObject = new JSONObject(jsonString);

        String password = jsonObject.getString("password");
        String username = jsonObject.getString("username");
        int ID = jsonObject.getInt("ID");
        String displayname = jsonObject.getString("displayname");

        user = new User(ID,username,password,displayname, "offline", "default");
        return user;
    }

    // hàm chuyển từ json Object về object User
    public User parserUser(JSONObject jsonObject) throws JSONException {

        User user;

        String password = jsonObject.getString("password");
        String username = jsonObject.getString("username");
        int ID = jsonObject.getInt("ID");
        String displayname = jsonObject.getString("displayname");
        //String status = jsonObject.getString("status");
        //String imageURL = jsonObject.getString("imageURL");

        user = new User(ID,username,password,displayname, "status", "imageURL");

        return user;
    }

    // hàm chuyển từ json Object về object Chat
    public Chat parseChat(JSONObject jsonObject) throws JSONException {
        int sender = jsonObject.getInt("sender");
        int receiver = jsonObject.getInt("receiver");
        String message = jsonObject.getString("message");
        Chat chat = new Chat(sender, receiver, message);
        return chat;
    }
}

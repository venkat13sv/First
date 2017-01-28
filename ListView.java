package com.androidhub4you.savecontacts;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by venkat on 28-10-2016.
 */
public class FileApi extends ListActivity {
    Button b1;
    String s;
    StringBuilder responseStrBuilder;
    ListView myList;
    ArrayList<HashMap<String, String>> contactList;
    JSONArray jsonArray;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.fileshow);
       b1=(Button) findViewById(R.id.btshow);
        myList = (ListView) findViewById(android.R.id.list);
        contactList = new ArrayList<>();

            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    try {
                       // Toast.makeText(FileApi.this, jsonArray.getJSONObject(position).getString("fname"), Toast.LENGTH_SHORT).show();
                       String ss=jsonArray.getJSONObject(position).getString("fname");
                        Intent intent = new Intent(FileApi.this, FileReceive.class);
                        intent.putExtra("message",ss );
                        startActivity(intent);

                    }catch (Throwable t){}
                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileApiBack().execute();
                }
            });

    }


    public class FileApiBack extends AsyncTask<String, Void, String> {

        ProgressDialog pd = null;
        @Override

        protected void onPreExecute() {

            pd = ProgressDialog.show(FileApi.this, "Please wait", "Loading please wait..", false);

            pd.setCancelable(true);



        }



        @Override

        protected String doInBackground(String... params) {

            save();
            try {

                jsonArray = new JSONArray(responseStrBuilder.toString());

                s = jsonArray.getJSONObject(0).getString("fname");
                for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject phone=   jsonArray.getJSONObject(i);
                    String mobile = phone.getString("fname");
                    String home = phone.getString("fsize");
                    String office = phone.getString("ftime");
                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("name", home);
                    contact.put("email",office );
                    contact.put("mobile", mobile);
                    contactList.add(contact);
                }

            }catch(Throwable t){}


            return null;

        }



        @Override

        protected void onPostExecute(String result) {


            ListAdapter adapter = new SimpleAdapter(FileApi.this, contactList, R.layout.list_item, new String[]{"name", "email", "mobile"}, new int[]{R.id.ftime, R.id.fsize, R.id.fname});

            myList.setAdapter(adapter);







             pd.dismiss();
                //JSONObject jsonObj = new JSONObject(s);
              //  s=jsonObj.getString("name");
                Log.v("json","ok");

                Log.v("instance","ok");
              //  alert1.setTitle("Welcome");
               // alert1.setMessage(s);
                // pd= ProgressDialog.show(MainActivity.this, s,

                // jsonObj.getString("number"), true);
                // pd = ProgressDialog.show(MainActivity.this, "Please wait",

                //    "Loading please wait..", true);
                //alert1.show();
                Log.v("alert","ok");
                pd.setCancelable(true);


           // catch(final JSONException e){}



        }



    }

    public void save() {

        try {
            //OutputStream os = null;
            InputStream is = null;
            JSONObject json = new JSONObject();
           // String sn=ed1.getText().toString();
            //String sn1=ed2.getText().toString();
           json.put("name", "v");
          //  json.put("number", "v");
            String m=json.toString();
            HttpURLConnection con = (HttpURLConnection)new URL("http://192.168.43.172:80/wservices/fileshow.php").openConnection();
            con.setDoOutput(true);
            //con.setRequestMethod("POST");
            //con.setRequestProperty("Content-Type", "application/json");
            // con.setChunkedStreamingMode(0);
            con.setDoInput(true);
            //con.setDoOutput(true);
           // con.setFixedLengthStreamingMode(m.getBytes().length);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.connect();
           // os = new BufferedOutputStream(con.getOutputStream());
           // os.write(m.getBytes());
            //clean up
            //os.flush();





            /*//String url = "http://androidhub4you.com/saveJSONObject.php";
            StringBuilder output = new StringBuilder();
            //output.append("data=");
           // output.append(URLEncoder.encode(json.toString(), "UTF-8"));
            output.append(json.toString());

            BufferedOutputStream stream = new BufferedOutputStream(con.getOutputStream());
            stream.write(output.toString().getBytes());
            stream.flush();

            con.connect();
            Exception result = null;
            int responseCode = con.getResponseCode();
            switch(responseCode) {
                case 200: //all ok
                    break;
                case 401:
                case 403:
                    // authorized
                    break;
                default:
                    //whatever else...
                    String httpResponse = con.getResponseMessage();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            Log.d("error", "    " + line);
                        }
                    }
                    catch(Exception ex) {
                        //nothing to do here
                    }

                    break;
            }




            */
            try {is = con.getInputStream();
                // InputStream in = new BufferedInputStream(con.getInputStream());
                //readStream(in);
                // Log.v("<<server response>>", s );
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                //JSONObject js=new JSONObject();


                //JSONObject jo=new JSONObject();



                String s;
                //s=readIt(is ,500);
                Log.v("<<server response>>","hello" );
               // Toast.makeText(getBaseContext(),s , Toast.LENGTH_LONG).show();
            }
            finally
            {
                con.disconnect();
            }
            // If the response does not enclose an entity, there is no need



            // InputStream instream = entity.getContent();




        }
        catch(final JSONException e){}
        catch(Throwable t){}


    }
    // public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException
   /* public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException
    {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[500];
        reader.read(buffer);
        return new String(buffer);
    }*/









}





















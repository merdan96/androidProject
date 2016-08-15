package merdan.com.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieView extends ActionBarActivity {
    Context main=this;
    TextView title,year,overview;
    ImageView Poster;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieview);

        title=(TextView)findViewById(R.id.title);
        year=(TextView)findViewById(R.id.year);
        overview=(TextView)findViewById(R.id.overview);
        Poster=(ImageView)findViewById(R.id.poster);

        id=(int)getIntent().getExtras().get("ID");
        Thread getMovie=new Thread(get);
        getMovie.start();
    }
    Runnable get=new Runnable() {
        @Override
        public void run() {
            try {
                URL url=new URL("http://api.themoviedb.org/3/movie/"+id+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2");
                HttpURLConnection http=(HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");
                http.connect();
                if(http.getResponseCode()== HttpURLConnection.HTTP_OK){
                    BufferedReader buff=new BufferedReader(new InputStreamReader(http.getInputStream()));
                    String json="";
                    String temp="";
                    while(true){
                        temp = buff.readLine();
                        if (temp == null) break;
                        json += temp;
                    }
                    putData(json);
                }
            }
            catch(MalformedURLException e){e.printStackTrace();}
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void putData(final String json) throws JSONException {
        JSONObject movie=new JSONObject(json);
        final Movie movieObject=new Movie(movie);
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                Picasso.with(main).load("http://image.tmdb.org/t/p/w500"+movieObject.poster+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2").into(Poster);
                title.setText(movieObject.title);
                overview.setText(movieObject.description);
                year.setText(movieObject.year.split("-")[0]);
            }
        });
    }
}

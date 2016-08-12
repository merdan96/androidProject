package merdan.com.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Main extends AppCompatActivity {
    Activity main=this;
    EditText search;
    ListView searchList;
    ProgressBar progressBar;
    TextView textView;
    String movieSearched;
    String searchMovie="http://api.themoviedb.org/3/search/movie?api_key=c00867b825ec5a921bb3c3bf6dfad2b2&query=",
           getMovie="http://api.themoviedb.org/3/movie/",
           apiKey="?api_key=c00867b825ec5a921bb3c3bf6dfad2b2";
    List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        search=(EditText)findViewById(R.id.search);
        searchList=(ListView)findViewById(R.id.searchList);
        searchList.setOnItemClickListener(goToMovie);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        textView=(TextView)findViewById(R.id.textView);

        if(getIntent().getExtras()!=null){
            String searchBack=(String)getIntent().getExtras().get("query");
            movieSearched=searchBack;
            Thread toList=new Thread(searchForMovie);
            toList.start();}

    }
    Runnable searchForMovie=new Runnable() {
        @Override
        public void run() {
            try {
                URL url=new URL(searchMovie+movieSearched);
                HttpURLConnection http=(HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");
                runOnUiThread(preLoad);
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
    public void Search(View v){
        movieSearched=search.getText().toString().replace(" ","%20");
        if(!movieSearched.isEmpty()){
        Thread toList=new Thread(searchForMovie);
        toList.start();}
    }
    private void putData(final String json) throws JSONException {
        JSONObject page=new JSONObject(json);
        JSONArray results=page.getJSONArray("results");
        movies = new ArrayList<>();
        for(int i=0;i<results.length();i++){
            JSONObject o=results.getJSONObject(i);
            Movie movie=new Movie(o);
            movies.add(movie);
        }
        final MovieAdapter list=new MovieAdapter(movies,this);
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                progressBar.setVisibility(View.INVISIBLE);
                searchList.setAdapter(list);
                searchList.setVisibility(View.VISIBLE);
            }
        });
    }
    Runnable preLoad=new Runnable(){
        @Override
        public void run() {
            textView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    };
    AdapterView.OnItemClickListener goToMovie=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent movie =new Intent(main,MovieView.class);
            Movie goToMovie = movies.get(i);
            movie.putExtra("title", goToMovie.title);
            movie.putExtra("poster",goToMovie.poster);
            movie.putExtra("release",goToMovie.year);
            movie.putExtra("description",goToMovie.description);
            startActivity(movie);
        }
    };

}
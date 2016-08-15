package merdan.com.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchList extends AppCompatActivity {
    Activity main=this;
    EditText search;
    ListView searchList;
    ProgressBar progressBar;
    TextView textView;
    boolean finishFlag=true;
    String searchMovie="http://api.themoviedb.org/3/search/movie?api_key=c00867b825ec5a921bb3c3bf6dfad2b2&query=",
           others="http://api.themoviedb.org/3/movie/",
           apiKey="?api_key=c00867b825ec5a921bb3c3bf6dfad2b2";
    String Url="";
    List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        search=(EditText)findViewById(R.id.search);
        search.setOnEditorActionListener(Search);
        searchList=(ListView)findViewById(R.id.searchList);
        searchList.setOnItemClickListener(goToMovie);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        textView=(TextView)findViewById(R.id.textView);

        String searchBack=(String)getIntent().getExtras().get("query");
        Url=searchMovie+searchBack;
        Thread toList=new Thread(searchForMovie);
        toList.start();
    }

    Runnable searchForMovie=new Runnable() {
        @Override
        public void run() {
            try {
                URL url=new URL(Url);
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

    TextView.OnEditorActionListener Search=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_GO){
                String movieSearched=search.getText().toString().replace(" ","%20");
                if(!movieSearched.isEmpty() && finishFlag){
                    finishFlag=false;
                    Url=searchMovie+movieSearched;
                    Thread toList=new Thread(searchForMovie);
                    toList.start();
                }
            }
            return true;
        }
    };
    private void putData(final String json) throws JSONException {
        JSONObject page=new JSONObject(json);
        JSONArray results=page.getJSONArray("results");
        movies = new ArrayList<>();
        for(int i=0;i<results.length();i++){
            JSONObject o=results.getJSONObject(i);
            Movie movie=new Movie(o);
            movies.add(movie);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                progressBar.setVisibility(View.INVISIBLE);
                searchList.setAdapter(new MovieAdapter(movies, main,false));
                searchList.setVisibility(View.VISIBLE);
                finishFlag=true;
            }
        });
    }
    Runnable preLoad=new Runnable(){
        @Override
        public void run() {
            textView.setVisibility(View.INVISIBLE);
            searchList.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    };

    AdapterView.OnItemClickListener goToMovie=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent movie =new Intent(main,MovieView.class);
            movie.putExtra("ID", ((MovieAdapter.ViewHolder)view.getTag()).id);
            startActivity(movie);
        }
    };

}
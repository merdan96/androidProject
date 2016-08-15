package merdan.com.androidproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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


public class Home extends ActionBarActivity {
    final Context main=this;
    String movieSearched;
    EditText search;
    GridAdapter []others=new GridAdapter[3];
    String []type={"now_playing","popular","top_rated"};
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        search=(EditText)findViewById(R.id.search);
        others[0]=(GridAdapter)findViewById(R.id.now_playing);
        others[1]=(GridAdapter)findViewById(R.id.popular);
        others[2]=(GridAdapter)findViewById(R.id.top_rated);

        search.setSelected(false);
        search.setOnEditorActionListener(Search);

        Thread start=new Thread(loadOthers);
        start.start();
    }

    TextView.OnEditorActionListener Search=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_GO) {
                movieSearched = search.getText().toString().replace(" ", "%20");
                if (!movieSearched.isEmpty()){
                    Intent searchBack = new Intent(main, SearchList.class);
                    searchBack.putExtra("query", movieSearched);
                    startActivity(searchBack);
                }
            }
            return true;
        }
    };
    Runnable loadOthers=new Runnable() {
        @Override
        public void run() {
            try {
                URL url=new URL("http://api.themoviedb.org/3/movie/"+type[i]+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2");
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
        JSONObject page=new JSONObject(json);
        JSONArray results=page.getJSONArray("results");
        List<Movie> movies = new ArrayList<>();

        for(int i=0;i<5;i++){
            JSONObject o=results.getJSONObject(i);
            Movie movie=new Movie(o);
            movies.add(movie);
        }
        final MovieAdapter grid=new MovieAdapter(movies,(Activity)main,true);
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                others[i].setAdapter(grid);
                others[i].setExpanded(true);
                others[i].setOnItemClickListener(goToMovie);
                i++;
                if(i<3){
                    Thread next=new Thread(loadOthers);
                    next.start();
                }
            }
        });
    }
    AdapterView.OnItemClickListener goToMovie=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
            MovieAdapter.ViewHolder viewHolder=(MovieAdapter.ViewHolder)view.getTag();
            int h=viewHolder.id;
            Intent movie=new Intent(main,MovieView.class);
            movie.putExtra("ID",h);
            startActivity(movie);
        }
    };
}

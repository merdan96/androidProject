package merdan.com.androidproject;

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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


public class MovieView extends ActionBarActivity {
    String movieSearched;
    EditText search;
    TextView title,year,overview;
    ImageView Poster;
    String src;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieview);

        search=(EditText)findViewById(R.id.search);
        search.setSelected(false);

        title=(TextView)findViewById(R.id.title);
        year=(TextView)findViewById(R.id.year);
        overview=(TextView)findViewById(R.id.overview);
        Poster=(ImageView)findViewById(R.id.poster);

        title.setText((String)getIntent().getExtras().get("title"));
        year.setText("("+((String)getIntent().getExtras().get("release")).split("-")[0]+")");
        overview.setText((String)getIntent().getExtras().get("description"));

        overview.setMovementMethod(new ScrollingMovementMethod());

        src=(String)getIntent().getExtras().get("poster");
        Thread poster=new Thread(loadPoster);
        poster.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String query="";
        switch(item.getItemId()){
            case R.id.NP:
                query="now_playing";
                break;
            case R.id.P:
                query="popular";
                break;
            case R.id.TR:
                query="top_rated";
                break;
        }
        Intent searchBack=new Intent(this,Main.class);
        searchBack.putExtra("query",query);
        startActivity(searchBack);
        return super.onOptionsItemSelected(item);
    }
    public void Search(View v){
        movieSearched=search.getText().toString().replace(" ","%20");
        if(!movieSearched.isEmpty()){
            Intent searchBack=new Intent(this,Main.class);
            searchBack.putExtra("query", movieSearched);
            startActivity(searchBack);
            }
    }
    Runnable loadPoster=new Runnable() {
        @Override
        public void run() {
            try{
                URL url=new URL("http://image.tmdb.org/t/p/w500"+src+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");
                http.connect();
                if(http.getResponseCode()== HttpURLConnection.HTTP_OK){
                    final Bitmap poster=BitmapFactory.decodeStream(http.getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Poster.setImageBitmap(poster);
                        }
                    });
                }
            }
            catch(ProtocolException e){e.printStackTrace();}
            catch (IOException e){e.printStackTrace();}
        }
    };
}

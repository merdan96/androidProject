package merdan.com.androidproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class MovieAdapter extends BaseAdapter {
    List<Movie> movieList;
    Activity a;
    public MovieAdapter(List<Movie> list,Activity a){
        this.movieList=list;
        this.a=a;
    }
    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movieList.get(i).id;
    }

    @Override
    public View getView(int i,View view,ViewGroup viewGroup) {
        if(view==null){
            view=a.getLayoutInflater().inflate(R.layout.searchrow,null);
        }
        ((TextView)view.findViewById(R.id.title)).setText(movieList.get(i).title);
        ((TextView)view.findViewById(R.id.year)).setText(movieList.get(i).year.split("-")[0]);

        /*try{
            URL url=new URL("http://image.tmdb.org/t/p/w45"+movieList.get(i).poster+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            if(http.getResponseCode()== HttpURLConnection.HTTP_OK) {
                //Bitmap thumbnail= BitmapFactory.decodeStream((InputStream)http.getContent());
                //((ImageView)view.findViewById(R.id.poster)).setImageBitmap(thumbnail);
            }
        }
        catch(ProtocolException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}*/
        return view;
    }
}

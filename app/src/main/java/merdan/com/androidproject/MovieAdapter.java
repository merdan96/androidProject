package merdan.com.androidproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
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
        ViewHolder holder=new ViewHolder();
        String posterPath="";
        if(view==null){
            view=a.getLayoutInflater().inflate(R.layout.searchrow,null);

            holder.title=(TextView)view.findViewById(R.id.title);
            holder.year=(TextView)view.findViewById(R.id.year);
            holder.poster=(ImageView)view.findViewById(R.id.poster);

            posterPath = movieList.get(i).poster;

            view.setTag(holder);
        }
        else{holder=(ViewHolder)view.getTag();}
        
        new ImageLoader(holder.poster).execute(posterPath);
        holder.title.setText(movieList.get(i).title);
        holder.year.setText("(" + movieList.get(i).year.split("-")[0] + ")");
        return view;
    }
    static class ViewHolder{
        ImageView poster;
        TextView title,year;
    }
}

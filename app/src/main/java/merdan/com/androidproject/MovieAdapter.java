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

import com.squareup.picasso.Picasso;

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
    boolean grid;
    public MovieAdapter(List<Movie> list,Activity a,boolean grid){
        this.movieList=list;
        this.a=a;
        this.grid=grid;
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
        ViewHolder holder;
        String posterPath="";
        if(view==null){
            if(!grid) view=a.getLayoutInflater().inflate(R.layout.searchrow,null);
            else view=a.getLayoutInflater().inflate(R.layout.moviegrid,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }
        else{holder=(ViewHolder)view.getTag();}

        holder.id=movieList.get(i).id;
        holder.title.setText(movieList.get(i).title);

        if(!grid) holder.year.setText("(" + movieList.get(i).year.split("-")[0] + ")");

        Picasso.with(a).load("http://image.tmdb.org/t/p/w154"+movieList.get(i).poster+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2").into(holder.poster);

        return view;
    }
    static class ViewHolder{
        public ViewHolder(View v){
            title=(TextView)v.findViewById(R.id.title);
            year=(TextView)v.findViewById(R.id.year);
            poster=(ImageView)v.findViewById(R.id.poster);
        }
        int id;
        ImageView poster;
        TextView title,year;
    }
}

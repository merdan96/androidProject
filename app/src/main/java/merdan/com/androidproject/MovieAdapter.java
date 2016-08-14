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
        ViewHolder holder;
        String posterPath="";
        if(view==null){
            view=a.getLayoutInflater().inflate(R.layout.searchrow,null);
            holder=new ViewHolder(view);

            view.setTag(holder);
        }
        else{holder=(ViewHolder)view.getTag();}

        holder.title.setText(movieList.get(i).title);
        holder.year.setText("(" + movieList.get(i).year.split("-")[0] + ")");

        Picasso.with(a).load("http://image.tmdb.org/t/p/w154"+movieList.get(i).poster+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2").into(holder.poster);

        return view;
    }
    static class ViewHolder{
        public ViewHolder(View v){
            title=(TextView)v.findViewById(R.id.title);
            year=(TextView)v.findViewById(R.id.year);
            poster=(ImageView)v.findViewById(R.id.poster);
        }
        ImageView poster;
        TextView title,year;
    }
}

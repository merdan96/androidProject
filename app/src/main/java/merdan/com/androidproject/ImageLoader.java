package merdan.com.androidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> posterRef;
    public ImageLoader(ImageView poster) {
        posterRef = new WeakReference<ImageView>(poster);
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        return getImage(strings[0]);
    }
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
            ImageView imageView = posterRef.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
            }
        }
    }
    private Bitmap getImage(String src) {
        try{
            URL url=new URL("http://image.tmdb.org/t/p/w154"+src+"?api_key=c00867b825ec5a921bb3c3bf6dfad2b2");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            if(http.getResponseCode()== HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(http.getInputStream());

            }
        }
        catch(ProtocolException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        return null;
    }
}

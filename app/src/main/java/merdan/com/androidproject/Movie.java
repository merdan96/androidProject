package merdan.com.androidproject;


import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    public int id;
    public String title;
    public String poster;
    public String year;
    public int runtime;
    public String description;
    public Movie(JSONObject o) throws JSONException {
        id=o.getInt("id");
        title=o.getString("title");
        poster=o.getString("poster_path");
        year=o.getString("release_date");
        description=o.getString("overview");
    }

}

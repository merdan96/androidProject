package merdan.com.androidproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class GridAdapter extends GridView{
    boolean expanded=false;
    public GridAdapter(Context c){
        super(c);
    }
    public  GridAdapter(Context c,AttributeSet attrs){
        super(c,attrs);
    }
    public GridAdapter(Context c,AttributeSet attrs,int defStyle){
        super(c,attrs,defStyle);
    }
    public boolean isExpanded(){
        return expanded;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isExpanded()){
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);}
    }
    public void setExpanded(boolean expanded){
        this.expanded=expanded;
    }
}

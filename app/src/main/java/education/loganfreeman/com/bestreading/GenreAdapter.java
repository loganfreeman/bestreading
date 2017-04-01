package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shanhong on 3/30/17.
 */

class GenreAdapter extends BaseAdapter {
    private Context mContext;

    List<Novels.Genre> genres;

    LayoutInflater inflater;

    // Constructor
    public GenreAdapter(Context c, List<Novels.Genre> genres) {
        mContext = c;
        this.genres = genres;
        this.inflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return genres.size();
    }

    public Novels.Genre getItem(int position) {
        return genres.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.genre_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Novels.Genre currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData.getTitle());

        return convertView;
    }


    private class MyViewHolder {
        TextView tvTitle;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvTitle);

        }
    }
}
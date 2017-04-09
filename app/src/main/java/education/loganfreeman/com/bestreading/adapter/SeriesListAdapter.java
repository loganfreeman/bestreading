package education.loganfreeman.com.bestreading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lucasurbas.listitemview.ListItemView;
import com.squareup.picasso.Picasso;

import java.util.List;

import education.loganfreeman.com.bestreading.Novels;
import education.loganfreeman.com.bestreading.R;
import education.loganfreeman.com.bestreading.transform.CircularTransform;

/**
 * Created by scheng on 4/8/17.
 */

public class SeriesListAdapter extends BaseAdapter {

    Context context;

    List<Novels.Series> series;

    LayoutInflater inflater;

    public SeriesListAdapter(Context context, List<Novels.Series> series) {
        this.series = series;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setItems(List<Novels.Series> series){
        this.series = series;
    }

    @Override
    public int getCount() {
        return series.size();
    }

    @Override
    public Novels.Series getItem(int position) {
        return series.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        Novels.Series series = getItem(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.series_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView, series);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.listItemView.setTitle(series.getTitle());

        mViewHolder.listItemView.setSubtitle(series.getText());


        return convertView;
    }

    private class MyViewHolder {
        ListItemView listItemView;

        public MyViewHolder(View item, Novels.Series novel) {
            listItemView = (ListItemView) item.findViewById(R.id.list_item_view);
        }
    }
}

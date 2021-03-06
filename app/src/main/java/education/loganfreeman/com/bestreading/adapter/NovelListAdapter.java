package education.loganfreeman.com.bestreading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lucasurbas.listitemview.ListItemView;

import java.util.List;

import education.loganfreeman.com.bestreading.Novels;
import education.loganfreeman.com.bestreading.R;

import static education.loganfreeman.com.bestreading.Novels.Novel;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by scheng on 3/31/17.
 */

public class NovelListAdapter extends BaseAdapter {

    Context context;

    List<Novel> novels;

    LayoutInflater inflater;

    public NovelListAdapter(Context context, List<Novel> novels) {
        this.novels = novels;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setItems(List<Novel> novels){
        this.novels = novels;
    }

    @Override
    public int getCount() {
        return novels.size();
    }

    @Override
    public Novel getItem(int position) {
        return novels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        Novel novel = getItem(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.novel_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView, novel);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.listItemView.setTitle(novel.getTitle());

        mViewHolder.listItemView.setSubtitle(novel.getAuthor());

        return convertView;
    }

    private class MyViewHolder {
        ListItemView listItemView;

        public MyViewHolder(View item, Novel novel) {
            listItemView = (ListItemView) item.findViewById(R.id.list_item_view);
        }
    }
}

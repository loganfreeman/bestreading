package education.loganfreeman.com.bestreading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lucasurbas.listitemview.ListItemView;

import java.util.List;

import education.loganfreeman.com.bestreading.Novels;
import education.loganfreeman.com.bestreading.R;


/**
 * Created by scheng on 3/31/17.
 */

public class AuthorListAdapter extends BaseAdapter {
    Context context;

    List<Novels.Author> authors;

    LayoutInflater inflater;

    public AuthorListAdapter(Context context, List<Novels.Author> authors) {
        this.authors = authors;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return authors.size();
    }

    @Override
    public Novels.Author getItem(int position) {
        return authors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        Novels.Author Author = getItem(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.author_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView, Author);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.listItemView.setTitle(Author.getName());


        return convertView;
    }

    private class MyViewHolder {
        ListItemView listItemView;

        public MyViewHolder(View item, Novels.Author Author) {
            listItemView = (ListItemView) item.findViewById(R.id.list_item_view);
        }
    }
}

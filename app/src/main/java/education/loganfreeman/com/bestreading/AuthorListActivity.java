package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import education.loganfreeman.com.bestreading.adapter.AuthorListAdapter;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.utils.ToastUtil;

/**
 * Created by scheng on 3/31/17.
 */

public class AuthorListActivity extends BaseActivity {
    private static final String AUTHOR_LIST = "author_list";

    List<Novels.Author> authors;

    @BindView(R.id.author_listview)
    ListView listView;




    public static void start(Context context, List<Novels.Author> authors) {
        Intent intent = new Intent(context, AuthorListActivity.class);
        intent.putExtra(AUTHOR_LIST, Parcels.wrap(authors));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);
        ButterKnife.bind(this);
        authors = Parcels.unwrap(getIntent().getParcelableExtra(AUTHOR_LIST));
        initView();

    }

    private void initView() {
        safeSetTitle("Hot Authors");
        AuthorListAdapter adapter = new AuthorListAdapter(this, authors);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Novels.Author author = adapter.getItem(position);
                ToastUtil.showShort(author.getUrl());
            }
        });
    }
}

package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import education.loganfreeman.com.bestreading.adapter.AuthorListAdapter;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.utils.PLog;
import education.loganfreeman.com.bestreading.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    private void gotoAuthor(String url) {
        PLog.i("Go to: " + url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void initView() {
        safeSetTitle("Hot Authors");
        AuthorListAdapter adapter = new AuthorListAdapter(this, authors);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Novels.Author author = adapter.getItem(position);
                //ToastUtil.showShort(author.getUrl());
                Novels.getAuthorHomePageAsync(author.getName())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                gotoAuthor(s);
                            }
                        });

            }
        });
    }
}

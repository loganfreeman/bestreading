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
import education.loganfreeman.com.bestreading.adapter.NovelListAdapter;
import education.loganfreeman.com.bestreading.adapter.SeriesListAdapter;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.utils.PLog;
import education.loganfreeman.com.bestreading.utils.StringUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by scheng on 4/8/17.
 */

public class NovelSeriesActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String NOVEL_GENRE = "novel_genre";

    private static final String NOVEL_URL = "novel_url";
    private static final String NOVEL_TITLE = "novel_title";

    private String url;

    private String title;

    private Novels.Genre genre;

    private SeriesListAdapter adapter;

    @BindView(R.id.series_listview)
    ListView listView;

    public static void start(Context context, Novels.Genre genre, String url, String title) {
        Intent intent = new Intent(context, NovelSeriesActivity.class);
        intent.putExtra(NOVEL_GENRE, Parcels.wrap(genre));
        intent.putExtra(NOVEL_URL, url);
        intent.putExtra(NOVEL_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_series);
        ButterKnife.bind(this);
        genre = Parcels.unwrap(getIntent().getParcelableExtra(NOVEL_GENRE));
        url = getIntent().getStringExtra(NOVEL_URL);
        title = getIntent().getStringExtra(NOVEL_TITLE);
        PLog.i("Url " + url);
        initView();

        listView.setOnItemClickListener(this);



    }

    private String getSafeTitle() {
        String[] parts = url.split("/");
        String last = parts[parts.length-1];
        parts = last.split("\\.");
        last = parts[0];
        return StringUtil.splitCamelCase(last);
    }

    private void initView() {
        safeSetTitle(getSafeTitle());
        Novels.getSeriesAsync(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Novels.Series>>() {
                    @Override
                    public void accept(List<Novels.Series> series) throws Exception {
                        adapter = new SeriesListAdapter(NovelSeriesActivity.this, series);
                        listView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Novels.Series series = adapter.getItem(position);
        String url = Novels.URL + series.getBookUrl();
        NovelDetailActivity.start(NovelSeriesActivity.this, genre, url);
    }
}

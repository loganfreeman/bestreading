package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.utils.PLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by scheng on 4/1/17.
 */

public class NovelDetailActivity extends BaseActivity {

    private static final String NOVEL_GENRE = "novel_genre";

    private static final String NOVEL_URL = "novel_url";

    private String url;

    private Novels.Genre genre;

    private Novels.Article mArticle;

    @BindView(R.id.novel_header)
    TextView header;

    @BindView(R.id.novel_textview)
    TextView text;

    public static void start(Context context, Novels.Genre genre, String url) {
        Intent intent = new Intent(context, NovelDetailActivity.class);
        intent.putExtra(NOVEL_GENRE, Parcels.wrap(genre));
        intent.putExtra(NOVEL_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);
        ButterKnife.bind(this);
        genre = Parcels.unwrap(getIntent().getParcelableExtra(NOVEL_GENRE));
        url = getIntent().getStringExtra(NOVEL_URL);
        PLog.i("Url " + url);
        initView();

    }

    private void initView() {
        Novels.getArticleAsync(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        mArticle = article;
                        header.setText(mArticle.getH1());
                        text.setText(Html.fromHtml(mArticle.getHtml()));
                    }
                });
    }
}

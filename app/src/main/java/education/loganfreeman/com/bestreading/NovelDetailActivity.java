package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import org.parceler.Parcels;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.speech.Speaker;
import education.loganfreeman.com.bestreading.utils.PLog;
import education.loganfreeman.com.bestreading.utils.StringUtil;
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
    HtmlTextView htmlTextView;

    @BindView(R.id.action_play)
    Button playBtn;

    @BindView(R.id.action_pause)
    Button pauseBtn;

    @BindView(R.id.btn_first)
    Button firstBtn;

    @BindView(R.id.btn_next)
    Button nextBtn;

    @BindView(R.id.btn_previous)
    Button previousBtn;

    @BindView(R.id.btn_last)
    Button lastBtn;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Speaker speaker;

    String lines[];


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
        checkTTS();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_novel_detail, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.next:
                gotoNext();
                break;
            case R.id.previous:
                gotoPrevious();
                break;
            case R.id.first:
                gotoFirst();
                break;
            case R.id.last:
                gotoLast();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String newUrl(String path) {
        if(path == null) return url;
        if(url.endsWith(".html")) {
            return StringUtil.replace(url, path);
        }
        return url + path;
    }

    private void setArticle(Novels.Article article) {
        mArticle = article;
        header.setText(mArticle.getH1());
        htmlTextView.setHtml(mArticle.getHtml());
    }

    @OnClick(R.id.btn_next)
    public  void gotoNext() {
        Novels.getNavAsync(url)
                .flatMap((nav) -> {
                    url = newUrl(nav.next);
                    return Novels.getArticleAsync(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        setArticle(article);
                    }
                });
    }

    @OnClick(R.id.btn_previous)
    public void gotoPrevious() {
        Novels.getNavAsync(url)
                .flatMap((nav) -> {
                    url = newUrl(nav.previous);
                    return Novels.getArticleAsync(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        setArticle(article);
                    }
                });
    }

    @OnClick(R.id.btn_first)
    public void gotoFirst() {
        Novels.getNavAsync(url)
                .flatMap((nav) -> {
                    url = newUrl(nav.first);
                    return Novels.getArticleAsync(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        setArticle(article);
                    }
                });
    }

    @OnClick(R.id.btn_last)
    public void gotoLast() {
        Novels.getNavAsync(url)
                .flatMap((nav) -> {
                    url = newUrl(nav.last);
                    return Novels.getArticleAsync(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        setArticle(article);
                    }
                });
    }
    @Override
    public void onPause(){
        if(speaker != null) {
            speaker.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(speaker != null) {
            speaker.destroy();
        }
        super.onDestroy();
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    private void scrollTo(int line) {
        htmlTextView.setHtml(mArticle.getHighlightedHtml(line+1));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int y = htmlTextView.getLayout().getLineTop(line);
                scrollView.scrollTo(0, y);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
                speaker.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {

                    }

                    @Override
                    public void onDone(String utteranceId) {
                        int line = Integer.valueOf(utteranceId.replace("line", ""));
                        // PLog.i(lines[line]);

                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });
                playBtn.setEnabled(true);
                pauseBtn.setEnabled(true);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @OnClick(R.id.action_play)
    public void play(View view) {
        String toSpeak = htmlTextView.getText().toString();
        //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        lines = toSpeak.split("\\r?\\n");
        int id = 1;
        for(String line : lines) {
            speaker.speak(line, "line" + id++);
        }
    }

    @OnClick(R.id.action_pause)
    public void pause(View view) {
        speaker.pause(LONG_DURATION);
    }

    private void initView() {
        safeSetTitle(genre.getTitle());
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(false);

        Novels.getArticleAsync(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Novels.Article>() {
                    @Override
                    public void accept(Novels.Article article) throws Exception {
                        setArticle(article);
                    }
                });
    }
}

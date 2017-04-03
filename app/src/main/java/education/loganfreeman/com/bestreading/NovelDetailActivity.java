package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.speech.Speaker;
import education.loganfreeman.com.bestreading.utils.PLog;
import education.loganfreeman.com.bestreading.utils.ToastUtil;
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

    @BindView(R.id.action_play)
    FloatingActionButton playBtn;

    @BindView(R.id.action_pause)
    FloatingActionButton pauseBtn;

    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Speaker speaker;


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

    private void initView() {
        safeSetTitle(genre.getTitle());
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(false);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = text.getText().toString();
                //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                String lines[] = toSpeak.split("\\r?\\n");
                for(String line : lines) {
                    speaker.speak(line);
                }

            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.pause(LONG_DURATION);
            }


        });
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

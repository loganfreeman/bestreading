package education.loganfreeman.com.bestreading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import education.loganfreeman.com.bestreading.adapter.NovelListAdapter;
import education.loganfreeman.com.bestreading.base.BaseActivity;
import education.loganfreeman.com.bestreading.utils.ToastUtil;

/**
 * Created by scheng on 3/31/17.
 */

public class NovelListActivity extends BaseActivity {

    @BindView(R.id.novel_listview)
    ListView novelListView;

    @BindView(R.id.action_before)
    FloatingActionButton beforeBtn;

    @BindView(R.id.action_after)
    FloatingActionButton afterBtn;

    private List<Novels.Novel> novels;

    private Novels.Genre genre;

    private static final String NOVEL_LIST = "novel_list";

    private static final String NOVEL_GENRE = "novel_genre";

    public static void start(Context context, List<Novels.Novel> novels, Novels.Genre genre) {
        Intent intent = new Intent(context, NovelListActivity.class);
        intent.putExtra(NOVEL_LIST, Parcels.wrap(novels));
        intent.putExtra(NOVEL_GENRE, Parcels.wrap(genre));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_list);
        ButterKnife.bind(this);
        novels = Parcels.unwrap(getIntent().getParcelableExtra(NOVEL_LIST));
        genre = Parcels.unwrap(getIntent().getParcelableExtra(NOVEL_GENRE));
        initView();

    }

    private void initView() {
        safeSetTitle(genre.getTitle());
        NovelListAdapter adapter = new NovelListAdapter(this, novels);
        novelListView.setAdapter(adapter);
        novelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Novels.Novel novel = adapter.getItem(position);
                ToastUtil.showShort(novel.getUrl());
            }
        });

    }
}

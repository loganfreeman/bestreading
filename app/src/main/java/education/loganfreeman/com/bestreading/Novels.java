package education.loganfreeman.com.bestreading;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;

/**
 * Created by shanhong on 3/30/17.
 */

public class Novels {

    public static final String URL = "http://www.8novels.net";

    private static final AsyncSubject<List<Genre>> mGenreSubject = AsyncSubject.create();

    private static final AsyncSubject<List<Novel>> mNovelSubject = AsyncSubject.create();

    public static Observable<List<Genre>> getGenreAsync() {
        Observable<List<Genre>> firstTimeObservable =
                Observable.fromCallable(Novels::getGenre);

        return firstTimeObservable.concatWith(mGenreSubject);
    }

    public static Observable<List<Novel>> getNovelsAsync(String url) {
        Observable<List<Novel>> observable = Observable.fromCallable(() -> Novels.getNovels(url));
        
        return observable.concatWith(mNovelSubject);
    }

    public static List<Novel> getNovels(String url) throws IOException {
        List<Novel> novels = new ArrayList<Novel>();
        Document document = Jsoup.connect(url).get();
        Elements links = document.select("div.content a.a2");
        for(Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            String author = link.nextElementSibling().text();
            novels.add(new Novel(author, linkHref, linkText));
        }
        return novels;
    }

    public static List<Genre> getGenre() throws IOException {
        List<Genre> genres = new ArrayList<Genre>();
        Document document = Jsoup.connect(URL).get();
        Elements links = document.select("nav ul li a");
        for(Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            Genre genre = new Genre(linkText, linkHref);
            genres.add(genre);
        }
        return genres;
    }

    public static class Novel {
        public final String author;

        public final String url;

        public  final String title;

        public Novel(String a, String u, String t) {
            author = a;
            title = t;
            url = u;
        }
    }

    public static class Genre {
        public  final String title;

        public String url;

        public Genre(String name) {
            this.title = name;
        }

        public Genre(String t, String u) {
            this.title = t;
            this.url = u;
        }

        public String getTitle() {
            return title;
        }
    }
}

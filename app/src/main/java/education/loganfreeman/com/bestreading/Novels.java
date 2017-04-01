package education.loganfreeman.com.bestreading;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import education.loganfreeman.com.bestreading.utils.PLog;
import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;

/**
 * Created by shanhong on 3/30/17.
 */

public class Novels {

    public static final String URL = "http://www.8novels.net";

    public static final String AUTHOR_URL = "http://www.8novels.net/authors/";

    public static final Pattern title_by_author = Pattern.compile("[\\w\\s]+(by [\\w\\s]+)");

    public static final Pattern by_author = Pattern.compile("(by [\\w\\s]+)");

    private static final AsyncSubject<List<Genre>> mGenreSubject = AsyncSubject.create();

    private static final AsyncSubject<List<Novel>> mNovelSubject = AsyncSubject.create();

    private static final AsyncSubject<List<Author>> mAuthorSubject = AsyncSubject.create();

    public static Observable<List<Genre>> getGenreAsync() {
        Observable<List<Genre>> firstTimeObservable =
                Observable.fromCallable(Novels::getGenre);

        return firstTimeObservable.concatWith(mGenreSubject);
    }

    public static Observable<List<Novel>> getNovelsAsync(String url) {
        PLog.i(url);
        Observable<List<Novel>> observable = Observable.fromCallable(() -> Novels.getNovels(url));

        return observable.concatWith(mNovelSubject);
    }

    public static Observable<List<Author>> getAuthorsAsync() {
        Observable<List<Author>> firstTimeObservable =
                Observable.fromCallable(Novels::getAuthors);

        return firstTimeObservable.concatWith(mAuthorSubject);
    }

    public static List<Author> getAuthors() throws IOException {
        List<Author> authors = new ArrayList<Author>();
        Document document = Jsoup.connect(AUTHOR_URL).get();
        Elements links = document.select("div.main table td.tb p a");
        for(Element link : links) {
            String url = link.attr("href");
            String name = link.text();
            Author author = new Author(name, url);
            authors.add(author);
        }
        return authors;
    }

    public static List<Novel> getNovels(String url) throws IOException {
        List<Novel> novels = new ArrayList<Novel>();
        Document document = Jsoup.connect(url).get();
        Elements links = document.select("div.content a");
        for(Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            String author = link.nextSibling().outerHtml().trim();
            if(!by_author.matcher(author).matches()) {
                Matcher matcher = title_by_author.matcher(linkText);

                if(matcher.matches()) {
                    author = matcher.group(1);
                }else {
                    author = "unknown";
                }
            }
            Novel novel = new Novel(author, linkHref, linkText);
            //PLog.i(novel.toString());
            novels.add(novel);
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

    @Parcel
    public static class Novel {
        private final String author;

        private final String url;

        private final String title;

        @ParcelConstructor
        public Novel(String author, String url, String title) {
            this.author = author;
            this.title = title;
            this.url = url;
        }


        public String getAuthor() {
            return author;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String toString() {
            return author + " " + url + " " + title;
        }
    }

    @Parcel
    public static class Genre {
        private final String title;

        private String url;

        @ParcelConstructor
        public Genre(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

    @Parcel
    public static class Author {
        private final String name;

        private final String url;

        @ParcelConstructor
        public Author(String name, String url) {
            this.name = name;

            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}

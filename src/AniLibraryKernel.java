import components.standard.Standard;

/**
 * Anime library kernel component with primary methods.
 * (Note: by package-wide convention, all references are non-null.)
 *
 * @mathsubtypes <pre>
 * ANILIBRARY_MODEL is a finite set of (title: String, genre: String)
 *  exemplar library
 *  constraint
 *   for all (t1, g1), (t2, g2) in library:
 *     if t1 = t2 then g1 = g2
 * </pre>
 * @mathdefinitions <pre>
 * TITLES(library): finite set of String satisfies
 *  for all t: String (t is in TITLES(library) iff there exists g: String ((t, g) is in library))
 *
 * GENRES(library): finite set of String satisfies
 *  for all g: String (g is in GENRES(library) iff there exists t: String ((t, g) is in library))
 * </pre>
 * @mathmodel type AniLibraryKernel is modeled by ANILIBRARY_MODEL
 * @initially <pre>
 * ():
 *  ensures
 *   this = {}
 * </pre>
 * @iterator <pre>
 * entries(~this.seen * ~this.unseen) = this and
 * |~this.seen * ~this.unseen| = |this|
 * </pre>
 */
public interface AniLibraryKernel extends Standard<AniLibrary> {

    /**
     * Adds a new anime title and its genre to this library.
     *
     * @param title
     *            the title of the anime
     * @param genre
     *            the genre of the anime
     * @updates this
     * @requires
     *  title and genre are not null and title is not in the domain of this
     * @ensures
     *  this = #this union {(title, genre)}
     */
    void addSeries(String title, String genre);

    /**
     * Removes the given anime from this library and returns its genre.
     *
     * @param title
     *            the title of the anime
     * @updates this
     * @requires
     *  title is in the domain of this
     * @ensures
     *  removeSeries = the value associated with title in #this and
     *  this = #this minus {(title, removeSeries)}
     * @return the genre of the removed anime
     */
    String removeSeries(String title);

    /**
     * Reports whether the given anime title is in this library.
     *
     * @param title
     *            the title of the anime
     * @ensures
     *  hasSeries = (title is in the domain of this)
     * @return true if the title is found, false otherwise
     */
    boolean hasSeries(String title);

    /**
     * Reports the genre associated with a given anime title.
     *
     * @param title
     *            the title of the anime
     * @requires
     *  title is in the domain of this
     * @ensures
     *  genreOf = the value associated with title in this
     * @return the genre of the anime
     */
    String genreOf(String title);

    /**
     * Reports the number of anime entries in this library.
     *
     * @ensures
     *  size = the number of pairs in this
     * @return the size of the library
     */
    int size();
}

package components.anilibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test fixture for {@code AniLibrary}'s kernel methods.
 *
 * @author Kevin Trinh
 */
public abstract class AniLibraryKernelTest {

    /**
     * Invokes the appropriate {@code AniLibrary} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new anilibrary
     * @ensures constructorTest = 0
     */
    protected abstract AniLibrary constructorTest();

    /**
     * Build a library from alternating title/genre pairs.
     *
     * @param pairs
     *            alternating sequence: title, genre, title, genre, ...
     * @return a new {@code AniLibrary} containing exactly the given pairs
     * @requires pairs.length is even and all titles are unique
     * @ensures result contains each (title, genre) from {@code pairs}
     */
    private AniLibrary build(String... pairs) {
        assert pairs.length % 2 == 0 : "pairs must be even length";
        AniLibrary lib = this.constructorTest();
        for (int i = 0; i < pairs.length; i += 2) {
            assert !lib.hasSeries(pairs[i]) : "duplicate title in args";
            lib.addSeries(pairs[i], pairs[i + 1]);
        }
        return lib;
    }

    /**
     * Take a non-destructive snapshot of a library’s contents.
     *
     * @param lib
     *            the library to snapshot
     * @return a map title -> genre representing {@code lib}
     * @ensures lib is restored to its original state
     */
    private java.util.Map<String, String> snapshot(AniLibrary lib) {
        java.util.Map<String, String> m = new HashMap<>();
        AniLibrary hold = lib.newInstance();
        while (lib.size() > 0) {
            String[] e = lib.removeAny();
            m.put(e[0], e[1]);
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            lib.addSeries(e[0], e[1]);
        }
        return m;
    }

    /**
     * Assert that the library contents equal the expected map.
     *
     * @param expected
     *            expected title->genre
     * @param actual
     *            the library to compare
     */
    private void assertEqualsMap(java.util.Map<String, String> expected,
            AniLibrary actual) {
        assertEquals(expected, this.snapshot(actual));
    }

    /**
     * Testing: constructor creates a library with no entries (empty snapshot).
     */
    @Test
    public void testConstructorCreatesEmptyLibrary() {
        AniLibrary lib = this.constructorTest();
        assertTrue(this.snapshot(lib).isEmpty());
    }

    /**
     * Testing: size() returns 0 on a newly constructed library.
     */
    @Test
    public void testSizeZeroOnNewLibrary() {
        AniLibrary lib = this.constructorTest();
        assertEquals(0, lib.size());
    }

    /**
     * Testing: addSeries to empty adds the (title, genre) pair and size becomes
     * 1.
     */
    @Test
    public void testAddSeriesToEmpty() {
        AniLibrary lib = this.constructorTest();
        lib.addSeries("AOT", "Action");
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        assertEquals(1, lib.size());
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: addSeries to non-empty adds the new pair and preserves existing
     * entries.
     */
    @Test
    public void testAddSeriesToNonEmpty() {
        AniLibrary lib = this.build("AOT", "Action");
        lib.addSeries("Haikyuu", "Sports");
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Haikyuu", "Sports");
        assertEquals(2, lib.size());
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: hasSeries returns true when the given title exists; state
     * unchanged.
     */
    @Test
    public void testHasSeriesTrue() {
        AniLibrary lib = this.build("AOT", "Action", "Your Lie in April",
                "Romance");
        assertTrue(lib.hasSeries("AOT"));
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Your Lie in April", "Romance");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: genreOf returns the correct genre for an existing title; state
     * unchanged.
     */
    @Test
    public void testGenreOfExistingTitle() {
        AniLibrary lib = this.build("AOT", "Action", "Your Lie in April",
                "Romance");
        assertEquals("Action", lib.genreOf("AOT"));
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Your Lie in April", "Romance");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: hasSeries returns false for a title not in the library; state
     * unchanged.
     */
    @Test
    public void testHasSeriesFalse() {
        AniLibrary lib = this.build("AOT", "Action");
        assertFalse(lib.hasSeries("Haikyuu"));
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: removeSeries removes the only entry and returns its genre,
     * leaving the library empty.
     */
    @Test
    public void testRemoveSeriesOne() {
        AniLibrary lib = this.build("AOT", "Action");
        String genre = lib.removeSeries("AOT");
        assertEquals("Action", genre);
        assertEquals(0, lib.size());
        assertTrue(this.snapshot(lib).isEmpty());
    }

    /**
     * Testing: removeSeries removes one entry from multiple and returns the
     * removed genre.
     */
    @Test
    public void testRemoveSeriesFromMany() {
        AniLibrary lib = this.build("AOT", "Action", "Haikyuu", "Sports",
                "YLiA", "Romance");
        String genre = lib.removeSeries("Haikyuu");
        assertEquals("Sports", genre);
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("YLiA", "Romance");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: removeAny on a single-entry library removes that pair and leaves
     * size 0.
     */
    @Test
    public void testRemoveAnySingle() {
        AniLibrary lib = this.build("AOT", "Action");
        String[] removed = lib.removeAny();
        assertEquals(2, removed.length);
        assertTrue(removed[0].equals("AOT"));
        assertEquals("Action", removed[1]);
        assertEquals(0, lib.size());
    }

    /**
     * Testing: removeAny on a multi-entry library removes one arbitrary pair;
     * remaining entries intact.
     */
    @Test
    public void testRemoveAnyFromManyKeepsSetMinusOne() {
        AniLibrary lib = this.build("A", "X", "B", "Y", "C", "Z");
        Map<String, String> before = this.snapshot(lib);
        String[] r = lib.removeAny();
        assertEquals(2, r.length);
        before.remove(r[0]);
        this.assertEqualsMap(before, lib);
    }

    /**
     * Testing: clear removes all entries, leaving an empty library with size 0.
     */
    @Test
    public void testClear() {
        AniLibrary lib = this.build("A", "X", "B", "Y");
        lib.clear();
        assertEquals(0, lib.size());
        assertTrue(this.snapshot(lib).isEmpty());
    }

    /**
     * Testing: newInstance creates a fresh, empty library.
     */
    @Test
    public void testNewInstanceCreatesEmptyLibrary() {
        AniLibrary seeded = this.build("A", "X");
        AniLibrary fresh = seeded.newInstance();
        assertEquals(0, fresh.size());
        assertTrue(this.snapshot(fresh).isEmpty());
    }

    /**
     * Testing: transferFrom moves all entries into the target and clears the
     * source.
     */
    @Test
    public void testTransferFromMovesAllAndClearsSource() {
        AniLibrary source = this.build("A", "X", "B", "Y");
        AniLibrary target = source.newInstance();

        target.transferFrom(source);

        assertEquals(0, source.size());
        assertTrue(this.snapshot(source).isEmpty());

        java.util.Map<String, String> exp = new java.util.HashMap<>();
        exp.put("A", "X");
        exp.put("B", "Y");
        this.assertEqualsMap(exp, target);
    }
}

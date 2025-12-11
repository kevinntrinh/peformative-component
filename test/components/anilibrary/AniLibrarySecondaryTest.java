package components.anilibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test fixture for {@code AniLibrary}'s secondary methods.
 *
 * @author Kevin Trinh
 *
 */
public abstract class AniLibrarySecondaryTest {

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

    @Test
    public void testUpdateGenre() {
        AniLibrary lib = this.build("AOT", "Action", "Haikyuu", "Sports");
        lib.updateGenre("Haikyuu", "Slice");
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Haikyuu", "Slice");
        this.assertEqualsMap(exp, lib);
    }

    @Test
    public void testCountByGenre() {
        AniLibrary lib = this.build("A", "X", "B", "Y", "C", "X");
        assertEquals(2, lib.countByGenre("X"));
        assertEquals(1, lib.countByGenre("Y"));
        assertEquals(0, lib.countByGenre("Z"));
    }

    @Test
    public void testMergeWith() {
        AniLibrary a = this.build("A", "X");
        AniLibrary b = this.build("B", "Y");
        a.mergeWith(b);
        Map<String, String> exp = new HashMap<>();
        exp.put("A", "X");
        exp.put("B", "Y");
        this.assertEqualsMap(exp, a);
        assertEquals(0, b.size()); // cleared
    }

    @Test
    public void testSharesTitleWith() {
        AniLibrary a = this.build("A", "X", "B", "Y");
        AniLibrary b = this.build("C", "Z", "B", "Q");
        assertTrue(a.sharesTitleWith(b));
    }
}

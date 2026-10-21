package applicationMain;
import entityClasses.LessonsLearned;	
import entityClasses.LessonsLearnedCollection;
import database.Database;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * <p> Title: LessonsLearnedTestbed </p>
 *
 * <p> Description: A console test driver for the create, read, update, and delete (CRUD)
 * operations that {@link database.Database} provides for {@link entityClasses.LessonsLearned}.
 * It runs against an in-memory H2 database and prints a PASS or FAIL line per check, followed
 * by a summary.</p>
 *
 * @author Harrison
 * @version 2.0
 */

public class LessonsLearnedTestbed {
	
    private static Database db;
    private static int passed = 0;
    private static int failed = 0;
    
    /**
     * Connects to an in-memory database, runs the create, read, update, and delete tests,
     * and prints the totals.
     *
     * @param args the command-line arguments (not used)
     * @throws Exception if the database connection cannot be established or closed
     */

    public static void main(String[] args) throws Exception {
        // Database reads this into a static final field when the class first loads,
        // so it must be set BEFORE anything touches Database.
        System.setProperty("foundation.db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        db = new Database();
        db.connectToDatabase();

        runCreateTests();
        runReadTests();
        runUpdateTests();
        runDeleteTests();

        db.closeConnection();
        System.out.println("\n==== " + passed + " passed, " + failed + " failed ====");
    }

    /* ---------- HELPERS ---------- */

    /** Records and prints one result. Exceptions inside a test count as a failure. */
    private static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("PASS  " + name); }
        else           { failed++; System.out.println("FAIL  " + name); }
    }

    /** A unique author per test keeps tests independent of each other. */
    private static String newAuthor() {
        return "author_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /** Saves a lesson and returns it as read back from the database (with its real ID). */
    private static LessonsLearned saveAndReload(String author, String title, String text) {
        db.addLesson(new LessonsLearned(author, title, text));
        List<LessonsLearned> list = db.getLessonByAuthor(author).getLessons();
        return list.get(list.size() - 1);
    }

    /* ---------- CREATE ---------- */

    private static void runCreateTests() throws Exception {
        System.out.println("--- Create ---");

        String a = newAuthor();
        db.addLesson(new LessonsLearned(a, "Run the full test suite before submitting",
                "I skipped a test once and lost points for a bug it would have caught."));
        List<LessonsLearned> got = db.getLessonByAuthor(a).getLessons();
        check("TC-01 valid lesson is saved and readable",
                got.size() == 1
                && got.get(0).getLessonTitle().equals("Run the full test suite before submitting")
                && got.get(0).getAuthor().equals(a));

        String b = newAuthor();
        saveAndReload(b, "Persistent lesson", "Still here after a new connection.");
        Database second = new Database();
        second.connectToDatabase();
        check("TC-02 lesson persists on a new connection",
                second.getLessonByAuthor(b).getLessons().size() == 1);
    }

    /* ---------- READ ---------- */

    private static void runReadTests() {
        System.out.println("--- Read ---");

        String a = newAuthor(), b = newAuthor();
        saveAndReload(a, "A's lesson", "Written by A.");
        saveAndReload(b, "B's lesson", "Written by B.");
        List<LessonsLearned> mine = db.getLessonByAuthor(a).getLessons();
        check("TC-10 only the author's own lessons are returned",
                mine.size() == 1 && mine.get(0).getLessonTitle().equals("A's lesson"));

        LessonsLearnedCollection empty = db.getLessonByAuthor(newAuthor());
        check("TC-11 author with no lessons gets an empty, non-null collection",
                empty != null && empty.getLessons().isEmpty());

        LessonsLearned saved = saveAndReload(newAuthor(), "Find me", "Lookup by ID.");
        LessonsLearned found = db.getLessonByID(saved.getLessonID());
        check("TC-12 getLessonByID returns matching fields",
                found != null && found.getLessonTitle().equals("Find me")
                && found.getLessonText().equals("Lookup by ID."));

        check("TC-13 getLessonByID with a missing ID returns null",
                db.getLessonByID(-999) == null);

        LessonsLearnedCollection c = new LessonsLearnedCollection();
        boolean zero = c.getLessons().size() == 0;
        c.addLesson(new LessonsLearned("x", "one", "text"));
        boolean one = c.getLessons().size() == 1;
        for (int i = 0; i < 1000; i++) c.addLesson(new LessonsLearned("x", "t" + i, "text"));
        check("TC-14 collection works with 0, 1, and 1001 lessons",
                zero && one && c.getLessons().size() == 1001 && c.returnTitles().size() == 1001);

        LessonsLearnedCollection r = new LessonsLearnedCollection();
        r.addLesson(new LessonsLearned("x", "Keep", "text"));
        check("TC-15 removeLessonByTitle: match true, no match false",
                r.removeLessonByTitle("keep") && !r.removeLessonByTitle("nothing"));
    }

    /* ---------- UPDATE ---------- */

    private static void runUpdateTests() {
        System.out.println("--- Update ---");

        String a = newAuthor();
        LessonsLearned old = saveAndReload(a, "Old title", "Old text.");
        boolean ok = db.updateLesson(old.getLessonID(), new LessonsLearned(a, "New title", "New text."));
        LessonsLearned now = db.getLessonByID(old.getLessonID());
        check("TC-16 update persists; ID and author unchanged",
                ok && now.getLessonTitle().equals("New title")
                && now.getLessonText().equals("New text.")
                && now.getAuthor().equals(a)
                && now.getLessonID() == old.getLessonID());

        String b = newAuthor();
        LessonsLearned first = saveAndReload(b, "First", "First text.");
        LessonsLearned second = saveAndReload(b, "Second", "Second text.");
        db.updateLesson(first.getLessonID(), new LessonsLearned(b, "Changed", "Changed text."));
        check("TC-17 updating one lesson leaves others untouched (WHERE clause)",
                db.getLessonByID(second.getLessonID()).getLessonTitle().equals("Second"));
    }

    /* ---------- DELETE ---------- */

    private static void runDeleteTests() {
        System.out.println("--- Delete ---");

        LessonsLearned l = saveAndReload(newAuthor(), "Delete me", "Gone soon.");
        boolean deleted = db.deleteLesson(l.getLessonID());
        check("TC-21 deleting an existing lesson removes it",
                deleted && db.getLessonByID(l.getLessonID()) == null);

        check("TC-22 deleting a missing ID returns false",
                !db.deleteLesson(-999));
    }
	
}

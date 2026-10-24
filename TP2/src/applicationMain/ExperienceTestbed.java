package applicationMain;

import java.nio.file.Files;	
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import database.Database;
import entityClasses.LessonExperience;
import entityClasses.LessonsLearned;
import entityClasses.Category;
import entityClasses.User;
import guiExperience.ModelExperience;

/*******
 * <p> Title: ExperienceTestbed </p>
 * <p> Description: Semi-automated positive/negative checks for the Contributor
 * experience story. PASS requires the expected value or specific validation message;
 * unexpected exceptions stop the run. A legacy schema and a separate JVM exercise
 * additive migration and real file persistence, without touching normal user data.</p>
 * @author Huichang
 */
public final class ExperienceTestbed {
	/** Counts successful assertions in this process (child totals are reported separately). */
	private static int passed;
	/** A database action expected to reject invalid input. */
	private interface Action {
		/** Executes the candidate operation. @throws Exception if it fails */
		void run() throws Exception;
	}
	/** Console-only test driver. */
	private ExperienceTestbed() { }
	/** Stops on a failed assertion so a partial run cannot be reported as passing. */
	private static void check(String name, boolean condition) {
		if (!condition) throw new AssertionError("FAIL: " + name);
		passed++;
		System.out.println("PASS: " + name);
	}
	/** Requires rejection and checks the context-specific message, not only exception type. */
	private static void rejects(String name, String message, Action action) throws Exception {
		try { action.run(); }
		catch (IllegalArgumentException error) {
			check(name, error.getMessage().contains(message));
			return;
		}
		throw new AssertionError("FAIL: " + name + " was accepted");
	}

	/**
	 * Runs isolated tests; arguments are internal child-process persistence parameters.
	 * <ul>
	 * <li>EXP-01/02/03: existing lesson migration, empty history, complete saved values,
	 * and multiple attempts without overwriting the original lesson.</li>
	 * <li>EXP-04 through 13: required fields, numeric formats, member effort, maximum
	 * lengths, exact boundaries, and rejected requests leaving history unchanged.</li>
	 * <li>EXP-14 through 21: account/lesson access, distinct histories, literal quoted
	 * text, and an immutable read result. Each rejection checks its error message.</li>
	 * <li>EXP-22/23: separate-process persistence and parent-delete cleanup. These tests
	 * fail on missing data, wrong values, a nonzero child exit, or orphan records.</li>
	 * </ul>
	 * @param args empty for a normal test run
	 * @throws Exception if a test, storage operation, or cleanup fails
	 */
	public static void main(String[] args) throws Exception {
		if (args.length > 0) { verifyRestart(args); return; }
		Path directory = Files.createTempDirectory("tp2-experience-");
		String url = "jdbc:h2:" + directory.resolve("experience");
		System.setProperty("foundation.db.url", url);
		Class.forName("org.h2.Driver");
		// Simulate the team's existing four-column lessons table before the new code connects.
		try (Connection connection = DriverManager.getConnection(url, "sa", "");
				var statement = connection.createStatement()) {
			statement.execute("CREATE TABLE lessons (id INT AUTO_INCREMENT PRIMARY KEY, "
					+ "author VARCHAR(255), title VARCHAR(100), text VARCHAR(255))");
			statement.execute("INSERT INTO lessons(author,title,text) VALUES ('alice','Existing lesson','Original text.')");
		}
		Database database = new Database();
		int lessonId;
		try {
			database.connectToDatabase();
			try {
				for (String name : new String[] {"alice", "bob", "viewer"}) database.register(new User(
						name, "TestPass1!", "Test", "", "User", "", name + "@example.com",
						false, !name.equals("viewer"), false));
				lessonId = database.getLessonByAuthor("alice").getLessons().get(0).getLessonID();
				ModelExperience model = new ModelExperience(database, "alice", lessonId);
				check("EXP-01 migration preserves old lesson; empty history is valid", model.list().isEmpty()
						&& database.getLessonByID(lessonId).getLessonText().equals("Original text."));
				long first = model.add(" Applied lesson ", " Followed the documented steps ", "30.50", "Alex: 20\nSam: 10.50");
				LessonExperience saved = model.list().get(0);
				check("EXP-02 create/read all fields and identity", saved.getId() == first
						&& saved.getLessonId() == lessonId && saved.getContributor().equals("alice")
						&& saved.getWhatDone().equals("Applied lesson")
						&& saved.getHowDone().equals("Followed the documented steps")
						&& saved.getElapsedMinutes().toPlainString().equals("30.50")
						&& saved.getMemberEffort().equals("Alex: 20\nSam: 10.50"));
				model.add("Second attempt", "Used the lesson again", "12", "Alex: 0\nSam: 12");
				check("EXP-03 append preserves history and original lesson", model.list().size() == 2
						&& model.list().get(0).getId() == first
						&& database.getLessonByID(lessonId).getLessonText().equals("Original text."));
				for (String missing : new String[] {null, "", "  "}) {
					rejects("EXP-04 required what", "What was done", () -> model.add(missing,"How","1","A: 1"));
					rejects("EXP-05 required how", "How it was done", () -> model.add("What",missing,"1","A: 1"));
					rejects("EXP-06 required member effort", "Member effort", () -> model.add("What","How","1",missing));
				}
				for (String minutes : new String[] {null, "", "-1", "0", "NaN", "Infinity", "1.234", "1e3", "10000000000"})
					rejects("EXP-07 invalid elapsed " + minutes, "Elapsed minutes", () -> model.add("What","How",minutes,"A: 1"));
				String[][] invalidEfforts = {{"A", "Member name: minutes"}, {": 2", "Member name: minutes"},
						{"A: -1", "Effort for A"}, {"A: nope", "Effort for A"},
						{"A: 1\na: 2", "only once"}, {"A: 1\n\nB: 2", "Member name: minutes"}};
				for (String[] invalid : invalidEfforts)
					rejects("EXP-08 invalid effort " + invalid[0].replace('\n', '/'), invalid[1],
							() -> model.add("What","How","1",invalid[0]));
				rejects("EXP-09 overlong what", "10000", () -> model.add("W".repeat(10001),"How","1","A: 1"));
				rejects("EXP-10 overlong how", "10000", () -> model.add("What","H".repeat(10001),"1","A: 1"));
				rejects("EXP-11 overlong effort", "10000", () -> model.add("What","How","1","A".repeat(10001)));
				check("EXP-12 failed inputs add no records", model.list().size() == 2);
				model.add("W".repeat(10000), "H".repeat(10000), "9999999999.99", "A: 0.01");
				LessonExperience limit = model.list().get(2);
				check("EXP-13 exact boundaries round-trip", limit.getWhatDone().length() == 10000
						&& limit.getHowDone().length() == 10000
						&& limit.getElapsedMinutes().toPlainString().equals("9999999999.99"));
				for (String account : new String[] {"bob", "viewer", "missing", "", null}) {
					rejects("EXP-14 forbidden add " + account, "required", () -> database.addExperience(account,lessonId,"What","How","1","A: 1"));
					rejects("EXP-15 forbidden history " + account, "required", () -> database.getExperiences(account,lessonId));
				}
				for (int id : new int[] {0, -1, 999999}) {
					rejects("EXP-16 invalid/missing lesson add", id <= 0 ? "positive" : "required", () -> database.addExperience("alice",id,"What","How","1","A: 1"));
					rejects("EXP-17 invalid/missing lesson read", id <= 0 ? "positive" : "required", () -> database.getExperiences("alice",id));
				}
				database.addLesson(new LessonsLearned("alice", "Other lesson", "Different original text.", Category.General));
				int other = database.getLessonByAuthor("alice").getLessons().get(1).getLessonID();
				check("EXP-18 histories are lesson-specific", database.getExperiences("alice", other).isEmpty());
				database.deleteLesson(other);
				model.add("Don't concatenate SQL", "Input: ' OR '1'='1", "2", "A: 2");
				check("EXP-19 quoted text is literal", model.list().get(3).getHowDone().equals("Input: ' OR '1'='1"));
				rejects("EXP-20 unchanged access after invalid inputs", "required", () -> database.getExperiences("bob",lessonId));
				try { model.list().clear(); throw new AssertionError("History was mutable"); }
				catch (UnsupportedOperationException expected) { check("EXP-21 immutable history", true); }
			} finally { database.closeConnection(); }
			List<String> command = new ArrayList<>();
			command.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
			command.add("-cp"); command.add(System.getProperty("java.class.path"));
			Module module = ExperienceTestbed.class.getModule();
			if (module.isNamed()) {
				command.add("--module-path"); command.add(System.getProperty("jdk.module.path"));
				command.add("--module"); command.add(module.getName() + "/" + ExperienceTestbed.class.getName());
			} else command.add(ExperienceTestbed.class.getName());
			command.add(url); command.add(Integer.toString(lessonId));
			check("EXP-22 separate-process restart", new ProcessBuilder(command).inheritIO().start().waitFor() == 0);
			System.out.println("Experience checks passed: " + passed + "; failed: 0");
		} finally {
			try (var paths = Files.walk(directory)) {
				for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) Files.delete(path);
			}
		}
	}

	/** Reopens the file in a separate process and checks persisted values and delete cleanup. */
	private static void verifyRestart(String[] args) throws Exception {
		System.setProperty("foundation.db.url", args[0]);
		Database database = new Database();
		database.connectToDatabase();
		try {
			int id = Integer.parseInt(args[1]);
			List<LessonExperience> attempts = database.getExperiences("alice", id);
			check("EXP-22 four complete attempts survive restart", attempts.size() == 4
					&& attempts.get(0).getWhatDone().equals("Applied lesson")
					&& attempts.get(0).getElapsedMinutes().toPlainString().equals("30.50")
					&& attempts.get(3).getHowDone().equals("Input: ' OR '1'='1"));
			check("EXP-23 delete parent removes its experience history", database.deleteLesson(id));
			try (Connection connection = DriverManager.getConnection(args[0], "sa", "");
					var rows = connection.createStatement().executeQuery("SELECT COUNT(*) FROM lesson_experiences")) {
				rows.next();
				check("EXP-23 no orphan experience rows", rows.getInt(1) == 0);
			}
		} finally { database.closeConnection(); }
	}
}

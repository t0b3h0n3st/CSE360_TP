package entityClasses;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/*******
 * <p> Title: LessonExperience </p>
 * <p> Description: An immutable record of one attempt to use an existing lesson.
 * The Contributor experience story supplies what was done, how, elapsed time, and
 * effort by team member. Separate records preserve earlier attempts for future analysis.
 * Minutes and two decimal places are implementation choices, not course requirements.</p>
 * @author Huichang Zhang 
 */
public final class LessonExperience {
	/** Database identity; zero represents an attempt not yet inserted. */
	private final long id;
	/** Associates the attempt with its existing lesson rather than creating a new lesson. */
	private final int lessonId;
	/** Signed-in contributor; never taken from an editable owner field. */
	private final String contributor;
	/** Describes the activity performed during this attempt. */
	private final String whatDone;
	/** Describes the procedure used, independently of the original lesson text. */
	private final String howDone;
	/** Elapsed minutes, distinct from the sum of members' working minutes. */
	private final BigDecimal elapsedMinutes;
	/** One named member and their working minutes per line; retains individual effort. */
	private final String memberEffort;

	/**
	 * Validates an attempt before storage and when constructing a saved snapshot.
	 * @param id generated ID, or zero for a new attempt
	 * @param lessonId positive ID of the existing lesson
	 * @param contributor signed-in contributor name
	 * @param whatDone activity performed; required, maximum 10,000 characters
	 * @param howDone procedure followed; required, maximum 10,000 characters
	 * @param elapsedMinutes positive elapsed minutes with at most two decimal places
	 * @param memberEffort lines in the form {@code Member name: minutes}
	 * @throws IllegalArgumentException if any input violates the field rules
	 */
	public LessonExperience(long id, int lessonId, String contributor, String whatDone,
			String howDone, BigDecimal elapsedMinutes, String memberEffort) {
		if (lessonId <= 0) throw new IllegalArgumentException("Lesson ID must be positive.");
		this.id = id;
		this.lessonId = lessonId;
		this.contributor = required("Contributor", contributor, 255);
		this.whatDone = required("What was done", whatDone, 10000);
		this.howDone = required("How it was done", howDone, 10000);
		if (elapsedMinutes == null)
			throw new IllegalArgumentException("Elapsed minutes is required.");
		this.elapsedMinutes = parseMinutes("Elapsed minutes", elapsedMinutes.toPlainString(), false);
		this.memberEffort = required("Member effort", memberEffort, 10000);
		Set<String> members = new HashSet<>();
		for (String line : this.memberEffort.split("\\R", -1)) {
			int separator = line.lastIndexOf(':');
			if (separator <= 0)
				throw new IllegalArgumentException("Use Member name: minutes on each effort line.");
			String member = required("Member name", line.substring(0, separator), 255);
			if (!members.add(member.toLowerCase(java.util.Locale.ROOT)))
				throw new IllegalArgumentException("Enter each team member only once.");
			parseMinutes("Effort for " + member, line.substring(separator + 1), true);
		}
	}

	/** Normalizes required text before validation and persistence. */
	private static String required(String field, String value, int maximum) {
		if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required.");
		String trimmed = value.strip();
		if (trimmed.length() > maximum)
			throw new IllegalArgumentException(field + " must be at most " + maximum + " characters.");
		return trimmed;
	}

	/**
	 * Parses decimal minutes consistently for the GUI and database boundary.
	 * @param field field name included in an actionable error
	 * @param value decimal text; no exponent notation
	 * @param allowZero whether a member may report zero working minutes
	 * @return minutes fitting the database's DECIMAL(12,2) column
	 * @throws IllegalArgumentException for missing, malformed, negative, or oversized values
	 */
	public static BigDecimal parseMinutes(String field, String value, boolean allowZero) {
		String text = value == null ? "" : value.strip();
		if (!text.matches("[0-9]{1,10}(\\.[0-9]{1,2})?"))
			throw new IllegalArgumentException(field + " must be minutes with up to two decimal places (max 9999999999.99).");
		BigDecimal minutes = new BigDecimal(text);
		if (!allowZero && minutes.signum() == 0)
			throw new IllegalArgumentException(field + " must be greater than zero.");
		return minutes;
	}

	/** Returns generated experience ID.
	 * @return generated experience ID
	 */
	public long getId() { return id; }
	/** Returns associated lesson ID.
	 * @return associated lesson ID
	 */
	public int getLessonId() { return lessonId; }
	/** Returns contributing username.
	 * @return contributing username
	 */
	public String getContributor() { return contributor; }
	/** Returns activity description.
	 * @return activity description
	 */
	public String getWhatDone() { return whatDone; }
	/** Returns procedure description.
	 * @return procedure description
	 */
	public String getHowDone() { return howDone; }
	/** Returns elapsed minutes, not total person-minutes.
	 * @return elapsed minutes, not total person-minutes
	 */
	public BigDecimal getElapsedMinutes() { return elapsedMinutes; }
	/** Returns named members and their individual working minutes.
	 * @return named members and their individual working minutes
	 */
	public String getMemberEffort() { return memberEffort; }
	/** Returns a compact history row; selecting it displays the full record.
	 * @return a compact history row; selecting it displays the full record
	 */
	@Override public String toString() { return "Attempt " + id + " | " + elapsedMinutes + " min | " + contributor; }
}

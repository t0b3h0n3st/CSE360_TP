# Experience information — Huichang

Branch: `experience-information/Huichuan`, based on the team's existing TP2.

## Scope and design choices

Implements the Contributor story about adding experience to an existing lesson after
another attempt: what was done, how, elapsed time, and effort for individual team members.
It adapts HW2's database-backed input/feedback pattern to the team code; it does not
replace the team's lesson entity, CRUD methods, or pages with the old HW2 project.

- Each Add Experience appends a `LessonExperience`; it never overwrites earlier attempts.
- `lesson_experiences` is an additive table created when the existing Database connects.
  Original lesson rows remain intact. Deleting a parent lesson cascades to its attempts.
- What/how are required, stripped, and limited to 10,000 characters each.
- Elapsed time is positive decimal **minutes**, up to two decimal places.
- Member effort uses **Name: minutes**, one member per line, required, max 10,000 characters.
  Duplicate member names are rejected case-insensitively; individual effort can be zero.
  Elapsed time and person-minutes are deliberately distinct. Numeric storage accepts
  at most 9,999,999,999.99 minutes; limits and units are design choices, not course rules.
- The feature follows the current TP2 author-owned Contributor workflow. Database methods
  recheck the registered Contributor and lesson owner, not just a UI flag. Supporting
  experience on another author's lesson would require a team access-policy decision.
- Existing title/description locks are not changed. A future whole-lesson/experience lock
  must be coordinated with the lock owner. Experience editing, deletion, and curator
  statistics are outside this add-experience story and are not introduced here.

## Entry and files

Contributor Home → Lessons Learned Home → View Lesson → **Experience Information**.
Select a saved attempt to read details; use the separate editor for a new attempt.

- `entityClasses/LessonExperience.java`: validated immutable attempt snapshot.
- `guiExperience/`: View, Controller, Model and package documentation.
- `database/Database.java`: additive table; `addExperience`, `getExperiences`, access check.
- `guiViewLesson/ViewViewLesson.java`: new navigation button only.
- `applicationMain/ExperienceTestbed.java`: isolated migration, validation, access,
  history, file persistence and parent-delete cleanup tests.

## Verification

The project compiles with the existing JavaFX25.0.4 and H2 libraries; no new dependencies.
Run `applicationMain.ExperienceTestbed` as a Java Application in Eclipse.
The normal run reports 53 parent checks passed and 0 failed; the child prints 3 checks.
Both classpath and named-module child launching are supported.
The temporary file database is removed at the end; normal accounts/lessons are not touched.
Existing LessonsLearnedTestbed: 12 passed; LessonsLearnedValidation: 11 passed.
Page rendering was inspected for the new entry and the new page at 800 × 600.

## Manual acceptance checklist

The user reported completing the GUI checks below, including saved history after restart,
validation messages, and account isolation. These are user-reported results; capture the
required source/comment screenshots separately for the final submission.

1. Refresh/build this project in Eclipse and log in as an existing Contributor.
2. Open an owned lesson and use Experience Information; confirm empty history is handled.
3. Enter What: Applied the lesson; How: Followed the steps; Elapsed: 30.50;
   Member effort: Alex: 20 / Sam: 10.50 on separate lines. Add and select the saved attempt.
4. Add another attempt; check both entries and all stored fields; original lesson is unchanged.
5. Try blank descriptions, zero/negative/non-numeric elapsed, and malformed member effort.
   Check helpful messages, preserved form input, and no added history row.
6. Clear New Attempt, Refresh History, Return to Lesson, and revisit; confirm expected state.
7. Restart from the same Eclipse working directory; verify history survives. Switch users
   and check another account cannot use this lesson's experience page.
8. Capture descriptively named **source-code and comment screenshots** for TP2 submission;
   generated Javadoc and UI render snapshots alone do not replace those screenshots.

## Documentation and integration

Javadoc is in the feature source, including field rationale and test requirement mapping.
Generated feature Javadoc is included under TP2/ in Lessons_Learned_Javadocs,
Contributor_Javadocs, and ContributorTests_Javadocs, with HTML and supporting resources.
These cover this feature and its integration points, not the full team's final documentation.
Regenerate the complete team documentation after integration to preserve shared links.
Required source/comment screenshots still need to be added for the final submission.
Confirm the units, access policy and lock interaction with Harrison/Ivan before merging.

## Eclipse launch configurations

- `TP2 Application`: `applicationMain.FoundationsMain`, project working directory,
  `jdbc:h2:./TP2Database` (separate from HW2 and the default FoundationDatabase).
- `TP2 Experience Tests`: isolated temporary-database tests, safe to repeat.
- Both use the existing JavaFX25.0.4/H2 user libraries and Java 25.
  VM flags include `--add-modules javafx.controls --enable-native-access=javafx.graphics`.
- In Eclipse: Run → Run Configurations → Java Application → choose the matching name.
  Both configurations have been imported; the Eclipse test run reported 53 passed, 0 failed.
- First GUI launch uses a new database. Complete first-admin setup yourself, then create
  or assign a Contributor account through the existing administration workflow.
- The user reported completing the GUI acceptance checks listed above.

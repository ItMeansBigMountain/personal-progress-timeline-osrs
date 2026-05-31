# Personal Progress Timeline

Personal Progress Timeline is a lightweight RuneLite plugin that records notable personal milestones during the current client session. It watches local game messages for level-ups, quest completions, and combat achievement completions, then keeps a small in-memory timeline and can announce each captured milestone back to the chat box.

This repo is structured as a standalone RuneLite external plugin following the same Gradle wrapper and metadata conventions used by the `breach-check-osrs` boilerplate/reference repo.

## Features

- Detects common RuneLite game messages for:
  - Skill level-ups, for example `Strength level 75`
  - Quest completions, for example `Quest complete: Dragon Slayer II`
  - Combat achievement completions
- Keeps the most recent milestones in memory for the current RuneLite session.
- Optional login summary showing that the plugin is active and how many session milestones have been recorded.
- Optional local chat announcement when a milestone is captured.
- No external APIs, credentials, or network calls are used.

## Configuration

RuneLite exposes these settings in the plugin configuration panel:

- `Show Login Summary`: show a short status message after logging in.
- `Announce Milestones`: add a local chat message when a milestone is recorded.
- `Session Milestones Stored`: maximum number of recent session milestones to keep in memory.

## Development requirements

- Java 11. In this workspace, use:

  ```sh
  export JAVA_HOME=/opt/data/jdks/current-java11
  export PATH="$JAVA_HOME/bin:$PATH"
  ```

- The included Gradle wrapper (`./gradlew`).

## Build and test

From this plugin directory:

```sh
export JAVA_HOME=/opt/data/jdks/current-java11
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test --no-daemon -q
./gradlew assemble --no-daemon -q
```

The test suite currently focuses on the RuneLite-independent milestone parser so it can run without launching a live client.

## Run locally in RuneLite developer mode

From this plugin directory:

```sh
export JAVA_HOME=/opt/data/jdks/current-java11
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew run --no-daemon
```

Manual smoke test checklist:

1. Confirm the plugin appears as `Personal Progress Timeline` in RuneLite.
2. Toggle the three config options and confirm they persist in the RuneLite config panel.
3. Log in and verify the optional login summary appears only when enabled.
4. Trigger or simulate a level-up, quest completion, or combat achievement message and verify the plugin records/announces it when `Announce Milestones` is enabled.
5. Confirm normal chat/game messages do not produce timeline announcements.

## RuneLite metadata

- Plugin class: `com.itmeansbigmountain.personalprogresstimeline.PersonalProgressTimelinePlugin`
- Config group: `personalprogresstimeline`
- Display name: `Personal Progress Timeline`
- Plugin Hub properties: `runelite-plugin.properties`

## Plugin Hub prep notes

Before submitting to RuneLite Plugin Hub, perform the manual smoke test above in a live RuneLite client and add screenshots or a short GIF if maintainers request visual proof. This plugin does not depend on Wise Old Man, TempleOSRS, or any other external service, so there are no API keys or rate-limit concerns to document.

## Product direction update

Personal Progress Timeline should become a persistent account history side panel. Once installed it starts tracking forward from that point, and when the player opens collection log/achievement interfaces it should backfill known owned items/milestones from RuneLite-visible state.

Target UX:

- Scrollable side panel timeline with timestamps for levels, quests, combat achievements, collection-log items, pets, notable drops, boss KC milestones, clue milestones, and other account milestones.
- Persist data locally so milestones survive client restarts.
- On opening the collection log, update the timeline with items already owned; mark these as backfilled/observed if exact acquisition time is unknown.
- Connect to public tracking websites/APIs such as Wise Old Man and TempleOSRS for data robustness, timestamps, recent collections, gains, and cross-checking.
- Keep confidence/source metadata per timeline item and avoid claiming exact timestamps when an API only proves the item existed by a snapshot time.

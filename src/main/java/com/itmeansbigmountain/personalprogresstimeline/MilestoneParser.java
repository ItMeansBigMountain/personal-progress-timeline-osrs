package com.itmeansbigmountain.personalprogresstimeline;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small, RuneLite-independent parser for the chat milestones this plugin records.
 */
final class MilestoneParser
{
	private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");
	private static final Pattern LEVEL_PATTERN = Pattern.compile("(?i)^congratulations, you just advanced (?:an? )?(.+?) level\\. your .+? level is now (\\d+)\\.");
	private static final Pattern QUEST_COMPLETE_PREFIX_PATTERN = Pattern.compile("(?i)^(?:congratulations[,! ]+)?quest complete[:!]?\\s*(.*)$");
	private static final Pattern QUEST_COMPLETED_PATTERN = Pattern.compile("(?i)^you have completed (?:the )?(.+?)(?: quest)?[!.]?$");
	private static final Pattern COMBAT_ACHIEVEMENT_PATTERN = Pattern.compile("(?i)^combat achievement complete[:!]?\\s*(.+)");

	private MilestoneParser()
	{
	}

	static Optional<String> parse(String rawMessage)
	{
		String message = normalize(rawMessage);
		if (message.isEmpty())
		{
			return Optional.empty();
		}

		Matcher levelMatcher = LEVEL_PATTERN.matcher(message);
		if (levelMatcher.find())
		{
			return Optional.of(capitalize(levelMatcher.group(1)) + " level " + levelMatcher.group(2));
		}

		Matcher combatAchievementMatcher = COMBAT_ACHIEVEMENT_PATTERN.matcher(message);
		if (combatAchievementMatcher.find())
		{
			return Optional.of("Combat achievement: " + combatAchievementMatcher.group(1).trim());
		}

		Matcher questCompletePrefixMatcher = QUEST_COMPLETE_PREFIX_PATTERN.matcher(message);
		if (questCompletePrefixMatcher.find())
		{
			return formatQuest(questCompletePrefixMatcher.group(1));
		}

		Matcher questCompletedMatcher = QUEST_COMPLETED_PATTERN.matcher(message);
		if (questCompletedMatcher.find())
		{
			return formatQuest(questCompletedMatcher.group(1));
		}

		return Optional.empty();
	}

	static String normalize(String rawMessage)
	{
		if (rawMessage == null)
		{
			return "";
		}

		return TAG_PATTERN.matcher(rawMessage)
			.replaceAll("")
			.replace('\u00a0', ' ')
			.trim()
			.replaceAll("\\s+", " ");
	}

	private static String capitalize(String value)
	{
		if (value == null || value.isEmpty())
		{
			return value;
		}
		return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
	}

	private static Optional<String> formatQuest(String questName)
	{
		if (questName == null || questName.trim().isEmpty())
		{
			return Optional.of("Quest complete");
		}

		return Optional.of("Quest complete: " + questName.trim().replaceAll("[!.:]$", ""));
	}
}

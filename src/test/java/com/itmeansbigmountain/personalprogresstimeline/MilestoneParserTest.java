package com.itmeansbigmountain.personalprogresstimeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

public class MilestoneParserTest
{
	@Test
	public void parsesLevelUpMessages()
	{
		Optional<String> milestone = MilestoneParser.parse("Congratulations, you just advanced a Strength level. Your Strength level is now 75.");

		assertTrue(milestone.isPresent());
		assertEquals("Strength level 75", milestone.get());
	}

	@Test
	public void parsesQuestCompletionMessages()
	{
		Optional<String> milestone = MilestoneParser.parse("You have completed Dragon Slayer II!");

		assertTrue(milestone.isPresent());
		assertEquals("Quest complete: Dragon Slayer II", milestone.get());
	}

	@Test
	public void parsesQuestCompletePrefixMessages()
	{
		Optional<String> milestone = MilestoneParser.parse("Quest complete: Dragon Slayer II");

		assertTrue(milestone.isPresent());
		assertEquals("Quest complete: Dragon Slayer II", milestone.get());
	}

	@Test
	public void handlesQuestCompleteWithoutQuestName()
	{
		Optional<String> milestone = MilestoneParser.parse("Quest complete!");

		assertTrue(milestone.isPresent());
		assertEquals("Quest complete", milestone.get());
	}

	@Test
	public void parsesCombatAchievementMessages()
	{
		Optional<String> milestone = MilestoneParser.parse("Combat Achievement complete: Perfect Theatre");

		assertTrue(milestone.isPresent());
		assertEquals("Combat achievement: Perfect Theatre", milestone.get());
	}

	@Test
	public void stripsRuneLiteChatMarkup()
	{
		String normalized = MilestoneParser.normalize("<col=ff0000>Combat Achievement complete: Perfect Theatre</col>");

		assertEquals("Combat Achievement complete: Perfect Theatre", normalized);
	}

	@Test
	public void ignoresNonMilestoneMessages()
	{
		assertFalse(MilestoneParser.parse("Welcome to Old School RuneScape.").isPresent());
	}
}

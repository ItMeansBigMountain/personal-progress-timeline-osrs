package com.itmeansbigmountain.personalprogresstimeline;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("personalprogresstimeline")
public interface PersonalProgressTimelineConfig extends Config
{
	@ConfigItem(
		keyName = "showLoginMessage",
		name = "Show Login Summary",
		description = "Show a short timeline status message when logging in"
	)
	default boolean showLoginMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceMilestones",
		name = "Announce Milestones",
		description = "Add a local chat message when a level-up, quest completion, or notable unlock is recorded"
	)
	default boolean announceMilestones()
	{
		return true;
	}

	@ConfigItem(
		keyName = "maxSessionMilestones",
		name = "Session Milestones Stored",
		description = "Maximum number of recent milestones to keep in the current RuneLite session"
	)
	default int maxSessionMilestones()
	{
		return 10;
	}
}

package com.itmeansbigmountain.personalprogresstimeline;

import com.google.inject.Provides;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Personal Progress Timeline",
	description = "Keeps a lightweight session timeline of personal milestones such as level-ups and quest completions.",
	tags = {"progress", "timeline", "milestones", "levels", "quests"}
)
public class PersonalProgressTimelinePlugin extends Plugin
{
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	@Inject
	private Client client;

	@Inject
	private PersonalProgressTimelineConfig config;

	private final Deque<String> sessionMilestones = new ArrayDeque<>();

	@Override
	protected void startUp()
	{
		sessionMilestones.clear();
		log.debug("Personal Progress Timeline started");
	}

	@Override
	protected void shutDown()
	{
		sessionMilestones.clear();
		log.debug("Personal Progress Timeline stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN && config.showLoginMessage())
		{
			String noun = sessionMilestones.size() == 1 ? "milestone" : "milestones";
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Personal Progress Timeline is tracking this session (" + sessionMilestones.size() + " " + noun + " recorded).", null);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		Optional<String> milestone = MilestoneParser.parse(event.getMessage());
		milestone.ifPresent(this::recordMilestone);
	}

	private void recordMilestone(String milestone)
	{
		String timelineEntry = LocalTime.now().format(TIME_FORMATTER) + " - " + milestone;
		sessionMilestones.addLast(timelineEntry);
		while (sessionMilestones.size() > Math.max(1, config.maxSessionMilestones()))
		{
			sessionMilestones.removeFirst();
		}

		log.debug("Recorded milestone: {}", timelineEntry);
		if (config.announceMilestones())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Personal Progress Timeline recorded: " + milestone, null);
		}
	}

	@Provides
	PersonalProgressTimelineConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PersonalProgressTimelineConfig.class);
	}
}

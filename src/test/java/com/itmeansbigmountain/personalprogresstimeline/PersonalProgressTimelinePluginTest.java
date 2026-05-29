package com.itmeansbigmountain.personalprogresstimeline;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PersonalProgressTimelinePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PersonalProgressTimelinePlugin.class);
		RuneLite.main(args);
	}
}
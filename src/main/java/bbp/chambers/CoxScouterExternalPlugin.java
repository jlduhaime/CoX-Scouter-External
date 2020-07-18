/*
 * Copyright (c) 2018, Kamiel
 * Copyright (c) 2020, Truth Forger <https://github.com/Blackberry0Pie>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package bbp.chambers;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.raids.Raid;
import net.runelite.client.plugins.raids.RaidRoom;
import net.runelite.client.plugins.raids.RoomType;
import net.runelite.client.plugins.raids.events.RaidScouted;
import net.runelite.client.plugins.raids.events.RaidReset;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginDescriptor(
	name = "CoX Scouter External"
)
public class CoxScouterExternalPlugin extends Plugin
{
	@Getter
	private Raid raid;

	@Inject
	private Client client;

	@Inject
	private CoxScouterExternalConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private CoxScouterExternalOverlay overlay;

	@Getter
	private final Set<String> roomWhitelist = new HashSet<String>();

	@Getter
	private final Set<String> roomBlacklist = new HashSet<String>();

	@Getter
	private final Set<String> rotationWhitelist = new HashSet<String>();

	@Getter
	private final Set<String> layoutWhitelist = new HashSet<String>();

	@Getter
	private int raidPartyID;

	// if the player is inside of a raid or not
	@Getter
	private boolean inRaidChambers;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		updateLists();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		inRaidChambers = false;
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("raids") && !event.getGroup().equals("coxscouterexternal"))
		{
			return;
		}

		updateLists();
	}

	@Subscribe
	public void onRaidScouted(RaidScouted raidScouted)
	{
		this.raid = raidScouted.getRaid();
		//raidScouted.isFirstScout();
	}

	@Subscribe
	public void onRaidReset(RaidReset raidReset)
	{
		this.raid = null;
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		int tempPartyID = client.getVar(VarPlayer.IN_RAID_PARTY);
		boolean tempInRaid = client.getVar(Varbits.IN_RAID) == 1;

		// if the player's party state has changed
		if (tempPartyID != raidPartyID)
		{
			raidPartyID = tempPartyID;
		}

		// if the player's raid state has changed
		if (tempInRaid != inRaidChambers)
		{
			inRaidChambers = tempInRaid;
		}
	}

	@Provides
	CoxScouterExternalConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoxScouterExternalConfig.class);
	}

	@VisibleForTesting
	void updateLists()
	{
		updateList(roomWhitelist, configManager.getConfiguration("raids", "whitelistedRooms"));
		updateList(roomBlacklist, configManager.getConfiguration("raids", "blacklistedRooms"));
		updateList(layoutWhitelist, configManager.getConfiguration("raids", "whitelistedLayouts"));

		// Update rotation whitelist
		rotationWhitelist.clear();
		for (String line : configManager.getConfiguration("raids", "whitelistedRotations").split("\\n"))
		{
			rotationWhitelist.add(line.toLowerCase().replace(" ", ""));
		}
	}

	private void updateList(Collection<String> list, String input)
	{
		list.clear();
		for (String s : Text.fromCSV(input.toLowerCase()))
		{
			if (s.equals("unknown"))
			{
				list.add("unknown (combat)");
				list.add("unknown (puzzle)");
			}
			else
			{
				list.add(s);
			}
		}
	}

	boolean getRotationMatches()
	{
		RaidRoom[] combatRooms = getCombatRooms();
		String rotation = Arrays.stream(combatRooms)
				.map(RaidRoom::getName)
				.map(String::toLowerCase)
				.collect(Collectors.joining(","));

		return rotationWhitelist.contains(rotation);
	}

	RaidRoom[] getCombatRooms()
	{
		List<RaidRoom> combatRooms = new ArrayList<>();

		for (Room room : raid.getLayout().getRooms())
		{
			if (room == null)
			{
				continue;
			}

			if (raid.getRooms()[room.getPosition()].getType() == RoomType.COMBAT)
			{
				combatRooms.add(raid.getRooms()[room.getPosition()]);
			}
		}

		return combatRooms.toArray(new RaidRoom[0]);
	}
}

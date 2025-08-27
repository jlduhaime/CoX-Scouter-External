/*
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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.Color;

@ConfigGroup("coxscouterexternal")
public interface CoxScouterExternalConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "showTutorialOverlay",
		name = "Show tutorial overlay",
		description = "Whether to show an overlay to help understand how to use the plugin"
	)
	default boolean showTutorialOverlay()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = "scoutOverlay",
		name = "Show scout overlay",
		description = "Display an overlay that shows the current raid layout (when entering lobby)"
	)
	default boolean scoutOverlay()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "displayFloorBreak",
		name = "Layout floor break",
		description = "Displays floor break in layout"
	)
	default boolean displayFloorBreak()
	{
		return false;
	}

	@ConfigItem(
		position = 3,
		keyName = "screenshotHotkey",
		name = "Scouter screenshot hotkey",
		description = "Hotkey used to screenshot the scouting overlay"
	)
	default Keybind screenshotHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 4,
		keyName = "showRecommendedItems",
		name = "Show recommended items",
		description = "Adds overlay with recommended items to scouter"
	)
	default boolean showRecommendedItems()
	{
		return false;
	}

	@ConfigItem(
		position = 5,
		keyName = "recommendedItems",
		name = "Recommended items",
		description = "User-set recommended items in the form: [muttadiles,ice barrage,zamorak godsword],[tekton,elder maul], ..."
	)
	default String recommendedItems()
	{
		return "";
	}

	@ConfigItem(
		position = 6,
		keyName = "showSupplies",
		name = "Show Supplies",
		description = "Shows dropped supplies from scouted rooms for no-prep planning"
	)
	default boolean showSupplies() { return false; }

	@ConfigItem(
		position = 7,
		keyName = "highlightedRooms",
		name = "Highlighted rooms",
		description = "Display highlighted rooms in a different color on the overlay. Separate with comma (full name)"
	)
	default String highlightedRooms()
	{
		return "";
	}

	@ConfigItem(
		position = 8,
		keyName = "highlightColor",
		name = "Highlight color",
		description = "The color of highlighted rooms"
	)
	default Color highlightColor()
	{
		return Color.MAGENTA;
	}

	@ConfigItem(
		position = 9,
		keyName = "hideMissingHighlighted",
		name = "Hide missing highlighted",
		description = "Completely hides raids missing highlighted room(s)"
	)
	default boolean hideMissingHighlighted()
	{
		return false;
	}

	@ConfigItem(
		position = 10,
		keyName = "highlightedShowThreshold",
		name = "Show threshold",
		description = "The number of highlighted rooms needed to show the raid. 0 means no threshold."
	)
	default int highlightedShowThreshold()
	{
		return 0;
	}

	@ConfigItem(
		position = 11,
		keyName = "hideBlacklist",
		name = "Hide raids with blacklisted",
		description = "Completely hides raids containing blacklisted room(s)"
	)
	default boolean hideBlacklisted()
	{
		return false;
	}

	@ConfigItem(
		position = 12,
		keyName = "hideMissingLayout",
		name = "Hide missing layout",
		description = "Completely hides raids missing a whitelisted layout"
	)
	default boolean hideMissingLayout()
	{
		return false;
	}

	@ConfigItem(
		position = 13,
		keyName = "hideRopeless",
		name = "Hide ropeless raids",
		description = "Completely hides raids missing a tightrope"
	)
	default boolean hideRopeless()
	{
		return false;
	}
}

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

@ConfigGroup("coxscouterexternal")
public interface CoxScouterExternalConfig extends Config
{
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
		keyName = "displayFloorBreak",
		name = "Layout floor break",
		description = "Displays floor break in layout"
	)
	default boolean displayFloorBreak()
	{
		return false;
	}

	@ConfigItem(
		keyName = "screenshotHotkey",
		name = "Scouter screenshot hotkey",
		description = "Hotkey used to screenshot the scouting overlay"
	)
	default Keybind screenshotHotkey()
	{
		return Keybind.NOT_SET;
	}
}

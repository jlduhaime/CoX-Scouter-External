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

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class CoxScouterExternalTutorialOverlay extends OverlayPanel
{
	private final CoxScouterExternalConfig config;
	private final CoxScouterExternalPlugin plugin;

	private final LineComponent line1;
	private final LineComponent line2;
	private final LineComponent line3;
	private final LineComponent line4;

	@Inject
	private CoxScouterExternalTutorialOverlay(CoxScouterExternalConfig config, CoxScouterExternalPlugin plugin)
	{
		this.config = config;
		this.plugin = plugin;

		panelComponent.setPreferredSize(new Dimension(234, 0));

		line1 = LineComponent.builder().left("This scouter can work at the same time as the default scouter.").build();
		line2 = LineComponent.builder().left("You can turn off the default scouter: Chambers -> Show scout overlay.").build();
		line3 = LineComponent.builder().left("You MUST use a unique screenshot hotkey for the screenshot feature to work.").build();
		line4 = LineComponent.builder().left("You can turn off this tutorial overlay: CoX External -> disable Show tutorial overlay.").build();

		setPriority(OverlayPriority.LOW);
		setPosition(OverlayPosition.TOP_RIGHT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showTutorialOverlay())
		{
			return null;
		}

		if (!plugin.isShouldShowOverlays())
		{
			return null;
		}

		panelComponent.getChildren().add(line1);
		panelComponent.getChildren().add(line2);
		panelComponent.getChildren().add(line3);
		panelComponent.getChildren().add(line4);

		return super.render(graphics);
	}
}

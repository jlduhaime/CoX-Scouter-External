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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.Client;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;

import net.runelite.api.SpriteID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.WorldService;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

import net.runelite.client.plugins.raids.RaidRoom;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;
import net.runelite.http.api.worlds.WorldResult;

public class CoxScouterExternalOverlay extends OverlayPanel
{
	static final String BROADCAST_ACTION = "Broadcast layout";
	static final String SCREENSHOT_ACTION = "Screenshot";
	private static final int BORDER_OFFSET = 2;
	private static final int ICON_SIZE = 32;
	private static final int SMALL_ICON_SIZE = 21;
	//might need to edit these if they are not standard
	private static final int TITLE_COMPONENT_HEIGHT = 20;
	private static final int LINE_COMPONENT_HEIGHT = 16;

	private Client client;
	private CoxScouterExternalPlugin plugin;
	private CoxScouterExternalConfig config;

	private final ItemManager itemManager;
	private final SpriteManager spriteManager;
	private final PanelComponent panelImages = new PanelComponent();

	@Getter
	private boolean scoutOverlayShown = false;

	@Inject
	private WorldService worldService;

	@Inject
	private ConfigManager configManager;

	@Inject
	private CoxScouterExternalOverlay(Client client, CoxScouterExternalPlugin plugin, CoxScouterExternalConfig config, ItemManager itemManager, SpriteManager spriteManager)
	{
		super(plugin);
		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.LOW);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.itemManager = itemManager;
		this.spriteManager = spriteManager;
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Raids overlay"));
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY, BROADCAST_ACTION, "Raids overlay"));
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY, SCREENSHOT_ACTION, "Raids overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isShouldShowOverlays())
		{
			return null;
		}

		Color color = Color.WHITE;
		String layout = plugin.getRaid().getLayout().toCodeString();

		String displayLayout;
		if (config.displayFloorBreak())
		{
			displayLayout = plugin.getRaid().getLayout().toCode();
			displayLayout = displayLayout.substring(0, displayLayout.length() - 1).replaceAll("#", "").replaceFirst("¤", " | ");
		}
		else
		{
			displayLayout = layout;
		}
		if (configManager.getConfiguration("raids", "enableLayoutWhitelist", Boolean.class) && !plugin.getLayoutWhitelist().contains(layout.toLowerCase()))
		{
			color = Color.RED;
		}

		boolean hide = false;
		HashSet<String> roomNames = new HashSet();
		for (Room layoutRoom : plugin.getRaid().getLayout().getRooms())
		{
			int position = layoutRoom.getPosition();
			RaidRoom room = plugin.getRaid().getRoom(position);

			if (room == null)
			{
				continue;
			}
			roomNames.add(room.getName().toLowerCase());

			if (config.hideBlacklisted() && plugin.getRoomBlacklist().contains(room.getName().toLowerCase()))
			{
				hide = true;
				break;
			}
		}

		if (!hide && config.hideMissingHighlighted())
		{
			for (String requiredRoom : plugin.getRoomHighlightedList())
			{
				if (!roomNames.contains(requiredRoom))
				{
					hide = true;
					break;
				}
			}
		}

		if (hide)
		{
			panelComponent.getChildren().add(TitleComponent.builder()
					.text("Bad Raid!")
					.color(Color.RED)
					.build());

			return super.render(graphics);
		}

		panelComponent.getChildren().add(TitleComponent.builder()
				.text(displayLayout)
				.color(color)
				.build());

		if (configManager.getConfiguration("raids", "ccDisplay", Boolean.class))
		{
			color = Color.RED;
			FriendsChatManager friendsChatManager = client.getFriendsChatManager();
			FontMetrics metrics = graphics.getFontMetrics();

			String worldString = "W" + client.getWorld();
			WorldResult worldResult = worldService.getWorlds();
			if (worldResult != null)
			{
				World world = worldResult.findWorld(client.getWorld());
				WorldRegion region = world.getRegion();
				if (region != null)
				{
					String countryCode = region.getAlpha2();
					worldString += " (" + countryCode + ")";
				}
			}

			String owner = "Join a FC";
			if (friendsChatManager != null)
			{
				owner = friendsChatManager.getOwner();
				color = Color.ORANGE;
			}

			panelComponent.setPreferredSize(new Dimension(Math.max(ComponentConstants.STANDARD_WIDTH, metrics.stringWidth(worldString) + metrics.stringWidth(owner) + 14), 0));
			panelComponent.getChildren().add(LineComponent.builder()
					.left(worldString)
					.right(owner)
					.leftColor(Color.ORANGE)
					.rightColor(color)
					.build());
		}

		Set<Integer> imageIds = new HashSet<>();
		FontMetrics metrics = graphics.getFontMetrics();
		int roomWidth = 0;
		int temp = 0;

		for (Room layoutRoom : plugin.getRaid().getLayout().getRooms())
		{
			int position = layoutRoom.getPosition();
			RaidRoom room = plugin.getRaid().getRoom(position);

			if (room == null)
			{
				continue;
			}

			temp = metrics.stringWidth(room.getName());
			if (temp > roomWidth)
			{
				roomWidth = temp;
			}

			color = Color.WHITE;

			switch (room.getType())
			{
				case COMBAT:
					String bossName = room == RaidRoom.UNKNOWN_COMBAT ? "Unknown" : room.getName();
					String bossNameLC = bossName.toLowerCase();
					if (config.showRecommendedItems() && plugin.getRecommendedItemsList().get(bossNameLC) != null)
						imageIds.addAll(plugin.getRecommendedItemsList().get(bossNameLC));
					if (plugin.getRoomHighlightedList().contains(bossNameLC))
					{
						color = config.highlightColor();
					}
					else if (plugin.getRoomWhitelist().contains(bossNameLC))
					{
						color = Color.GREEN;
					}
					else if (plugin.getRoomBlacklist().contains(bossNameLC)
							|| configManager.getConfiguration("raids", "enableRotationWhitelist", Boolean.class) && !plugin.getRotationMatches())
					{
						color = Color.RED;
					}

					panelComponent.getChildren().add(LineComponent.builder()
							.left(config.showRecommendedItems() ? "" : room.getType().getName())
							.right(bossName)
							.rightColor(color)
							.build());

					break;

				case PUZZLE:
					String puzzleName = room == RaidRoom.UNKNOWN_PUZZLE ? "Unknown" : room.getName();
					String puzzleNameLC = puzzleName.toLowerCase();
					if (config.showRecommendedItems() && plugin.getRecommendedItemsList().get(puzzleNameLC) != null)
						imageIds.addAll(plugin.getRecommendedItemsList().get(puzzleNameLC));
					if (plugin.getRoomHighlightedList().contains(puzzleNameLC))
					{
						color = config.highlightColor();
					}
					else if (plugin.getRoomWhitelist().contains(puzzleNameLC))
					{
						color = Color.GREEN;
					}
					else if (plugin.getRoomBlacklist().contains(puzzleNameLC))
					{
						color = Color.RED;
					}

					panelComponent.getChildren().add(LineComponent.builder()
							.left(config.showRecommendedItems() ? "" : room.getType().getName())
							.right(puzzleName)
							.rightColor(color)
							.build());
					break;
			}
		}

		//add recommended items
		Dimension panelDims = super.render(graphics);
		if (config.showRecommendedItems() && imageIds.size() > 0)
		{
			panelImages.getChildren().clear();
			Integer[] idArray = imageIds.toArray(new Integer[0]);
			int imagesVerticalOffset = TITLE_COMPONENT_HEIGHT + (configManager.getConfiguration("raids", "ccDisplay", Boolean.class) ? LINE_COMPONENT_HEIGHT : 0) - BORDER_OFFSET;
			int imagesMaxHeight = (int) panelDims.getHeight() - BORDER_OFFSET - imagesVerticalOffset;
			boolean smallImages = false;

			panelImages.setPreferredLocation(new Point(0, imagesVerticalOffset));
			panelImages.setBackgroundColor(null);
			panelImages.setWrap(true);
			panelImages.setPreferredSize(new Dimension(2 * ICON_SIZE, 0));
			if (2 * imagesMaxHeight / ICON_SIZE < idArray.length)
			{
				smallImages = true;
				panelImages.setPreferredSize(new Dimension(3 * SMALL_ICON_SIZE, 0));
			}

			panelImages.setOrientation(ComponentOrientation.HORIZONTAL);
			for (Integer e : idArray)
			{
				final BufferedImage image = getImage(e, smallImages);
				if (image != null)
				{
					panelImages.getChildren().add(new ImageComponent(image));
				}
			}

			panelImages.render(graphics);
		}
		return panelDims;
	}

	private BufferedImage getImage(int id, boolean small)
	{
		BufferedImage bim;
		if (id != SpriteID.SPELL_ICE_BARRAGE)
			bim = itemManager.getImage(id);
		else
			bim = spriteManager.getSprite(id, 0);
		if (bim == null)
			return null;
		if (!small)
			return ImageUtil.resizeCanvas(bim, ICON_SIZE, ICON_SIZE);
		if (id != SpriteID.SPELL_ICE_BARRAGE)
			return ImageUtil.resizeImage(bim, SMALL_ICON_SIZE, SMALL_ICON_SIZE);
		return ImageUtil.resizeCanvas(bim, SMALL_ICON_SIZE, SMALL_ICON_SIZE);
	}
}

package bbp.chambers;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "CoX Scouter External"
)
public class CoxScouterExternalPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CoxScouterExternalConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("CoX Scouter External started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("CoX Scouter External stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "CoX Scouter External says " + config.greeting(), null);
		}
	}

	@Provides
	CoxScouterExternalConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoxScouterExternalConfig.class);
	}
}

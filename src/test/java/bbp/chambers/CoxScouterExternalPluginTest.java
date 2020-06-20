package bbp.chambers;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CoxScouterExternalPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CoxScouterExternalPlugin.class);
		RuneLite.main(args);
	}
}
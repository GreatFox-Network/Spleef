package me.matsync.Spleef.util;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class Messages {

	private static String prefix = "[" + ChatColor.DARK_AQUA + "Spleef" + ChatColor.WHITE + "] ";
	
	public static void sendHotbarMessage(Player player, ChatColor color, String message) {
		CraftPlayer cp = (CraftPlayer) player;

		IChatBaseComponent icbc = null;

		icbc = ChatSerializer.a("{\"text\": \"" + color + message + "\"}");

		PacketPlayOutChat ppot = null;

		ppot = new PacketPlayOutChat(icbc, (byte) 2);

		cp.getHandle().playerConnection.sendPacket(ppot);
	}

	public static void sendPlayerMessage(Player player, String message) {
		player.sendMessage(prefix + message);
	}

	public static void sendTitleMessage(Player player, String title, String subtitle, Integer fadeIn, Integer time,
			Integer fadeOut) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null,
				fadeIn.intValue(), time.intValue(), fadeOut.intValue());
		connection.sendPacket(packetPlayOutTimes);

		if (title != null) {
			title = title.replaceAll("%player%", player.getDisplayName());
			IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
					titleMain);
			connection.sendPacket(packetPlayOutTitle);
		}

		if (subtitle != null) {
			subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
			IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(
					PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}
	}
}
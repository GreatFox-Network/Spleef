package me.matsync.Spleef.util;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerUtils {

	public static void clearItems(Player player) {
		player.getInventory().clear();
		player.getInventory().setBoots(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setHelmet(null);
		player.getInventory().setLeggings(null);
	}
	
	public static void healPlayer(Player player) {
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(10);
	}
	
	public static void setDefaultGameInventory(Player player) {
		clearItems(player);
		
		ItemStack shovel;
		ItemStack snowball;
		
		if (player.hasPermission("spleef.perks")) {
			shovel = new ItemStack(Material.DIAMOND_SPADE);
			snowball = new ItemStack(Material.SNOW_BALL, 16);
		} else {
			shovel = new ItemStack(Material.IRON_SPADE);
			snowball = new ItemStack(Material.SNOW_BALL, 8);
		}
		
		shovel.addEnchantment(Enchantment.DIG_SPEED, 3);
		
		ItemMeta shovelMeta = shovel.getItemMeta();
		ItemMeta snowballMeta = snowball.getItemMeta();
		
		shovelMeta.setDisplayName(ChatColor.AQUA + "Spleef Shovel");
		
		snowballMeta.setDisplayName(ChatColor.AQUA + "Snowball");
		snowballMeta.setLore(Arrays.asList(ChatColor.GRAY + "This snowball melts snow", ChatColor.GRAY + "blocks on touch!"));
		
		shovel.setItemMeta(shovelMeta);
		snowball.setItemMeta(snowballMeta);
		
		player.getInventory().setItem(0, shovel);
		player.getInventory().setItem(1, snowball);
	}
	
	public static void shootRandomFirework(Location location) {
		Firework fw = (Firework) location.getWorld().spawn(location, Firework.class);
		FireworkMeta meta = fw.getFireworkMeta();
		Random random = new Random();
		int type = random.nextInt(5) + 1;
		Type fireworkType = null;
		
		switch (type) {
		case 1:
			fireworkType = Type.BALL;
		case 2:
			fireworkType = Type.CREEPER;
		case 3:
			fireworkType = Type.STAR;
		case 4:
			fireworkType = Type.BURST;
		case 5:
			fireworkType = Type.BALL_LARGE;
		}
		
		int color = random.nextInt(16) + 1;
		Color fireworkColor = null;
		
		switch (color) {
		case 1:
			fireworkColor = Color.AQUA;
		case 2:
			fireworkColor = Color.BLACK;
		case 3: 
			fireworkColor = Color.BLUE;
		case 4:
			fireworkColor = Color.FUCHSIA;
		case 5:
			fireworkColor = Color.GRAY;
		case 6:
			fireworkColor = Color.GREEN;
		case 7:
			fireworkColor = Color.LIME;
		case 8:
			fireworkColor = Color.MAROON;
		case 9:
			fireworkColor = Color.NAVY;
		case 10:
			fireworkColor = Color.OLIVE;
		case 11:
			fireworkColor = Color.PURPLE;
		case 12:
			fireworkColor = Color.RED;
		case 13:
			fireworkColor = Color.SILVER;
		case 14:
			fireworkColor = Color.TEAL;
		case 15:
			fireworkColor = Color.WHITE;
		case 16:
			fireworkColor = Color.YELLOW;
		}
		
		meta.addEffect(FireworkEffect.builder().flicker(random.nextBoolean()).trail(random.nextBoolean()).withColor(fireworkColor).with(fireworkType).build());
		meta.setPower(random.nextInt(3) + 1);
		fw.setFireworkMeta(meta);
	}
}
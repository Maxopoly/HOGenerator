package test.bla;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventorygui.AnimatedClickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class BlaListener implements Listener {
	/**
	 * Turns slabs with the lore "Smooth double slab" into smooth double slab
	 * blocks and logs with the lore "6-sided log" into logs with the log
	 * texture on all 6 sides
	 * 
	 * @param e
	 */
	/*@EventHandler
	public void onSpecialBlockUse(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) {
			return;
		}
		Block b = e.getClickedBlock();
		if (b.getType() != Material.STEP && (b.getType() != Material.LOG)
				&& b.getType() != Material.LOG_2) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack is = p.getItemInHand();
		if (is == null || is.getType() != Material.STICK) {
			return;
		}
		Material material = b.getType();
		ItemMeta blockMeta = is.getItemMeta();
		byte type;
		switch (material) {
		case STEP:
			type = (byte) (b.getData() + 8);
			b.setTypeIdAndData(Material.DOUBLE_STEP.getId(), type, true);
			break;
		case LOG:
		case LOG_2:
			type = (byte) ((b.getData() % 4) + 12);
			b.setTypeIdAndData(material.getId(), type, true);
			break;
		default:
			return;
		}

	}
	*/
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		ClickableInventory ci = new ClickableInventory(54,"test");
		List <ItemStack> items = new LinkedList<ItemStack>();
		items.add(new ItemStack(Material.STONE));
		items.add(new ItemStack(Material.CLAY_BALL));
		items.add(new ItemStack(Material.BEDROCK));
		AnimatedClickable c = new AnimatedClickable(items, 20);
		ci.setSlot(c, 5);
		ci.showInventory(e.getPlayer());
	}
}

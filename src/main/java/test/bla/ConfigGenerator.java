package test.bla;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigGenerator {

	private File input;
	private File output;
	private List <ConvertionConfig> configs;

	public ConfigGenerator(File input, File output) {
		this.input = input;
		this.output = output;
		configs = new LinkedList<ConfigGenerator.ConvertionConfig>();
		configs.add(new ConvertionConfig(1, 7, 6, 18, 0.8));
		configs.add(new ConvertionConfig(1, 6, 5, 16, 0.7));
		configs.add(new ConvertionConfig(1, 5, 4, 14, 0.6));
		configs.add(new ConvertionConfig(1, 4, 3, 12, 0.5));
		configs.add(new ConvertionConfig(1, 3, 2, 10, 0.4));
		configs.add(new ConvertionConfig(1, 2, 1, 8, 0.3));
		configs.add(new ConvertionConfig(1, 1, 1, 5, 0.2));
		configs.add(new ConvertionConfig(3, 5, 6, 18, 0.8));
	}
	
	public class ConvertionConfig {
		int lowerBound;
		int upperBound;
		int megaLowerBound;
		int megaUpperBound;
		double miniMultiplier;
		
		public ConvertionConfig(int lowerBound, int upperBound, int megaLowerBound, int megaUpperBound, double miniMultiplier) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.megaLowerBound = megaLowerBound;
			this.megaUpperBound = megaUpperBound;
			this.miniMultiplier = miniMultiplier;
		}
		
		public boolean equals(Object o) {
			if (o instanceof ConvertionConfig) {
				return (((ConvertionConfig) o).lowerBound == lowerBound && ((ConvertionConfig) o).upperBound == upperBound);
			}
			return false;
		}
	}

	public void generate() {
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(input);
		ConfigurationSection blocks = config.getConfigurationSection("blocks");
		DecimalFormat df = new DecimalFormat("#.###############", new DecimalFormatSymbols(Locale.US));
		for (String key : blocks.getKeys(false)) {
			ConfigurationSection currentBlock = blocks.getConfigurationSection(key).getConfigurationSection("drops");
			System.out.println(key);
			Set <String> keys = currentBlock.getKeys(false);
			for (String oreKey : keys) {
				ConfigurationSection current = currentBlock.getConfigurationSection(oreKey);
				System.out.println(oreKey);
				@SuppressWarnings("unchecked")
				List<ItemStack> items = (List<ItemStack>) current.getList("package");
				ItemStack is = items.get(0);
				if (is.getType() == Material.FLINT) {
					// fossil
					continue;
				}
				System.out.println("Doing " + oreKey + " for " + key);
				//half rate
				double originalChance = current.getDouble("chance");
				current.set("chance", originalChance / 2 * 2);
				//copy current
				currentBlock.createSection(oreKey + "_micro", current.getValues(true));
				ConfigurationSection microSection = currentBlock.getConfigurationSection(oreKey + "_micro");
				ConvertionConfig convertConfig = getConfig(new ConvertionConfig(microSection.getInt("minAmount"), microSection.getInt("maxAmount"), 0, 0, 0.0));
				microSection.set("minAmount", 1);	
				microSection.set("maxAmount", 1);	
				int lowerBound = microSection.getInt("minY");
				int upperBound = microSection.getInt("maxY");
				int totalRange = Math.abs(upperBound - lowerBound);
				int increaseEach = (int) Math.ceil(((double) totalRange) / 20.0);
				int lowerIncreaseBonus = Math.max(upperBound - 255 + increaseEach, 0);
				int upperIncreaseBonus = Math.abs(Math.min(lowerBound - 1 - increaseEach, 0));
				int newLower = Math.max(1, lowerBound - (increaseEach + lowerIncreaseBonus));
				int newUpper = Math.min(255, upperBound + increaseEach + upperIncreaseBonus);
				microSection.set("minY", newLower);
				microSection.set("maxY", newUpper);
				microSection.set("chance", originalChance * convertConfig.miniMultiplier * 2);
				
				currentBlock.createSection(oreKey + "_mega", current.getValues(true));
				ConfigurationSection megaSection = currentBlock.getConfigurationSection(oreKey + "_mega");
				megaSection.set("minAmount", convertConfig.megaLowerBound);	
				megaSection.set("maxAmount", convertConfig.megaUpperBound);
				megaSection.set("chance", originalChance / 10 * 2);
				megaSection.set("transformMaxDropsIfFails", convertConfig.megaLowerBound);
			}
		}
		try {
			output.createNewFile();
			config.save(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ConvertionConfig getConfig(ConvertionConfig cc) {
		for(ConvertionConfig ccList : configs) {
			if (ccList.equals(cc)) {
				return ccList;
			}
		}
		return null;
	}

}

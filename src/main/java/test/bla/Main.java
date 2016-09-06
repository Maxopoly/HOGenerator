package test.bla;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.IBlockData;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.AsyncCatcher;

public class Main extends JavaPlugin {
    public static Main instance;

    public void onEnable() {
	instance = this;
	getServer().getPluginManager().registerEvents(new BlaListener(), this);

	File folder = new File("C:\\Users\\Max\\Desktop\\civ plugins\\HiddenOreConfigs\\convert\\");
	File [] files = folder.listFiles();
	for(File file : files) {
		if (!file.getName().endsWith(".j2")) {
			System.out.println("Skipping " + file.getName());
			continue;
		}
		File input = file;
		System.out.println("File " + input.getName());
		File outpuit = new File(file.getAbsolutePath() + ".generated");
		ConfigGenerator cg = new ConfigGenerator(input, outpuit);
		cg.generate();
	}
	
    }

    public void onDisable() {

    }
    

}

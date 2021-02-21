package com.GenZVirus.SoulmateMod.Init;

import com.GenZVirus.SoulmateMod.SoulmateMod;
import com.GenZVirus.SoulmateMod.Items.RoseItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulmateMod.MOD_ID);

	public static final RegistryObject<Item> ROSE = ITEMS.register("rose", ()-> new RoseItem(new Item.Properties().group(ItemGroup.MISC)));
	
}

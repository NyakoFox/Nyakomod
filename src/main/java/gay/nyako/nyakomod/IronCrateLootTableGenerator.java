package gay.nyako.nyakomod;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class IronCrateLootTableGenerator implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(NyakoLoot.IRON_CRATE_LOOT_TABLE,
                LootTable.builder().pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.COD).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.SALMON).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.TROPICAL_FISH).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.PUFFERFISH).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.BOW).weight(4).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.50f)))).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())
                                .with(ItemEntry.builder(Items.FISHING_ROD).weight(4).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.50f)))).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())
                                .with(ItemEntry.builder(Items.BOOK).weight(4).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(35.0f)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.NAUTILUS_SHELL).weight(6))
                                .with(ItemEntry.builder(Items.HEART_OF_THE_SEA).weight(2))
                                .with(ItemEntry.builder(NyakoItems.COPPER_COIN).weight(16).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.GOLD_COIN).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.EMERALD_COIN).weight(1))
                                .rolls(ConstantLootNumberProvider.create(6))
                                .bonusRolls(ConstantLootNumberProvider.create(2))
                )
        );
    }
}
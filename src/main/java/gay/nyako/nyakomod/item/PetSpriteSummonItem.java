package gay.nyako.nyakomod.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.Box;
import org.spongepowered.include.com.google.common.base.Predicate;

import java.util.UUID;

public class PetSpriteSummonItem extends TrinketItem {
    public PetSpriteSummonItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            var entities = entity.world.getEntitiesByType(NyakoMod.PET_SPRITE,
                    new Box(entity.getX() - 200, entity.getY() - 100, entity.getZ() - 100, entity.getX() + 100, entity.getY() + 100, entity.getZ() + 100),
                    (e) -> e.getOwnerUuid() != null && e.getOwnerUuid().equals(entity.getUuid()));

            if (entities.size() == 0) {
                var pet = new PetSpriteEntity(NyakoMod.PET_SPRITE, entity.world);
                pet.setOwnerUuid(entity.getUuid());
                pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
                pet.setInvulnerable(true);
                var nbt = stack.getOrCreateNbt();
                if (nbt.contains("custom_sprite")) {
                    pet.setCustomSprite(nbt.getString("custom_sprite"));
                }
                entity.world.spawnEntity(pet);
            } else if (entities.size() > 1) {
                for (var e : entities.subList(1, entities.size())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        super.tick(stack, slot, entity);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (player.isSneaking() && clickType == ClickType.RIGHT) {
            var nbt = stack.getOrCreateNbt();
            nbt.putString("custom_sprite", "https://men.are-pretty.sexy/9tk4DuJ.png");
            stack.setNbt(nbt);
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}
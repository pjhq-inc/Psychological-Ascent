package com.pjhq.psychologicalascent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = PsychologicalAscent.MODID)
public class OxygenHandler {
    
    private static final ResourceKey<Level> SPACE_DIMENSION = 
        ResourceKey.create(Registries.DIMENSION, 
            ResourceLocation.fromNamespaceAndPath(PsychologicalAscent.MODID, "space_dimension")); //this too is a bit silly because its not very scalable if thats the right word
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        handleOxygen(entity);
    }

    public static void handleOxygen(Entity entity) {
        if (!(entity instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        
        if (player.level().dimension() == SPACE_DIMENSION) {
            if (player.tickCount % 20 == 0) {
                int oxygen = player.getData(OxygenData.OXYGEN);
                if (oxygen > 0) {
                    player.setData(OxygenData.OXYGEN, oxygen - 1);
                } else {
                    player.hurt(player.damageSources().genericKill(), 1.0F);
                }
            }
        } else {
            player.setData(OxygenData.OXYGEN, 1000);
        }
    }
}

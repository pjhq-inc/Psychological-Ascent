package com.pjhq.psychologicalascent;

import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = PsychologicalAscent.MODID)
public class DimensionTeleportHandler {
    
    private static final ResourceKey<Level> SPACE_DIMENSION = 
        ResourceKey.create(Registries.DIMENSION, 
            ResourceLocation.fromNamespaceAndPath(PsychologicalAscent.MODID, "space_dimension"));
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        handleOxygen(entity);
        tpSpace(entity);
        tpOverworld(entity);
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

    public static void tpOverworld(Entity entity) {
        if (entity.level().isClientSide()) { return; }
        if (entity.getY() < 0 && entity.level().dimension() == SPACE_DIMENSION) {
            ServerLevel targetLevel = entity.getServer().getLevel(Level.OVERWORLD);
            if (targetLevel != null) {
                entity.teleportTo(
                    targetLevel,           
                    entity.getX(),
                    100.0,
                    entity.getZ(),
                    Set.of(),
                    entity.getYRot(),
                    entity.getXRot(),
                    true
                );
            }
        }
    }

    public static void tpSpace(Entity entity) {
        if (entity.level().isClientSide()) { return; }
        if (entity.getY() > 1500 && entity.level().dimension() != SPACE_DIMENSION) {
            ServerLevel targetLevel = entity.getServer().getLevel(SPACE_DIMENSION);
            if (targetLevel != null) {
                entity.teleportTo(
                    targetLevel,           
                    entity.getX(),
                    1400.0,
                    entity.getZ(),
                    Set.of(),
                    entity.getYRot(),
                    entity.getXRot(),
                    true
                );
            }
        }
    }
}

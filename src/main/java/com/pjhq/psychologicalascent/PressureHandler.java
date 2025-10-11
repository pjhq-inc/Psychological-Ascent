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
public class PressureHandler {
    
    private static final ResourceKey<Level> SPACE_DIMENSION = 
        ResourceKey.create(Registries.DIMENSION, 
            ResourceLocation.fromNamespaceAndPath(PsychologicalAscent.MODID, "space_dimension"));
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        handlePressure(entity);
    }

    public static void handlePressure(Entity entity) {
        if (!(entity instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        
        int basePressure = getBasePressure(player.level().dimension());
        double yLevel = player.getY();
        int pressure = calculatePressure(basePressure, yLevel);
        
        player.setData(PressureData.PRESSURE, pressure);
        
        if (player.tickCount % 20 == 0) {
            if (pressure < 50) {
                player.hurt(player.damageSources().genericKill(), 0.5F);
            } else if (pressure > 150) {
                player.hurt(player.damageSources().genericKill(), 0.5F);
            }
        }
    }

    private static int getBasePressure(ResourceKey<Level> dimension) {
        if (dimension == Level.OVERWORLD) {
            return Config.OVERWORLD_BASE_PRESSURE.get();
        } else if (dimension == SPACE_DIMENSION) {
            return Config.SPACE_BASE_PRESSURE.get();
        } else if (dimension == Level.NETHER) {
            return Config.NETHER_BASE_PRESSURE.get();
        } else if (dimension == Level.END) {
            return Config.END_BASE_PRESSURE.get(); //this is a bit silly
        }
        return 100;
    }

    private static int calculatePressure(int basePressure, double yLevel) {
        int pressure = basePressure;
        if (yLevel < 63) {
            pressure += (int)((63 - yLevel) * 0.1); //REALISTIC!!!!!!!!!!!!!!!!!
        } else if (yLevel > 63) {
            pressure -= (int)((yLevel - 63) * 0.012);
        }
        return Math.max(0, Math.min(1000, pressure));
    }
}

package com.pjhq.psychologicalascent;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class PressureData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PsychologicalAscent.MODID);
    public static final Supplier<AttachmentType<Integer>> PRESSURE = ATTACHMENT_TYPES.register("pressure", () -> AttachmentType.builder(() -> 100).build());
}

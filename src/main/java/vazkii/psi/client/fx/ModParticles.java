package vazkii.psi.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.psi.common.lib.LibMisc;

//https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/ModParticles.java
@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    @ObjectHolder(LibMisc.MOD_ID + ":wisp")
    public static ParticleType<WispParticleData> WISP;

    @ObjectHolder(LibMisc.MOD_ID + ":sparkle")
    public static ParticleType<SparkleParticleData> SPARKLE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        register(evt.getRegistry(), new WispParticleType(), "wisp");
        register(evt.getRegistry(), new SparkleParticleType(), "sparkle");
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {
        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent evt) {
            Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, WispParticleType.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
        }
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        register(reg, thing, new ResourceLocation(LibMisc.MOD_ID, name));
    }
}

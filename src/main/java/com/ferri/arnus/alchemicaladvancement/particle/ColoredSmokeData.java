package com.ferri.arnus.alchemicaladvancement.particle;

import java.util.Locale;

import javax.annotation.Nullable;

import com.ferri.arnus.alchemicaladvancement.AlchemicalAdvancement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ObjectHolder;

public class ColoredSmokeData implements ParticleOptions
{
    @ObjectHolder(AlchemicalAdvancement.MODID + ":colored_smoke")
    public static ParticleType<ColoredSmokeData> TYPE = null;

    public static Codec<ColoredSmokeData> CODEC = RecordCodecBuilder
            .create((instance) -> instance.group(
                    Codec.FLOAT.fieldOf("red").forGetter(i -> i.red),
                    Codec.FLOAT.fieldOf("green").forGetter(i -> i.green),
                    Codec.FLOAT.fieldOf("blue").forGetter(i -> i.blue)
            ).apply(instance, ColoredSmokeData::new));

    public final float red;
    public final float green;
    public final float blue;

    private ColoredSmokeData(float red, float green, float blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<?> getType()
    {
        return TYPE;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(red);
        buffer.writeFloat(green);
        buffer.writeFloat(blue);
    }

    @Override
    public String writeToString()
    {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", this.getType().getRegistryName(), this.red, this.green, this.blue);
    }

    @Deprecated
    public static final Deserializer<ColoredSmokeData> DESERIALIZER = new Deserializer<ColoredSmokeData>()
    {
        @Override
        public ColoredSmokeData fromCommand(ParticleType<ColoredSmokeData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            return new ColoredSmokeData(r, g, b);
        }

        @Override
        public ColoredSmokeData fromNetwork(ParticleType<ColoredSmokeData> particleTypeIn, FriendlyByteBuf buffer)
        {
            return new ColoredSmokeData(
                    buffer.readFloat(),
                    buffer.readFloat(),
                    buffer.readFloat()
            );
        }
    };

    public static ColoredSmokeData withColor(float red, float green, float blue)
    {
        return new ColoredSmokeData(red, green, blue);
    }

    

    public static class Factory implements ParticleProvider<ColoredSmokeData>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet p_i51045_1_)
        {
            this.spriteSet = p_i51045_1_;
        }

        @Nullable
        @Override
        public Particle createParticle(ColoredSmokeData colorData, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new SmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.spriteSet)
            {
                {
                    setColor(colorData.red, colorData.green, colorData.blue);
                }
            };
        }
    }

    public static class Type extends ParticleType<ColoredSmokeData>
    {
        public Type(boolean alwaysShow)
        {
            super(alwaysShow, ColoredSmokeData.DESERIALIZER);
        }

        @Override
        public Codec<ColoredSmokeData> codec()
        {
            return CODEC;
        }
    }
}

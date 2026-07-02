package com.silent.createwingsplus.block.entity;

import com.simibubi.create.Create;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.compat.computercraft.events.KineticsChangeEvent;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.minecraft.ChatFormatting.GOLD;
import static net.minecraft.ChatFormatting.GRAY;

public class SailShaftStraightEntity extends KineticBlockEntity {
    public @Nullable Long network;
    public @Nullable BlockPos source;
    public boolean networkDirty;
    public boolean updateSpeed;
    public int preventSpeedUpdate;

    protected KineticEffectHandler effects;
    protected float speed;
    protected float capacity;
    protected float stress;
    protected boolean overStressed;
    protected boolean wasMoved;

    private int flickerTally;
    private int networkSize;
    private int validationCountdown;
    protected float lastStressApplied;
    protected float lastCapacityProvided;

    public SequencedGearshiftBlockEntity.SequenceContext sequenceContext;

    public SailShaftStraightEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SAIL_SHAFT_STRAIGHT_BE.get(), pos, state);
        effects = new KineticEffectHandler(this);
        updateSpeed = true;
    }

    @Override
    public void initialize() {
        if (hasNetwork() && !level.isClientSide) {
            KineticNetwork network = getOrCreateNetwork();
            if (!network.initialized)
                network.initFromTE(capacity, stress, networkSize);
            network.addSilently(this, lastCapacityProvided, lastStressApplied);
        }

        super.initialize();
    }

    @Override
    public void tick() {
        if (!level.isClientSide && needsSpeedUpdate())
            attachKinetics();

        super.tick();
        effects.tick();

        preventSpeedUpdate = 0;

        if (level.isClientSide) {
            CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> this.tickAudio());
            return;
        }

        if (validationCountdown-- <= 0) {
            validationCountdown = AllConfigs.server().kinetics.kineticValidationFrequency.get();
        }

        if (getFlickerScore() > 0)
            flickerTally = getFlickerScore() - 1;

        if (networkDirty) {
            if (hasNetwork())
                getOrCreateNetwork().updateNetwork();
            networkDirty = false;
        }
    }

    @Override
    public void remove() {
        if (!level.isClientSide) {
            if (hasNetwork())
                getOrCreateNetwork().remove(this);
            detachKinetics();
        }
        super.remove();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putFloat("Speed", speed);
        if (sequenceContext != null && (!clientPacket || syncSequenceContext()))
            compound.put("Sequence", sequenceContext.serializeNBT());

        if (needsSpeedUpdate())
            compound.putBoolean("NeedsSpeedUpdate", true);

        if (hasSource())
            compound.put("Source", NbtUtils.writeBlockPos(source));

        if (hasNetwork()) {
            CompoundTag networkTag = new CompoundTag();
            networkTag.putLong("Id", this.network);
            networkTag.putFloat("Stress", stress);
            networkTag.putFloat("Capacity", capacity);
            networkTag.putInt("Size", networkSize);

            if (lastStressApplied != 0)
                networkTag.putFloat("AddedStress", lastStressApplied);
            if (lastCapacityProvided != 0)
                networkTag.putFloat("AddedCapacity", lastCapacityProvided);

            compound.put("Network", networkTag);
        }

        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        boolean overStressedBefore = overStressed;
        clearKineticInformation();

        // DO NOT READ kinetic information when placed after movement
        if (wasMoved) {
            super.read(compound, registries, clientPacket);
            return;
        }

        speed = compound.getFloat("Speed");
        sequenceContext = SequencedGearshiftBlockEntity.SequenceContext.fromNBT(compound.getCompound("Sequence"));

        source = null;
        if (compound.contains("Source"))
            source = NBTHelper.readBlockPos(compound, "Source");

        if (compound.contains("Network")) {
            CompoundTag networkTag = compound.getCompound("Network");
            network = networkTag.getLong("Id");
            stress = networkTag.getFloat("Stress");
            capacity = networkTag.getFloat("Capacity");
            networkSize = networkTag.getInt("Size");
            lastStressApplied = networkTag.getFloat("AddedStress");
            lastCapacityProvided = networkTag.getFloat("AddedCapacity");
            overStressed = capacity < stress && IRotate.StressImpact.isEnabled();
        }

        super.read(compound, registries, clientPacket);

        if (clientPacket && overStressedBefore != overStressed && speed != 0)
            effects.triggerOverStressedEffect();

        if (clientPacket)
            CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> VisualizationHelper.queueUpdate(this));
    }
}

package com.dandy.moblocks.item.custom;

import com.dandy.moblocks.block.ModBlocks;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.Map;

public class ChiselItem extends Item {
    private static final Map<Block, Block> CHISEL_MAP =
            Map.ofEntries(
                Map.entry(Blocks.COBBLESTONE, ModBlocks.COBBLESTONE_BRICKS.get()),
                Map.entry(ModBlocks.COBBLESTONE_BRICKS.get(), Blocks.STONE),
                Map.entry(Blocks.STONE, Blocks.STONE_BRICKS),
                Map.entry(Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS),

                Map.entry(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE),
                Map.entry(Blocks.DEEPSLATE, Blocks.DEEPSLATE_BRICKS),
                Map.entry(Blocks.DEEPSLATE_BRICKS, Blocks.CHISELED_DEEPSLATE),

                Map.entry(Blocks.SANDSTONE, Blocks.CUT_SANDSTONE),
                Map.entry(Blocks.CUT_SANDSTONE, Blocks.SMOOTH_SANDSTONE),
                Map.entry(Blocks.SMOOTH_SANDSTONE, Blocks.CHISELED_SANDSTONE),
                Map.entry(Blocks.RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE),
                Map.entry(Blocks.CUT_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE),
                Map.entry(Blocks.SMOOTH_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE),

                Map.entry(Blocks.END_STONE, Blocks.END_STONE_BRICKS),

                Map.entry(Blocks.GRASS_BLOCK, Blocks.DIRT),
                Map.entry(Blocks.PACKED_MUD, Blocks.MUD_BRICKS),

                Map.entry(Blocks.GRANITE, Blocks.POLISHED_GRANITE),
                Map.entry(Blocks.DIORITE, Blocks.POLISHED_DIORITE),
                Map.entry(Blocks.ANDESITE, Blocks.POLISHED_ANDESITE)
            );

    public ChiselItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();
        InteractionResult result = InteractionResult.PASS;

        if(CHISEL_MAP.containsKey(clickedBlock)) {
            if(!level.isClientSide()) {
                level.setBlockAndUpdate(pContext.getClickedPos(), CHISEL_MAP.get(clickedBlock).defaultBlockState());

                pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) pContext.getPlayer()),
                    item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                // Get soundType from block that will replace the current block
                SoundType blockSound = CHISEL_MAP.get(clickedBlock).getSoundType(clickedBlock.defaultBlockState(), level, pContext.getClickedPos(), null);
                level.playSound(null, pContext.getClickedPos(), blockSound.getPlaceSound(), SoundSource.BLOCKS);

                // Create particles
                ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, clickedBlock.defaultBlockState()),
                        pContext.getClickLocation().x, pContext.getClickLocation().y, pContext.getClickLocation().z, 10, 0, 0, 0, 2);

                result = InteractionResult.SUCCESS;
            }
        }

        return result;
    }
}

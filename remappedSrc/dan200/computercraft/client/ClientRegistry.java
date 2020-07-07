/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2019. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.client;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.util.Colour;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Registers textures and models for items.
 */
public final class ClientRegistry
{
    private static final String[] EXTRA_MODELS = new String[] {
        "turtle_modem_normal_off_left",
        "turtle_modem_normal_on_left",
        "turtle_modem_normal_off_right",
        "turtle_modem_normal_on_right",

        "turtle_modem_advanced_off_left",
        "turtle_modem_advanced_on_left",
        "turtle_modem_advanced_off_right",
        "turtle_modem_advanced_on_right",
        "turtle_crafting_table_left",
        "turtle_crafting_table_right",

        "turtle_speaker_upgrade_left",
        "turtle_speaker_upgrade_right",

        "turtle_colour",
        "turtle_elf_overlay",
    };

    private static final String[] EXTRA_TEXTURES = new String[] {
        // TODO: Gather these automatically from the model. I'm unable to get this working with Forge's current
        //  model loading code.
        "block/turtle_colour",
        "block/turtle_elf_overlay",
        "block/turtle_crafty_face",
        "block/turtle_speaker_face",
    };

    private ClientRegistry() {}

    public static void onTextureStitchEvent( SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry )
    {
        for( String extra : EXTRA_TEXTURES )
        {
            registry.register( new Identifier( ComputerCraft.MOD_ID, extra ) );
        }
    }

    public static void onModelBakeEvent( ResourceManager manager, Consumer<ModelIdentifier> out )
    {
        for( String model : EXTRA_MODELS )
        {
            out.accept( new ModelIdentifier( new Identifier( ComputerCraft.MOD_ID, model ), "inventory" ) );
        }
    }

    public static void onItemColours()
    {
        ColorProviderRegistry.ITEM.register(
            ( stack, layer ) -> layer == 1 ? ((ItemDisk) stack.getItem()).getColour( stack ) : 0xFFFFFF,
            ComputerCraft.Items.disk
        );

        ColorProviderRegistry.ITEM.register( ( stack, layer ) -> {
            switch( layer )
            {
                case 0:
                default:
                    return 0xFFFFFF;
                case 1: // Frame colour
                    return IColouredItem.getColourBasic( stack );
                case 2: // Light colour
                {
                    int light = ItemPocketComputer.getLightState( stack );
                    return light == -1 ? Colour.Black.getHex() : light;
                }
            }
        }, ComputerCraft.Items.pocketComputerNormal, ComputerCraft.Items.pocketComputerAdvanced );

        // Setup turtle colours
        ColorProviderRegistry.ITEM.register(
            ( stack, tintIndex ) -> tintIndex == 0 ? ((IColouredItem) stack.getItem()).getColour( stack ) : 0xFFFFFF,
            ComputerCraft.Blocks.turtleNormal, ComputerCraft.Blocks.turtleAdvanced
        );
    }

    private static BakedModel bake( ModelLoader loader, UnbakedModel model )
    {
        model.getTextureDependencies( loader::getOrLoadModel, new HashSet<>() );
        SpriteAtlasTexture sprite = MinecraftClient.getInstance().getSpriteAtlas();
        return model.bake( loader, sprite::getSprite, ModelRotation.X0_Y0 );
    }
}

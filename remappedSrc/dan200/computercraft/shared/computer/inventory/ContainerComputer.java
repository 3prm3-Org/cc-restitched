/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2019. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.shared.computer.inventory;

import dan200.computercraft.shared.computer.blocks.TileComputer;
import dan200.computercraft.shared.computer.core.IComputer;
import dan200.computercraft.shared.computer.core.IContainerComputer;
import dan200.computercraft.shared.computer.core.InputState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerComputer extends ScreenHandler implements IContainerComputer
{
    private final TileComputer computer;
    private final InputState input = new InputState( this );

    public ContainerComputer( int id, TileComputer computer )
    {
        super( null, id );
        this.computer = computer;
    }

    @Override
    public boolean canUse( @Nonnull PlayerEntity player )
    {
        return computer.isUsableByPlayer( player );
    }

    @Nullable
    @Override
    public IComputer getComputer()
    {
        return computer.getServerComputer();
    }

    @Nonnull
    @Override
    public InputState getInput()
    {
        return input;
    }

    @Override
    public void close( PlayerEntity player )
    {
        super.close( player );
        input.close();
    }
}

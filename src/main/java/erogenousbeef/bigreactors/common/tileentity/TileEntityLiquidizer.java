package erogenousbeef.bigreactors.common.tileentity;

import cofh.core.util.oredict.OreDictionaryArbiter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erogenousbeef.bigreactors.api.registry.Reactants;
import erogenousbeef.bigreactors.client.gui.GuiCyaniteReprocessor;
import erogenousbeef.bigreactors.client.gui.GuiLiquidizer;
import erogenousbeef.bigreactors.common.tileentity.base.TileEntityPoweredInventoryFluid;
import erogenousbeef.bigreactors.gui.container.ContainerCyaniteReprocessor;
import erogenousbeef.bigreactors.gui.container.ContainerLiquidizer;
import erogenousbeef.bigreactors.utils.StaticUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class TileEntityLiquidizer extends TileEntityPoweredInventoryFluid {

    public static final int SLOT_INLET_1 = 0;
    /*public static final int SLOT_INLET_2 = 1;*/
    public static final int SLOT_OUTLET = 1;
    public static final int NUM_SLOTS = 2;

    public static final int FLUIDTANK_WATER = 0;
    public static final int NUM_TANKS = 2;

    protected static final int FLUID_CONSUMED = FluidContainerRegistry.BUCKET_VOLUME * 1;
    protected static final int INGOTS_CONSUMED = 2;

    protected static final int MAX_ENERGY_STORE = 10000;
    protected static final int CYCLE_ENERGY_COST = 2000;

    public TileEntityLiquidizer() {
        super();

        // Do not transmit energy from the internal buffer.
        m_ProvidesEnergy = false;
    }

    /*int calc = 0;

    public void calculate() {
        ItemStack stack = getStackInSlot(SLOT_INLET_1);
        if(stack != null && isItemValidForSlot(SLOT_INLET_1, stack)) {
            calc += stack.stackSize;
        }

    }*/

    @Override
    public int getSizeInventory() {
        return NUM_SLOTS;
    }

    @Override
    protected int getMaxEnergyStored() {
        return MAX_ENERGY_STORE;
    }

    @Override
    public int getCycleEnergyCost() {
        return CYCLE_ENERGY_COST;
    }

    @Override
    public int getCycleLength() {
        return 200; // 10 seconds / 20tps;
    }

    @Override
    public boolean canBeginCycle() {
        FluidStack fluid = drain(0, FLUID_CONSUMED, false);
        if(fluid == null || fluid.amount < FLUID_CONSUMED) {
            return false;
        }

        if(_inventories[SLOT_INLET_1] != null && _inventories[SLOT_INLET_1].stackSize >= INGOTS_CONSUMED) {
            /*if (_inventories[SLOT_INLET_2] != null && _inventories[SLOT_INLET_2].stackSize >= INGOTS_CONSUMED) {*/
                if (_inventories[SLOT_OUTLET] != null && _inventories[SLOT_OUTLET].stackSize >= getInventoryStackLimit()) {
                    return false;
                }
            /*} */
            return true;
        }

        return false;
    }

    @Override
    public String getInventoryName() {
        return "Liquidizer";
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if(itemstack == null) { return true; }

        if(slot == SLOT_OUTLET) {
            return Reactants.isFuel(itemstack);
        }
        else if(slot == SLOT_INLET_1 /* || slot == SLOT_INLET_2 */) {
            return Reactants.isWaste(itemstack);
        }

        return false;
    }

    @Override
    protected int getExposedInventorySlotFromSide(int side) {
        return 0;
    }



    @Override
    public void onPoweredCycleBegin() {

    }

    @Override
    public void onPoweredCycleEnd() {
        if(_inventories[SLOT_OUTLET] != null) {
            if(consumeInputs()) {
                _inventories[SLOT_OUTLET].stackSize += 1;
            }
        }
        else {
            // TODO: Make this query the input for the right type of output to create.
            ArrayList<ItemStack> candidates = OreDictionaryArbiter.getOres("ingotBlutonium");
            if(candidates == null || candidates.isEmpty()) {
                // WTF?
                return;
            }

            if(consumeInputs()) {
                _inventories[SLOT_OUTLET] = candidates.get(0).copy();
                _inventories[SLOT_OUTLET].stackSize = 1;
            }
        }

        distributeItemsFromSlot(SLOT_OUTLET);
        markChunkDirty();
    }

    private boolean consumeInputs() {
        _inventories[SLOT_INLET_1] = StaticUtils.Inventory.consumeItem(_inventories[SLOT_INLET_1], INGOTS_CONSUMED);
        drain(0, FLUID_CONSUMED, true);

        return true;
    }

    @Override
    public int getNumTanks() {
        return NUM_TANKS;
    }

    @Override
    public int getTankSize(int tankIndex) {
        return FluidContainerRegistry.BUCKET_VOLUME * 5;
    }

    @Override
    public int getExposedTankFromSide(int side) {
        return 0;
    }

    @Override
    protected boolean isFluidValidForTank(int tankIdx, FluidStack type) {
        if(type == null) { return false; }
        return type.getFluid().getID() == FluidRegistry.getFluid("water").getID();
    }

    /// BeefGUI
    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGUI(EntityPlayer player) {
        return new GuiLiquidizer(getContainer(player), this);
    }

    @Override
    public Container getContainer(EntityPlayer player) {
        return new ContainerLiquidizer(this, player);
    }

    @Override
    protected int getDefaultTankForFluid(Fluid fluid) {
        if(fluid.getName() == "water")
            return 0;
        else
            return FLUIDTANK_NONE;
    }

    @Override
    public IIcon getIconForSide(int referenceSide) {
        return null;
    }

    @Override
    public int getNumConfig(int i) {
        return 0;
    }
}
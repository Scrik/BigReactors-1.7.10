package erogenousbeef.bigreactors.utils;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public final class LangUtils
{
    public static String transOnOff(boolean b)
    {
        return LangUtils.localize("gui." + (b ? "on" : "off"));
    }

    public static String transYesNo(boolean b)
    {
        return LangUtils.localize("tooltip." + (b ? "yes" : "no"));
    }

    public static String transOutputInput(boolean b)
    {
        return LangUtils.localize("gui." + (b ? "output" : "input"));
    }

    public static String localizeFluidStack(FluidStack fluidStack)
    {
        return (fluidStack == null || fluidStack.getFluid() == null ) ? null : fluidStack.getFluid().getLocalizedName(fluidStack);
    }

    /**
     * Localizes the defined string.
     * @param s - string to localized
     * @return localized string
     */
    public static String localize(String s)
    {
        return StatCollector.translateToLocal(s);
    }
}
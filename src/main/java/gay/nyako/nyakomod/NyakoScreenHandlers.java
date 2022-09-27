package gay.nyako.nyakomod;

import gay.nyako.nyakomod.screens.BlueprintWorkbenchScreenHandler;
import gay.nyako.nyakomod.screens.CunkShopScreenHandler;
import gay.nyako.nyakomod.screens.IconScreenHandler;
import gay.nyako.nyakomod.screens.NBPScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoScreenHandlers {
    public static final ScreenHandlerType<IconScreenHandler> ICON_SCREEN_HANDLER_TYPE = register("icon_menu", new ExtendedScreenHandlerType<>(IconScreenHandler::new));
    public static final ScreenHandlerType<CunkShopScreenHandler> CUNK_SHOP_SCREEN_HANDLER_TYPE = register("cunk_shop", new ExtendedScreenHandlerType<>(CunkShopScreenHandler::new));
    public static final ScreenHandlerType<BlueprintWorkbenchScreenHandler> BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE = register("blueprint_workbench", new ScreenHandlerType<>(BlueprintWorkbenchScreenHandler::new));
    public static final ScreenHandlerType<NBPScreenHandler> NBP_SCREEN_HANDLER_TYPE = register("note_block_plus", new ExtendedScreenHandlerType<>(NBPScreenHandler::new));

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier("nyakomod", id), screenHandlerType);
    }
}

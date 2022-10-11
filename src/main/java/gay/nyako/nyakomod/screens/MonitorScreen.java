package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoNetworking;
import gay.nyako.nyakomod.entity.MonitorEntity;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MonitorScreen extends BaseUIModelScreen<FlowLayout> {
    MonitorEntity monitorEntity;

    public MonitorScreen(MonitorEntity monitorEntity) {
        super(FlowLayout.class, DataSource.asset(new Identifier("nyakomod", "monitor_input")));

        this.monitorEntity = monitorEntity;
    }


    @Override
    protected void build(FlowLayout rootComponent) {
        var submitButton = rootComponent.childById(ButtonWidget.class, "submit");
        var textElement = rootComponent.childById(TextFieldWidget.class, "url-box");
        var widthElement = rootComponent.childById(TextFieldWidget.class, "width-box");
        var heightElement = rootComponent.childById(TextFieldWidget.class, "height-box");

        var leftButton = rootComponent.childById(ButtonWidget.class, "left");
        var rightButton = rootComponent.childById(ButtonWidget.class, "right");
        var upButton = rootComponent.childById(ButtonWidget.class, "up");
        var downButton = rootComponent.childById(ButtonWidget.class, "down");

        textElement.setMaxLength(250);
        textElement.setText(monitorEntity.getURL());
        widthElement.setText(String.valueOf(monitorEntity.getMonitorWidth()));
        heightElement.setText(String.valueOf(monitorEntity.getMonitorHeight()));

        leftButton.onPress(button -> {
            var buf = PacketByteBufs.create();
            buf.writeUuid(monitorEntity.getUuid());
            buf.writeDouble(-1f);
            buf.writeDouble(0);
            buf.writeDouble(0);
            ClientPlayNetworking.send(NyakoNetworking.MONITOR_MOVE, buf);
        });

        rightButton.onPress(button -> {
            var buf = PacketByteBufs.create();
            buf.writeUuid(monitorEntity.getUuid());
            buf.writeDouble(1f);
            buf.writeDouble(0);
            buf.writeDouble(0);
            ClientPlayNetworking.send(NyakoNetworking.MONITOR_MOVE, buf);
        });

        upButton.onPress(button -> {
            var buf = PacketByteBufs.create();
            buf.writeUuid(monitorEntity.getUuid());
            buf.writeDouble(0);
            buf.writeDouble(1f);
            buf.writeDouble(0);
            ClientPlayNetworking.send(NyakoNetworking.MONITOR_MOVE, buf);
        });

        downButton.onPress(button -> {
            var buf = PacketByteBufs.create();
            buf.writeUuid(monitorEntity.getUuid());
            buf.writeDouble(0);
            buf.writeDouble(-1f);
            buf.writeDouble(0);
            ClientPlayNetworking.send(NyakoNetworking.MONITOR_MOVE, buf);
        });

        assert submitButton != null;
        submitButton.onPress(button -> {
            var value = textElement.getText();
            if (value.startsWith("https://") && value.endsWith(".png")) {
                //monitorEntity.setURL(textElement.getText());
                var buf = PacketByteBufs.create();
                buf.writeString(textElement.getText());
                buf.writeUuid(monitorEntity.getUuid());

                try {
                    buf.writeInt(Integer.parseInt(widthElement.getText()));
                } catch (Exception e) {
                    buf.writeInt(1);
                }

                try {
                    buf.writeInt(Integer.parseInt(heightElement.getText()));
                } catch (Exception e) {
                    buf.writeInt(1);
                }

                ClientPlayNetworking.send(NyakoNetworking.MONITOR_SET_URL, buf);

                MinecraftClient.getInstance().setScreen(null);
            }
        });
    }
}

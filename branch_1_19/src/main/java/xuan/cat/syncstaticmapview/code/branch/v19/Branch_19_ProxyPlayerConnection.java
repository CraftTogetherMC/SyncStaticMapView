package xuan.cat.syncstaticmapview.code.branch.v19;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.packet.PacketSpawnEntityEvent;

import java.lang.reflect.Field;

public final class Branch_19_ProxyPlayerConnection {
    public static boolean read(Player player, Packet<?> packet) {
        return true;
    }


    private static Field field_PacketPlayOutEntityMetadata_entityId;

    static {
        try {
            field_PacketPlayOutEntityMetadata_entityId = PacketPlayOutEntityMetadata.class.getDeclaredField("b");
            field_PacketPlayOutEntityMetadata_entityId.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static private int getRelevantID(Packet<?> packet) throws IllegalAccessException {
        if (packet instanceof PacketPlayOutEntityMetadata)
            return field_PacketPlayOutEntityMetadata_entityId.getInt(packet);
        return -1;
    }

    public static boolean write(Player player, Packet<?> packet) {
        try {
            int entityID = getRelevantID(packet);
            if (entityID > 0) {
                PacketSpawnEntityEvent event = new PacketSpawnEntityEvent(player, entityID);
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}

package xuan.cat.syncstaticmapview.code.branch.v17;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapData;

public final class Branch_17_Packet implements BranchPacket {
    @Override
    public int readEntityIdSpawn(PacketContainer container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMapView(Player player, int mapId, MapData mapData) {
        throw new UnsupportedOperationException();
    }
}

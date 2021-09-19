package xuan.cat.syncstaticmapview.code.branch;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CodeBranchMinecraft implements BranchMinecraft {
    private final Field field_CraftItemStack_handle;
    private final Field field_ItemStack_tag;


   public CodeBranchMinecraft() throws NoSuchFieldException {
       field_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle");
       field_ItemStack_tag = net.minecraft.world.item.ItemStack.class.getDeclaredField("u");
       field_CraftItemStack_handle.setAccessible(true);
       field_ItemStack_tag.setAccessible(true);
   }


    public Entity getEntityFromId(World world, int entityId) {
        net.minecraft.world.entity.Entity entity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return entity != null ? entity.getBukkitEntity() : null;
    }


    public int getMapId(ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            net.minecraft.world.item.ItemStack itemNMS = (net.minecraft.world.item.ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            return nbt.getInt("map");
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }
    public ItemStack setMapId(ItemStack item, int mapId) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            net.minecraft.world.item.ItemStack itemNMS = (net.minecraft.world.item.ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            if (nbt == null) {
                nbt = new NBTTagCompound();
                field_ItemStack_tag.set(itemNMS, nbt);
            }
            nbt.setInt("map", mapId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return craftItem;
    }


    public List<Player> getTracking(Entity entity) {
        WorldServer worldServer = ((CraftWorld) entity.getWorld()).getHandle();
        List<Player> playerList = new ArrayList<>();
        ChunkProviderServer providerServer = worldServer.getChunkProvider();
        PlayerChunkMap playerChunkMap = providerServer.a;
        Int2ObjectMap<PlayerChunkMap.EntityTracker> entityTrackerMap = playerChunkMap.G;
        PlayerChunkMap.EntityTracker entityTracker = entityTrackerMap.get(entity.getEntityId());
        if (entityTracker != null) {
            Set<ServerPlayerConnection> trackedPlayers = entityTracker.f;
            if (trackedPlayers != null) {
                for (ServerPlayerConnection connection : trackedPlayers) {
                    EntityPlayer entityPlayer = connection.d();
                    playerList.add(entityPlayer.getBukkitEntity());
                }
            }
        }
        return playerList;
    }
}

package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class FileChannelDisk<S extends Sector> implements Disk<S> {

    protected List<Sector> sectors;
    protected Map<String, Sector> sectorsMap;

    public FileChannelDisk() {
        this.sectors = new ArrayList<Sector>();
        this.sectorsMap = new HashMap<String, Sector>();
    }

    public S getSectorAt(int index) {
        return (S) sectors.get(index);
    }

    public S getSector(String key) {
        return (S) sectorsMap.get(key);
    }


    public FileChannelDisk<S> mapTo(FileChannel channel) {
        try {
            int channelCursor = 0;
            for (Sector sector : sectors) {
                channelCursor += sector.mapTo(channel.map(FileChannel.MapMode.READ_ONLY, channelCursor, sector.getLengthInBytes())).getLengthInBytes();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public FileChannelDisk<S> mapTo(String filePath) {
        int size = 0;
        for (Sector sector : sectors) {
            size += sector.getLengthInBytes();
        }

        try {
            RandomAccessFile f = new RandomAccessFile(filePath, "rw");
            f.setLength(size);
            FileChannel channel = f.getChannel();

            int channelCursor = 0;
            for (Sector sector : sectors) {
                channelCursor += sector.mapTo(channel.map(FileChannel.MapMode.READ_WRITE, channelCursor, sector.getLengthInBytes())).getLengthInBytes();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }


    public FileChannelDisk<S> addSector(String name, S sector) {
        sectors.add(sector);
        if (name != null)
            sectorsMap.put(name, sector);

        return this;
    }

}

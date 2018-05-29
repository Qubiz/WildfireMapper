package robor.wildfiremapper.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Mathijs de Groot on 29/05/2018.
 */
@Entity(nameInDb = "ble_device")
public class BleDevice {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "mac_address")
    private String macAddress;

    @Property(nameInDb = "connected")
    private boolean connected;

    @Generated(hash = 1192054326)
    public BleDevice(Long id, String name, String macAddress, boolean connected) {
        this.id = id;
        this.name = name;
        this.macAddress = macAddress;
        this.connected = connected;
    }

    @Generated(hash = 1527739491)
    public BleDevice() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean getConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}

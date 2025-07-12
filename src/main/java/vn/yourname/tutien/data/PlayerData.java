package vn.yourname.tutien.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private double totalLinhKhi;
    private CanhGioi canhGioi;
    private int tieuCanhGioi;
    private LinhCan linhCan;
    private boolean tuViHidden = false;
    private final Map<UUID, String> marksPlaced = new HashMap<>();
    private UUID markedBy = null;

    public PlayerData() {
        this.totalLinhKhi = 0;
        this.linhCan = null;
        updateRealmData();
    }

    public void updateRealmData() {
        CanhGioi.RealmData realmData = CanhGioi.getRealmDataFor(this.totalLinhKhi);
        this.canhGioi = realmData.canhGioi;
        this.tieuCanhGioi = realmData.tieuCanhGioi;
    }

    public void setTotalLinhKhi(double totalLinhKhi) {
        this.totalLinhKhi = totalLinhKhi;
        updateRealmData();
    }
    public void addLinhKhi(double amount) {
        setTotalLinhKhi(this.totalLinhKhi + amount);
    }

    // Getters and Setters
    public double getTotalLinhKhi() { return totalLinhKhi; }
    public CanhGioi getCanhGioi() { return canhGioi; }
    public int getTieuCanhGioi() { return tieuCanhGioi; }
    public LinhCan getLinhCan() { return linhCan; }
    public void setLinhCan(LinhCan linhCan) { this.linhCan = linhCan; }
    public boolean isTuViHidden() { return tuViHidden; }
    public void setTuViHidden(boolean hidden) { this.tuViHidden = hidden; }
    public Map<UUID, String> getMarksPlaced() { return marksPlaced; }
    public UUID getMarkedBy() { return markedBy; }
    public void setMarkedBy(UUID uuid) { this.markedBy = uuid; }
    public int getMaxMarks() { return canhGioi == null ? 0 : Math.max(0, canhGioi.ordinal()); }
}

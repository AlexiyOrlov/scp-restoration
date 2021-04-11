package dev.buildtool.scp.human;

import java.util.UUID;

public class SetOwner {
    public int eid;
    public UUID ownerUUid;

    public SetOwner() {
    }

    public SetOwner(int eid, UUID ownerUUid) {
        this.eid = eid;
        this.ownerUUid = ownerUUid;
    }
}

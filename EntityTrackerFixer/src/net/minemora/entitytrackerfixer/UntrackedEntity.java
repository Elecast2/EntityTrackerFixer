package net.minemora.entitytrackerfixer;

import java.util.UUID;

public class UntrackedEntity {
	
	private final net.minecraft.server.v1_14_R1.Entity entity;
	private final UUID uniqueID;
	private int id;
	
	public UntrackedEntity(net.minecraft.server.v1_14_R1.Entity entity) {
		this.entity = entity;
		this.uniqueID = entity.getUniqueID();
		this.id = entity.getId();
	}
	
	public net.minecraft.server.v1_14_R1.Entity getEntity() {
		return entity;
	}

	public UUID getUniqueID() {
		return uniqueID;
	}
	
	private int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if ((o instanceof UntrackedEntity) && (((UntrackedEntity) o).getId() == this.id)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}

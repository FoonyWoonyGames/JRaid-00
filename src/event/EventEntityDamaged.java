package event;

import entity.Entity;

public class EventEntityDamaged extends Event {

	private float damageDealt;
	private Entity victim;
	private Entity attacker;
	private boolean critical;
	private boolean willKill;
	
	public EventEntityDamaged(float damage, Entity victim, Entity attacker, boolean crit, boolean overkill) {
		this.damageDealt = damage;
		this.victim = victim;
		this.attacker = attacker;
		this.critical = crit;
		this.willKill = overkill;
	}
	
	public float getDamageDealt() {
		return damageDealt;
	}
	
	public Entity getVictim() {
		return victim;
	}
	
	public Entity getAttacker() {
		return attacker;
	}
	
	public boolean isCriticalHit() {
		return critical;
	}
	
	public boolean isOverkill() {
		return willKill;
	}
}

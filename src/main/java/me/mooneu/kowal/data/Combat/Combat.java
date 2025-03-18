package me.mooneu.kowal.data.Combat;

import org.bukkit.entity.Player;

public class Combat {

    private Player player, lastAttackPlayer;
    private long lastAttackTime;

    public Combat(Player p) {
        this.player = p;
        this.lastAttackPlayer = null;
        this.lastAttackTime = 0L;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getLastAttackPlayer() {
        return lastAttackPlayer;
    }

    public void setLastAttackPlayer(Player lastAttackPlayer) {
        this.lastAttackPlayer = lastAttackPlayer;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public boolean hasFight() {
        return this.getLastAttackTime() > System.currentTimeMillis();
    }

    public boolean wasFight() {
        return this.getLastAttackPlayer() != null;
    }

}
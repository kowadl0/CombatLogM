package me.mooneu.kowal.data.Combat.impl;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.data.Combat.CombatManager;
import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManagerImpl implements CombatManager {

    private CombatMain plugin;

    private final ConcurrentHashMap<Player, Combat> combats = new ConcurrentHashMap<>();

    public CombatManagerImpl(CombatMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public Combat getFight(Player p) {
        return combats.get(p);
    }

    @Override
    public void createFight(Player p) {
        Combat combat = new Combat(p);
        combats.put(p, combat);
    }

    @Override
    public void removeFight(Player p) {
        combats.remove(p);
    }

    @Override
    public ConcurrentHashMap<Player, Combat> getFights() {
        return combats;
    }
}
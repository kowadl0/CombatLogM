package me.mooneu.kowal.data.Combat;

import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;

public interface CombatManager {

    Combat getFight(Player p);

    void createFight(Player p);

    void removeFight(Player p);

    ConcurrentHashMap<Player, Combat> getFights();

}

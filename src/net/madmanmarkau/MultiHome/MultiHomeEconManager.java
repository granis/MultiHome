package net.madmanmarkau.MultiHome;

import org.bukkit.plugin.Plugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MultiHomeEconManager {

    public static EconomyHandler handler;
    public static MultiHome plugin;
    private static Economy economy = null;

    public enum EconomyHandler {

        VAULT, NONE
    }

    protected static void initialize(MultiHome plugin) {
        MultiHomeEconManager.plugin = plugin;

        if (Settings.isEconomyEnabled()) {
            RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
                handler = EconomyHandler.VAULT;
            } else {
                handler = EconomyHandler.NONE;
            }
        } else {
            handler = EconomyHandler.NONE;
        }
    }

    public static boolean hasEnough(String player, double amount) {
        if (handler == EconomyHandler.VAULT) {
            if (economy.has(player, amount)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean chargePlayer(String player, double amount) {
        if (handler == EconomyHandler.VAULT) {
            if (hasEnough(player, amount)) {
                EconomyResponse result = economy.withdrawPlayer(player, amount);
                if (result.type == ResponseType.SUCCESS) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static String formatCurrency(double amount) {
        if (handler == EconomyHandler.VAULT) {
            return economy.format(amount);
        } else {
            return amount + "";
        }
    }
}
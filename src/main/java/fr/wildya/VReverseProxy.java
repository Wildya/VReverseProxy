package fr.wildya;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

@Plugin(id = "reverseproxy", name = "Reverse Proxy", authors = {"Foxley"})
public class VReverseProxy {

    @Nonnull
    final ProxyServer proxyServer;

    @Inject
    public VReverseProxy(@Nonnull final ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onPlayerChooseInitialServer(@Nonnull final PlayerChooseInitialServerEvent event) {
        final Player player = event.getPlayer();
        final String hostName = player.getVirtualHost().map(InetSocketAddress::getHostName).orElse("");

        final int firstIndex = hostName.indexOf(".");
        final int secondIndex = hostName.indexOf(".", firstIndex + 1);
        final int thirdIndex = hostName.indexOf(".", secondIndex + 1);

        // the virtualHost has the shape of subdomain.wildya.fr
        if (firstIndex > 0 && thirdIndex < 0) {
            final String subdomain = hostName.substring(0, firstIndex);
            proxyServer.getServer(subdomain).ifPresent(event::setInitialServer);
        }
    }
}
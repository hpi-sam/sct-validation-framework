package de.hpi.mod.sim.worlds.util;

public class UtilConfiguration {
    
    public static final String URL_TO_EXPLOSION = "/explosion.gif"; // the gif needs to be loaded using getResource. For
                                                                    // this reason the path looks different to the other
                                                                    // paths

    private static String urlToExplosion = URL_TO_EXPLOSION;
    
    public static String getURLToExplosion() {
        return urlToExplosion;
    }
    
}
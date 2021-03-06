package dev.hevav.pfbot;

import dev.hevav.pfbot.API.Module;
import dev.hevav.pfbot.Listener;
import dev.hevav.pfbot.Modules.Admin;
import dev.hevav.pfbot.Modules.Help;
import dev.hevav.pfbot.Modules.Music;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Configuration for PFbot
 *
 * @author hevav
 * @since 1.0
 */
public class Boot {
    //Modules to load
    public Module[] modules;

    //YouTube v3 api token
    public String yt_token;
    //Discord bot token
    public String bot_token;
    //Prefix to trigger
    public String bot_prefix;
    public String log_level;
    public JDA api;

    private static final Logger logger = LogManager.getLogger("PFbot");
    public Boot(String[] args){
        yt_token = System.getenv("pf_yt_token");
        bot_token = System.getenv("pf_bot_token");
        bot_prefix = System.getenv("pf_bot_prefix");
        log_level = System.getenv("pf_log_level");
        for(String arg : args){
            String[] arg_split = arg.split("=");
            switch(arg_split[0]){
                case "bot_token":
                    bot_token = arg_split[1];
                    break;
                case "bot_prefix":
                    bot_prefix = arg_split[1];
                    break;
                case "log_level":
                    log_level = arg_split[1];
                    break;
                case "yt_token":
                    yt_token = arg_split[1];
                    break;
            }
        }
        if(log_level == null)
            log_level = "WARN";
        switch (log_level) {
            case "OFF":
                Configurator.setLevel("PFbot", Level.OFF);
                break;
            case "FATAL":
                Configurator.setLevel("PFbot", Level.FATAL);
                break;
            case "ERROR":
                Configurator.setLevel("PFbot", Level.ERROR);
                break;
            default:
                Configurator.setLevel("PFbot", Level.WARN);
                break;
            case "DEBUG":
                Configurator.setLevel("PFbot", Level.DEBUG);
                break;
            case "TRACE":
                Configurator.setLevel("PFbot", Level.TRACE);
                break;
            case "INFO":
                Configurator.setLevel("PFbot", Level.INFO);
                break;
        }
        try {
            api = new JDABuilder(bot_token).build();
        } catch (javax.security.auth.login.LoginException e) {
            logger.fatal("Wrong credentials", e);
            return;
        }
        modules = new Module[]{new Admin(), new Music(this), new Help(this)};
        api.addEventListener(new Listener(this));
    }
}

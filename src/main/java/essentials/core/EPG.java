package essentials.core;

import arc.Core;
import arc.Events;
import mindustry.entities.type.Player;
import mindustry.game.EventType;
import mindustry.gen.Call;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static essentials.Global.*;
import static essentials.core.PlayerDB.getData;

public class EPG {
    public void main(){
        if(config.isExplimit()){
            Events.on(EventType.BuildSelectEvent.class, e -> {
                if(!e.breaking){
                    JSONObject db = getData(((Player)e.builder).uuid);
                    String name = e.tile.block().name;
                    int level = (int) db.get("level");
                    Yaml yaml = new Yaml();
                    Map<String, Integer> obj = yaml.load(String.valueOf(Core.settings.getDataDirectory().child("mods/Essentials/BlockReqExp.yml").readString()));
                    int blockreqlevel = 100;
                    if(obj.get(name) != null) {
                        blockreqlevel = obj.get(name);
                    } else if(e.tile.block().name.equals("air")){
                        log("err","epg-block-not-valid", name);
                    } else {
                        return;
                    }

                    if(level < blockreqlevel){
                        Call.onDeconstructFinish(e.tile, e.tile.block(), ((Player)e.builder).id);
                        ((Player)e.builder).sendMessage(nbundle(((Player)e.builder), "epg-block-require", name, blockreqlevel));
                    }
                }
            });
        }
    }
}

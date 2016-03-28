package com.yellowbytestudios.spacedoctor.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

public class CoreLevelSaver {

    private boolean encoded;
    private Save save;
    private String file = "levels/level_1.json";
    private FileHandle saveFile;
    private FileHandle loadFile;

    public CoreLevelSaver(String filePath, boolean encoded) {
        file = filePath;
        saveFile = Gdx.files.local(file);
        loadFile = Gdx.files.internal(file);


        this.encoded = encoded;
        save = getSave();
    }

    public static class Save {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }


    private Save getSave() {
        Save save = new Save();

        if (loadFile.exists()) {
            Json json = new Json();
            if (encoded)
                save = json.fromJson(Save.class,
                        Base64Coder.decodeString(loadFile.readString()));
            else
                save = json.fromJson(Save.class, loadFile.readString());
        }
        return save;
    }

    public void saveToJson() {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        if (encoded)
            saveFile.writeString(Base64Coder.encodeString(json.prettyPrint(save)),
                    false);
        else
            saveFile.writeString(json.prettyPrint(save), false);
    }

    @SuppressWarnings("unchecked")
    public <T> T loadDataValue(String key, Class<T> type) {
        if (save.data.containsKey(key))
            return (T) save.data.get(key);
        else
            return null; // this if() avoids and exception, but check for null
        // on load.
    }

    public void saveDataValue(String key, Object object) {
        save.data.put(key, object);
        saveToJson(); // Saves current save immediately.

        System.out.println("save complete");
    }

    public ObjectMap<String, Object> getAllData() {
        return save.data;
    }
}
package beelivery.misc;

import beelivery.user.model.Regular;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static beelivery.Application.gson;

public class JSONRepository <T extends IIdentifiable<I>, I> {
    private String filename;
    private Type type;

    public JSONRepository(String filename, Type type) {
        this.filename = filename;
        this.type = type;
        init();
    }

    private void init() {
        File f = new File(filename);
        if(!f.isFile()) {
            save(new ArrayList<T>());
        }
    }

    public boolean create(T entity) {
        List<T> all = getAll();
        Optional<T> en = all.stream().filter(e -> e.getId().equals(entity.getId())).findFirst();
        if(!en.isPresent()) {
            all.add(entity);
            save(all);
            return true;
        } else if(en.get().isDeleted()) {
            en.get().setDeleted(false);
            return update(en.get());
        }
        return false;
    }

    public boolean update(T entity) {
        List<T> all = getAll();
        for(int i = 0; i < all.size(); ++i) {
            if(all.get(i).getId().equals(entity.getId())) {
                all.set(i, entity);
                save(all);
                return true;
            }
        }
        return false;
    }

    public boolean delete(I id) {
        List<T> all = getAll();
        Optional<T> en = all.stream().filter(e -> e.getId().equals(id)).findFirst();
        if(!en.isPresent()) {
            return false;
        }
        en.get().setDeleted(true);
        save(all);
        return true;
    }

    public T get(I id) {
        return getAll().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public List<T> getAll() {
        List<T> entities = null;
        Type collectionType = new TypeToken<List<T>>(){}.getType();
        try (FileReader freader = new FileReader(filename);
             JsonReader jreader = new JsonReader(freader)) {
            entities = gson.fromJson(jreader, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities == null ? Collections.emptyList() : entities;
    }

    private void save(List<T> entities) {
        try(FileWriter writer = new FileWriter(filename)) {
            gson.toJson(entities, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

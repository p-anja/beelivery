package beelivery.user.repository;

import static beelivery.Application.gson;

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

public class RegularRepository {
    private String filename;

    public RegularRepository(String filename) {
        this.filename = filename;
        init();
    }

    private void init() {
        File f = new File(filename);
        if(!f.isFile()) {
            save(new ArrayList<Regular>());
        }
    }

    public boolean create(Regular regular) {
        List<Regular> all = getAll();
        Optional<Regular> reg = all.stream().filter(r -> r.getUsername().equals(regular.getUsername())).findFirst();
        if(!reg.isPresent()) {
            all.add(regular);
            save(all);
            return true;
        } else if(reg.get().isDeleted()) {
            reg.get().setDeleted(false);
            return update(reg.get());
        }
        return false;
    }

    public boolean update(Regular regular) {
        List<Regular> all = getAll();
        for(int i = 0; i < all.size(); ++i) {
            if(all.get(i).getUsername().equals(regular.getUsername())) {
                all.set(i, regular);
                save(all);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String username) {
        List<Regular> all = getAll();
        Optional<Regular> regular = all.stream().filter(r -> r.getUsername().equals(username)).findFirst();
        if(!regular.isPresent()) {
            return false;
        }
        regular.get().setDeleted(true);
        save(all);
        return true;
    }

    public Regular get(String username) {
        return getAll().stream().filter(r -> r.getUsername().equals(username)).findFirst().orElse(null);
    }

    public List<Regular> getAll() {
        List<Regular> regulars = null;
        Type collectionType = new TypeToken<List<Regular>>(){}.getType();
        try (FileReader freader = new FileReader(filename);
             JsonReader jreader = new JsonReader(freader)) {
            regulars = gson.fromJson(jreader, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regulars == null ? Collections.emptyList() : regulars;
    }

    private void save(List<Regular> regulars) {
        try(FileWriter writer = new FileWriter(filename)) {
            gson.toJson(regulars, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.XXXYJade.AuthMod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager {
    private static final Map<String,String> passwordMap=new HashMap<>();
    private static final String password_data_url="data/password_data.json";
    private static final Gson gson = new Gson();

    public Map<String,String> getPasswordMap(){
        return passwordMap;
    }
    public PasswordManager(){
        loadPasswords();
    }
    public static void loadPasswords(){
        try {
            Path path = Path.of(password_data_url);
            if (Files.exists(path)) {   //文件如果存在
                try (FileReader reader = new FileReader(path.toFile())) {
                    Map<String, String> loadedPasswords = gson.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());  //读取文件中的密码数据
                    if (loadedPasswords != null) {
                        passwordMap.putAll(loadedPasswords);  //如果文件不为空，将读取到的数据赋值给passwords
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("加载密码文件时出现异常: " + e.getMessage());
        }
    }
    public void savePasswords(){
        try {
            Path path = Path.of(password_data_url);
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile())) {
                gson.toJson(passwordMap, writer);
            }
        } catch (IOException e) {
            System.err.println("保存密码文件时出现异常: " + e.getMessage());
        }
    }
    public void addNewPassword(String username, String password){
        String encryptedPassword = PasswordUtils.encryptPassword(password);
        passwordMap.put(username,encryptedPassword);
        savePasswords();
    }
    public void deletePassword(String username){
        passwordMap.remove(username);
        savePasswords();
    }
    public boolean hasPassword(String username){
        return passwordMap.get(username) != null;
    }
    public String getPassword(String username){
        return passwordMap.get(username);
    }
}

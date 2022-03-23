package io.javabrains.weaponservice.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "services")
@Component
public class Links {
    private static String bandUrl;
    private static String taskUrl;
    private static String userUrl;
    private static String weaponUrl;

    public static  String getTaskUrl() {
      return taskUrl;
    }

    public static String getBandUrl() {
      return bandUrl;
    }

    public String getUserUrl() {
      return userUrl;
    }

    public String getWeaponUrl() {
      return weaponUrl;
    }

    public void setTaskUrl(String taskUrl) {
        Links.taskUrl = taskUrl;
    }

    public void setBandUrl(String bandUrl) {
        Links.bandUrl = bandUrl;
    }

    public void setUserUrl(String userUrl) {
        Links.userUrl = userUrl;
    }

    public void setWeaponUrl(String weaponUrl) {
        Links.weaponUrl = weaponUrl;
    }
}


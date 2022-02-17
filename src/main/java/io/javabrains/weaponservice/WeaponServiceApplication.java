package io.javabrains.weaponservice;

import java.nio.file.Path;

// import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import connection.DataStaxAstraProperties;
import io.javabrains.weaponservice.repository.WeaponRepository;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class WeaponServiceApplication {

	@Autowired WeaponRepository weaponRepository;
	public static void main(String[] args) {
		SpringApplication.run(WeaponServiceApplication.class, args);
	}

	// ==================================
	// TESTING CONNECTION TO THE DATABASE 
	// ==================================
	// @PostConstruct
	// public void start() {

	// 	// Weapon weapon = new Weapon();
	// 	// weapon.setId(1L);
	// 	// weapon.setName("AK-47");
	// 	// weapon.setDamage(12);
	// 	// weapon.setBand_id(1L);
	// 	// weapon.setTask_id(1L);
	// 	// weaponRepository.save(weapon);

	// 	// Weapon weapon2 = new Weapon();
	// 	// weapon2.setId(2L);
	// 	// weapon2.setName("Glock");
	// 	// weapon2.setDamage(8);
	// 	// weapon2.setBand_id(2L);
	// 	// weapon2.setTask_id(2L);
	// 	// weaponRepository.save(weapon2);

	// 	// Weapon weapon3 = new Weapon();
	// 	// weapon3.setId(3L);
	// 	// weapon3.setName("knife");
	// 	// weapon3.setDamage(4);
	// 	// weapon3.setBand_id(3L);
	// 	// weapon3.setTask_id(3L);
	// 	// weaponRepository.save(weapon3);

	// }

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}

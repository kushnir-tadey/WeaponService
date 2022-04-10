package io.javabrains.weaponservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;


@Table(value = "weapon")
public class Weapon {

    @Id 
    @PrimaryKeyColumn(name = "Id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long Id;

    @Column("name")
    @CassandraType(type = Name.TEXT)
    private String name;
    
    @Column("damage")
    @CassandraType(type = Name.INT)
    private int damage;

    @Column("taskId")
    private Long taskId;

    @Column("bandId")
    private Long bandId;

    public Weapon() {
    }

    public Weapon(Long Id, String name, int damage, Long taskId, Long bandId) {
        this.Id = Id;
        this.name = name;
        this.damage = damage;
        this.taskId = taskId;
        this.bandId = bandId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    @Override
    public String toString() {
        return "Id = " + Id + ", name = " + name + ", damage = " + damage + ", taskId = " + taskId + ", bandId = " + bandId;
    }

}

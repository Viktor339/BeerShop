package com.shop.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "positions")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Position implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "beer_type")
    private String beerType;
    @Column(name = "alcohol_percentage")
    private Double alcoholPercentage;
    private Integer bitterness;
    @Column(name = "container_type")
    private String containerType;

    @Type(type = "json")
    @Column(name = "beer_Info", columnDefinition = "json")
    private BeerInfo beerInfo;

}

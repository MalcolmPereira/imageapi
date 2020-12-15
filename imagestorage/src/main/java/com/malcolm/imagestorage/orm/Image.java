package com.malcolm.imagestorage.orm;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@Entity
@Table(name = "image",schema = "public")
public class Image extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageid")
    @Getter
    @Setter
    public long imageid;

    @Column(name = "imagehashid")
    @Getter
    @Setter
    public String imagehashid;

    @Getter
    @Setter
    @Column(name = "img")
    public byte[] img;

    @Getter
    @Setter
    @Column(name = "imgthumbnail")
    public byte[] imgthumbnail;

    @Getter
    @Setter
    @Column(name = "dateadded")
    public LocalDateTime dateadded;
}

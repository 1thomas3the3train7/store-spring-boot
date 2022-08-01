package com.example.MYSTORE.PRODUCTS.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
public class TeaImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String linkImage;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "tea_and_image",joinColumns = @JoinColumn(name = "image_id"),inverseJoinColumns = @JoinColumn(name = "tea_id"))
    private Tea tea;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "slaider_and_image",joinColumns = @JoinColumn(name = "image_id"),inverseJoinColumns = @JoinColumn(name = "slaider_id"))
    private Collection<SlaiderImages> slaiderImages;

    public TeaImage() {
    }

    public TeaImage(String linkImage) {
        this.linkImage = linkImage;
    }
}

//package com.example.newneekclone.domain.news.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@Table(name="images")
//@NoArgsConstructor
//public class Images {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "newsId")
//    private News news;
//
//    @Column(name = "imageURL")
//    String imageUrl;
//
//    @Column(name = "videoURL")
//    String videoUrl;
//
//    public Images( String imageUrl, String videoUrl) {
////
//        this.imageUrl = imageUrl;
//        this.videoUrl = videoUrl;
//    }
//}

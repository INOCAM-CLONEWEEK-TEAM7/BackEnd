package com.example.newneekclone.domain.news;

import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.entity.News;
import com.example.newneekclone.domain.news.repository.NewsRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "daum")
@EnableScheduling
@Configuration
@Service
@RequiredArgsConstructor
public class daumCrawlingServicetow {
    private final NewsRepository newsRepository;
//    private final ImagesRepository imagesRepository;

    public List<NewsResponseDto> allCrwaling() {
        String[] categories = {"society", "politics", "economic", "foreign", "culture", "entertain", "sports", "digital"};
        String baseURL = "https://news.daum.net/breakingnews/";
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        List<NewsResponseDto> allNews = new ArrayList<>();
//        List<String> allTag=new ArrayList<>();
        try {
            for (String category : categories) {
                // Construct the URL for each category with page 1
                String url = baseURL + category + "?page=1";

                // Fetch the HTML of the page
                Document doc = Jsoup.connect(url).get();

                // Find the article elements that contain the titles and links
                Elements articleElements = doc.select(".tit_thumb > a.link_txt");

                for (Element article : articleElements) {
                    String title = article.text();
                    String link = article.attr("href"); // Get the link

                    News existingNewsTitle = newsRepository.findByTitle(title);
                    if (existingNewsTitle != null) {
                        continue;
                    }

                    News existingNewsId = newsRepository.findByUrl(link);
                    if (existingNewsId != null) {
                        continue;
                    }

                    // Fetch details of the news article using the crawlNewsArticle() method
                    NewsResponseDto newsResponseDto = crawlNewsArticle(link, new News());

                    News existingNewsContent = newsRepository.findByContent(newsResponseDto.getContent());
                    if (existingNewsContent != null) {
                        continue;
                    }
                    if (newsResponseDto != null) {
                        // Create and save News entity to the database

//                        KomoranResult analyzeResultList = komoran.analyze(newsResponseDto.getTitle());
//                        List<Token> tokenList =analyzeResultList.getTokenList();
//                        for (Token token : tokenList) {
//                            if (token.getMorph().length() > 1 && !token.getMorph().startsWith("ㄹ") && !token.getMorph().startsWith("ㅂ") && !token.getMorph().startsWith("ㄴ")) {
//                                log.info(token.getMorph());
//                                allTag.add(token.getMorph()+"{^%");
//
//                            }
//                        }
                        // Define the date-time format to match the input string
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. H:mm");

                        // Parse the string date-time into a LocalDateTime object
                        LocalDateTime localDateTime = newsResponseDto.getDate();
                        News news = new News(newsResponseDto.getTags(), newsResponseDto.getTitle(), category,
                                link, newsResponseDto.getContent(), localDateTime, newsResponseDto.getImageUrl(), newsResponseDto.getVideoUrl());


                        //                        Images images = new Images(newsResponseDto.getImageUrl(), newsResponseDto.getVideoUrl());
////                        News news = new News();
//////                        Images images = new Images();
////                        news.setTags(allTag.toString());
////                        news.setTitle(newsResponseDto.getTitle());
////                        news.setCategory(category);
////                        news.setUrl(link);
////                        news.setContent(newsResponseDto.getContent());
////                        news.setDate(newsResponseDto.getDate());
////                        news.setImages(images);
                        newsRepository.save(news);
//                        imagesRepository.save(images);

                        allNews.add(newsResponseDto);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allNews;
    }


    public NewsResponseDto crawlNewsArticle(String url, News news) {
        try {
            Document doc = Jsoup.connect(url).get();
            Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

            Element titleElement = doc.selectFirst("h3.tit_view");
            Element dateElement = doc.selectFirst("span.num_date");
            Elements contentElements = doc.select("p[dmcf-ptype=general]");
            Element imageElement = doc.select("img.thumb_g_article").first();
            Element videoElement = doc.select("div.vod_player iframe").first();

            String title = titleElement.text();
            String date = dateElement.text();
            String content = contentElements.text();
            String imageUrl = (imageElement != null) ? imageElement.attr("src") : null;
            String vidioUrl = (videoElement != null) ? "https:" + videoElement.attr("src") : null;
            List<String> allTag=new ArrayList<>();

            KomoranResult analyzeResultList = komoran.analyze(title);
            List<Token> tokenList =analyzeResultList.getTokenList();
            for (Token token : tokenList) {
                if (token.getMorph().length() > 1 && !token.getMorph().startsWith("ㄹ") && !token.getMorph().startsWith("ㅂ") && !token.getMorph().startsWith("ㄴ")) {
                    log.info(token.getMorph());
                    allTag.add(token.getMorph()+"{^%");
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. H:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

            NewsResponseDto newsResponseDto = new NewsResponseDto(news);
            newsResponseDto.setTitle(title);
            newsResponseDto.setDate(localDateTime);
            newsResponseDto.setContent(content);
            newsResponseDto.setImageUrl(imageUrl);
            newsResponseDto.setVideoUrl(vidioUrl);
            newsResponseDto.setTags(allTag.toString());
            return newsResponseDto;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

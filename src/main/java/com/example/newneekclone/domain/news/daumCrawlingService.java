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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "daum")
@EnableScheduling
@Configuration
@Service
@RequiredArgsConstructor
public class daumCrawlingService {
    private final NewsRepository newsRepository;

    // 주기적으로 실행되는 스케줄링 메서드
    @Scheduled(cron = "5 * * * * ?")
    public void allCrwaling() { //
        String[] categories = {"society", "politics", "economic", "foreign", "culture", "entertain", "sports", "digital"};
        String baseURL = "https://news.daum.net/breakingnews/";

        try {
            for (String category : categories) {
                // 각 카테고리별로 페이지 1의 URL을 구성
                String url = baseURL + category + "?page=1";

                // 페이지의 HTML을 가져옴
                Document doc = Jsoup.connect(url).get();

                // 기사 제목과 링크를 포함하는 기사 요소를 찾음
                Elements articleElements = doc.select(".tit_thumb > a.link_txt");

                for (Element article : articleElements) {
                    String title = article.text();
                    String link = article.attr("href"); // 링크 추출

                    // 같은 제목, 주소의 뉴스가 이미 데이터베이스에 있는 경우 건너뜀
                    if (newsRepository.existsByTitleOrUrl(title, link)) {
                        continue;
                    }

                    // 뉴스 기사의 상세 내용을 crawlNewsArticle() 메서드를 통해 가져옴
                    NewsResponseDto newsResponseDto = crawlNewsArticle(link, new News());

//                    // 같은 내용의 뉴스가 이미 데이터베이스에 있는 경우 건너뜀
                    if (newsRepository.existsByContent(newsResponseDto.getContent())) {
                        continue;
                    }
                    if (newsResponseDto != null) {
                        // 뉴스 엔티티를 생성하여 데이터베이스에 저장

                        // 문자열 형식의 날짜를 LocalDateTime 객체로 파싱
                        LocalDateTime localDateTime = newsResponseDto.getDate();
                        News news = new News(newsResponseDto.getTags(), newsResponseDto.getTitle(), category,
                                link, newsResponseDto.getContent(), localDateTime, newsResponseDto.getImageUrl(), newsResponseDto.getVideoUrl());

                        newsRepository.save(news);
//                        allNews.add(newsResponseDto);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        return allNews;
    }

    // 뉴스 기사 상세 내용을 크롤링하는 메서드
    public NewsResponseDto crawlNewsArticle(String url, News news) {
        try {
            Document doc = Jsoup.connect(url).get();

            // 기사 제목, 날짜, 내용, 이미지, 비디오 링크 등을 가져옴
            Element titleElement = doc.selectFirst("h3.tit_view");
            Element dateElement = doc.selectFirst("span.num_date");
            Elements contentElements = doc.select("p[dmcf-ptype=general]");
            Element imageElement = doc.select("img.thumb_g_article").first();
            Element videoElement = doc.select("div.vod_player iframe").first();

            String title = titleElement.text();
            String date = dateElement.text();
            String content = contentElements.text();
            String imageUrl = (imageElement != null) ? imageElement.attr("src") : null;
            String videoUrl = (videoElement != null) ? "https:" + videoElement.attr("src") : null;

            String allTag = analyzeTags(title);

            // 날짜 형식을 일치시키기 위한 DateTimeFormatter 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. H:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

            // NewsResponseDto 객체를 생성하여 뉴스 정보를 담아 반환
//            NewsResponseDto newsResponseDto = new NewsResponseDto(news);
            return new NewsResponseDto(title, allTag, localDateTime, content, imageUrl, videoUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 제목을 형태소 분석하여 태그로 사용
    public String analyzeTags(String title) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        List<String> allTag=new ArrayList<>();
        KomoranResult analyzeResultList = komoran.analyze(title);
        List<Token> tokenList =analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            if (token.getMorph().length() > 1 && !token.getMorph().startsWith("ㄹ") && !token.getMorph().startsWith("ㅂ") && !token.getMorph().startsWith("ㄴ")) {
//                log.info(token.getMorph());
                allTag.add(token.getMorph()+"{^%");
            }
        }
        return allTag.toString();
    }

}

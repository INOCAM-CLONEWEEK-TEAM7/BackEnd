package com.example.newneekclone.domain.news;

import com.example.newneekclone.domain.news.dto.NewsOneResponsDto;
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
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j(topic = "daum")
@EnableScheduling
@Configuration
@Service
@RequiredArgsConstructor
public class daumCrawlingService {
    private final NewsRepository newsRepository;

    // 주기적으로 실행하는 스케줄링 메서드
    @Scheduled(cron = "0 */8 * * * ?") // 매달 5일 마다
    public void allCrwaling() { //
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String[] categories = {"society", "politics", "economic", "foreign", "culture", "entertain", "sports", "digital"};
        String baseURL = "https://news.daum.net/breakingnews/";

        try {
            for (int page = 1; page <= 1; page++) { // 1~10 페이지
                for (String category : categories) { // 각 카테고리별로
                    // 현재 카테고리와 페이지를 사용하여 URL 구성
                    String url = baseURL + category + "?page=" + page;

                    // 페이지의 HTML을 가져옴
                    Document doc = Jsoup.connect(url).get();

                    // 기사 제목과 링크를 포함하는 기사 요소를 찾음
                    Elements articleElements = doc.select(".tit_thumb > a.link_txt");

                    for (Element article : articleElements) {
                        String title = article.text();
                        String link = article.attr("href"); // 주소 추출

                        if (newsRepository.existsByTitleOrUrl(title, link)) {
                            continue;
                        }

                        // 뉴스 기사의 상세 내용을 crawlNewsArticle() 메서드를 통해 가져옴
                        NewsOneResponsDto newsResponseDto = crawlNewsArticle(link);

                        // 같은 내용의 뉴스가 이미 데이터베이스에 있는 경우 건너뜀
                        if (newsRepository.existsByContent(newsResponseDto.getContent())) {
                            continue;
                        }
                        if (newsResponseDto != null) {
                            // Create the News entity and add it to the list
                            LocalDateTime localDateTime = newsResponseDto.getCreateDate();
                            News news = new News(newsResponseDto.getTag(), newsResponseDto.getTitle(), category,
                                    link, newsResponseDto.getContent(), localDateTime, newsResponseDto.getImageUrl(), newsResponseDto.getVideoUrl());

                            newsRepository.save(news);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.info("객체 생성 시간: {}", stopWatch.getTotalTimeSeconds());
    }

    // 뉴스 기사 상세 내용을 크롤링하는 메서드
    public NewsOneResponsDto crawlNewsArticle(String url) {
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
            String content = "";

            // P 태그 사이에 {^% 추가
            for (Element element : contentElements) {
                content += (element.text() + "{^%");
            }

            String imageUrl = (imageElement != null) ? imageElement.attr("src") : null;
            String videoUrl = (videoElement != null) ? "https:" + videoElement.attr("src") : null;

            String allTag = analyzeTags(title);

            // 날짜 형식을 일치시키기 위한 DateTimeFormatter 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. H:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

            return new NewsOneResponsDto(title, allTag, localDateTime, content, imageUrl, videoUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private Set<String> allowedMorphs = new HashSet<>(Arrays.asList("ㄹ", "ㅂ", "ㄴ"));
    private Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
//    입력된 제목을 형태소 분석하여 의미 있는 태그를 추출
//    형태소 분석에 사용되는 라이브러리는 Komoran
    public String analyzeTags(String title) {
        long startTime = System.currentTimeMillis();

        String allTag = "";
        KomoranResult analyzeResultList = komoran.analyze(title);
        for (Token token : analyzeResultList.getTokenList()) {
            if (token.getMorph().length() > 1 && !allowedMorphs.contains(token)) {
//                log.info(token.getMorph());
                allTag += (token.getMorph()+"{^%");
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed Time: " + elapsedTime + "ms");


        return allTag;

    }

}

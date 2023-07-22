package com.example.newneekclone.domain.news;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.dto.TestDto;
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

@Slf4j(topic = "daum")
@EnableScheduling
@Configuration
@Service
@RequiredArgsConstructor
public class daumCrawlingService {
    private final NewsRepository newsRepository;

//    @Scheduled(cron = "5 * * * * ?")// 매월 매일 매시 매분 5초에 실행 ("초 분 시 일 월")
    // 매시간 1분 10시1분 11시1분
    public List<NewsResponseDto> allCrwaling() {
//        List<String> idList = new ArrayList<>();
        List<TestDto> idList=new ArrayList<>();

        TestDto testDto=new TestDto();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        //society : 사회, politics : 정치, economic : 경제, foreign : 국제, culture : 문화, entertain : 연예, sports : 스포츠, digital : IT
        String[] category = {"society", "politics", "economic", "foreign", "culture", "entertain", "sports", "digital"};
        try {
            for (int i = 0; i < category.length; i++) {
                String param = category[i] + "?page=";

                String URL = "https://news.daum.net/breakingnews/" + param;
                for (int p = 1; p < 2; p++) {
                    String params = "" + p;
                    Document doc = Jsoup.connect(URL + params).userAgent("Mozilla/5.0").get();
                    Elements links = doc.select("a[href]");

                    for (Element link : links) {
                        String href = link.attr("href");
                        if (href.startsWith("https://v.daum.net/v/")) {
//                            idList.add(href.substring(21));
                            testDto.setId(href.substring(21));
                            testDto.setCategory(category[i]);
                            idList.add(testDto);
                            //subhttps://v.daum.net/v/15615232554
                            // id들을 저장
                            //상세 조회부분에 id들을 넘겨주면
                            // 서버에 부담 x
                        }
                    }

                }
//                System.out.println(ca);
                idList = idList.stream().distinct().collect(Collectors.toList()); //중복되는 id들 제거
            }
        //idList<???>
            // news_id

        } catch (Exception e) {
            log.info("크롤링 실패 : " + e);
        }
        //뉴스 상세조회
        try {
            // 뉴스를 새로갱신될때
            String URL = "https://v.daum.net/v/";
            for (int p = 0; p < idList.size(); p++) {
                String params = "" + idList.get(p).getId();
                //뉴스 아이디들
                // 클래스를 한개 만들어서 카테고리하고 아이디
                // List<responseDto> = new
                log.info(params);//
                Document doc = Jsoup.connect(URL + params).userAgent("Mozilla/5.0").get();

                Element title = doc.selectFirst("h3.tit_view");
                Element img = doc.select("img.thumb_g_article").first();
                // 동영상
                Element vidio = doc.select("div.vod_player iframe").first();
                Elements contents = doc.select("p[dmcf-ptype=general]");
                Element date = doc.selectFirst("span.num_date");
//                String strToAnalyze = "땡큐 포스코 에코프로 동학개미";

                KomoranResult analyzeResultList = komoran.analyze(title.text());
//                System.out.println(analyzeResultList.getPlainText());
                log.info("태그 : ");
                String allTag = "";
                List<Token> tokenList = analyzeResultList.getTokenList();
                for (Token token : tokenList) {
                    if (token.getMorph().length() > 1 && !token.getMorph().startsWith("ㄹ") && !token.getMorph().startsWith("ㅂ") && !token.getMorph().startsWith("ㄴ")) {
                        log.info(token.getMorph());
                        allTag += (token.getMorph()+"{^%");
                    }
                }
                String allContents = "";
                List<String> text = new ArrayList<>();
                for (Element content : contents) {
                    allContents += (content.text() + "{^%");
//                    text.add(content.text()); // text() 메서드로 태그 안의 텍스트를 추출합니다.
                }
                log.info(title.text());//제목
                log.info(allContents);// 기사내용 한줄한줄 리스트

                if (img != null) {
                    log.info(img.attr("src"));//사진 링크
                    // Do something with text
                } else {
                    //이미지 없음
                }
                if (vidio != null) {
                    log.info("https:" + vidio.attr("src"));//사진 링크
                    // Do something with text
                } else {
                    //이미지 없음
                }
                log.info(date.text() + "\n\n");//날짜
                News news = new News(title.text(),idList.get(p).getCategory() , allContents, date.text(), allTag);
                newsRepository.save(news);
            }

        } catch (Exception e) {
            log.info("크롤링 실패 : " + e);
        }
        return null;
    }
}

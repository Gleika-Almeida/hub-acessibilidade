package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.ScanHtmlResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HtmlAccessibilityAnalyzer {

     public ScanHtmlResponse analyze(String html){
         Document document = Jsoup.parse(html);

         boolean hasLanguage = hasDocumentLanguage(document);
         boolean hasTitle = !document.title().isBlank();
         boolean hasHeadingOne = document.selectFirst("h1") != null;
         
         int totalImagens = document.select("img").size();

         int imagesWithoutAlt = document
            .select("img:not([alt])")
            .size();
           
         int score = calculateScore(
            hasLanguage,
            hasTitle,
            hasHeadingOne,
            imagesWithoutAlt
         );
         
         return new ScanHtmlResponse(
            score,
            hasLanguage,
            hasTitle,
            hasHeadingOne,
            totalImagens,
            imagesWithoutAlt,
            Instant.now()
         );
     }
     private boolean hasDocumentLanguage(Document document) {
        Element htmlElement = document.selectFirst("html");

        return htmlElement != null
                && !htmlElement.attr("lang").isBlank();
    }

private int calculateScore(
            boolean hasLanguage,
            boolean hasTitle,
            boolean hasHeadingOne,
            int imagesWithoutAlt
    ) {
        int score = 100;

        if (!hasLanguage) {
            score -= 20;
        }

        if (!hasTitle) {
            score -= 20;
        }

        if (!hasHeadingOne) {
            score -= 20;
        }

        score -= Math.min(imagesWithoutAlt * 10, 40);

        return Math.max(score, 0);
    }
}



    

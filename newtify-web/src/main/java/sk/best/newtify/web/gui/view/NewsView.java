package sk.best.newtify.web.gui.view;

import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.ObjectFactory;
import sk.best.newtify.api.ArticlesApi;
import sk.best.newtify.api.dto.ArticleDTO;
import sk.best.newtify.web.gui.component.article.ArticlePreviewComponent;
import sk.best.newtify.web.gui.component.widget.CryptoWidgetComponent;
import sk.best.newtify.web.gui.component.widget.JokeWidgetComponent;
import sk.best.newtify.web.gui.component.widget.NameDayWidgetComponent;
import sk.best.newtify.web.gui.component.widget.WeatherWidgetComponent;
import sk.best.newtify.web.gui.layout.MainLayout;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author Marek Urban
 * Copyright © 2022 BEST Technická univerzita Košice.
 * All rights reserved.
 */
@PageTitle("News")
@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "news", layout = MainLayout.class)
public class NewsView extends FlexLayout {

    private static final long serialVersionUID = 4107656392983873277L;

    private final ArticlesApi                            articlesApi;
    private final ObjectFactory<ArticlePreviewComponent> articlePreviewObjectFactory;
    private final ObjectFactory<NameDayWidgetComponent>  nameDayWidgetComponentObjectFactory;
    private final ObjectFactory<WeatherWidgetComponent>  weatherWidgetComponentObjectFactory;
    private final ObjectFactory<JokeWidgetComponent>  jokeWidgetComponentObjectFactory;
    private final ObjectFactory<CryptoWidgetComponent>  cryptoWidgetComponentObjectFactory;

    private final VerticalLayout middleContent      = new VerticalLayout();
    private final VerticalLayout leftWidgetContent  = new VerticalLayout();
    private final VerticalLayout rightWidgetContent = new VerticalLayout();

    private List<ArticleDTO> articles = Collections.emptyList();

    public NewsView(ArticlesApi articlesApi,
                    ObjectFactory<ArticlePreviewComponent> articlePreviewObjectFactory,
                    ObjectFactory<NameDayWidgetComponent> nameDayWidgetComponentObjectFactory,
                    ObjectFactory<WeatherWidgetComponent> weatherWidgetComponentObjectFactory,
                    ObjectFactory<JokeWidgetComponent>  jokeWidgetComponentObjectFactory,
                    ObjectFactory<CryptoWidgetComponent>  cryptoWidgetComponentObjectFactory) {
        this.articlesApi                         = articlesApi;
        this.articlePreviewObjectFactory         = articlePreviewObjectFactory;
        this.nameDayWidgetComponentObjectFactory = nameDayWidgetComponentObjectFactory;
        this.weatherWidgetComponentObjectFactory = weatherWidgetComponentObjectFactory;
        this.jokeWidgetComponentObjectFactory = jokeWidgetComponentObjectFactory;
        this.cryptoWidgetComponentObjectFactory = cryptoWidgetComponentObjectFactory;
    }

    @PostConstruct
    protected void init() {
        createMainPane();
        createLeftWidgetPane();
        createRightWidgetPane();

        add(leftWidgetContent, middleContent, rightWidgetContent);
    }

    private void createMainPane() {
        middleContent.removeAll();
        middleContent.setAlignItems(Alignment.CENTER);
        setFlexShrink(1, middleContent);
        setFlexGrow(2, middleContent);
        fetchArticles();
        for (ArticleDTO article : articles) {
            ArticlePreviewComponent previewComponent = articlePreviewObjectFactory.getObject();
            previewComponent.setArticle(article);
        }
    }

    private void createRightWidgetPane() {
        rightWidgetContent.removeAll();
        rightWidgetContent.setAlignItems(Alignment.CENTER);
        setFlexShrink(2, rightWidgetContent);
        setFlexGrow(1, rightWidgetContent);

        JokeWidgetComponent jokeWidget = jokeWidgetComponentObjectFactory.getObject();
        rightWidgetContent.add(jokeWidget);

        CryptoWidgetComponent cryptoWidget = cryptoWidgetComponentObjectFactory.getObject();
        rightWidgetContent.add(cryptoWidget);
    }

    private void createLeftWidgetPane() {
        leftWidgetContent.removeAll();
        leftWidgetContent.setAlignItems(Alignment.CENTER);
        setFlexShrink(2, leftWidgetContent);
        setFlexGrow(1, leftWidgetContent);

        NameDayWidgetComponent nameDayWidget = nameDayWidgetComponentObjectFactory.getObject();
        leftWidgetContent.add(nameDayWidget);

        WeatherWidgetComponent weatherWidget = weatherWidgetComponentObjectFactory.getObject();
        leftWidgetContent.add(weatherWidget);
    }

    private void fetchArticles() {
        articles = articlesApi.retrieveArticles(null).getBody();
    }
}

package com.example.application.views.main;


import com.example.application.views.about.AboutView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;

import java.util.Arrays;
import java.util.HashSet;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    static class Route{
        public final Class<? extends Component> clazz;
        public final String name;
        public final VaadinIcon icon;

        public Route(Class<? extends Component> clazz, String name, VaadinIcon icon) {
            this.clazz = clazz;
            this.name = name;
            this.icon = icon;
        }
    }
    public static final HashSet<Route> navigationRoutes = new HashSet<>(Arrays.asList(
            new Route(MainView.class, "Home", VaadinIcon.HOME),
            new Route(AboutView.class, "About", VaadinIcon.QUESTION)
    ));

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("My Todo");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private Layout createNavigation() {
        Layout ly = new Layout();
        for (Route r : navigationRoutes) {
            ly.horizontal().alignCenter().padding(true)
                    //.widthNext("10px").a(new Icon(r.icon))
                    .textS(r.name)
                    .onClick(event -> {
                        UI.getCurrent().navigate(r.clazz);
                    });
        }
        return ly;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}

package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import org.vaadin.firitin.appframework.MainLayout;

public class Layout extends MainLayout {
    @Override
    protected String getDrawerHeader() {
        return "}> Java in Browser";
    }

    Anchor viewSource = new Anchor("", "View source");

    private static final String baseSourceUrl = "https://github.com/mstahv/java-in-browser-examples/blob/main/demo-app/src/main/java/%s.java";

    @Override
    public void setContent(Component content) {
        super.setContent(content);
        String name = content.getClass().getName();
        viewSource.setHref(baseSourceUrl.formatted(name.replace(".", "/")));
        if(!viewSource.isAttached()) {
            HorizontalLayout links = new HorizontalLayout(viewSource);
            links.getStyle().setPosition(Style.Position.ABSOLUTE);
            links.getStyle().setRight("1em");
            links.getStyle().setTop("1em");
            addToNavbar(true, links);
        }
    }

}

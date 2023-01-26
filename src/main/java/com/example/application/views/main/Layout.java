package com.example.application.views.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Layout extends Div {
    public VerticalLayout vl = new VerticalLayout();
    public HorizontalLayout hl = new HorizontalLayout();
    private String nextWidth, nextHeight;
    private void setNextSize(Component comp){
        if(comp instanceof HasSize){
            HasSize c = (HasSize) comp;
            if(nextWidth != null){
                c.setWidth(nextWidth);
                nextWidth = null;
            }
            if(nextHeight != null){
                c.setHeight(nextHeight);
                nextHeight = null;
            }
        }
    }

    public Layout() {
        this(false);
    }

    public Layout(boolean isHorizontal) {
        vl.setSizeFull(); hl.setSizeFull();
        add(vl, hl);
        align(isHorizontal);
    }

    public Layout(Component... children) {
        this(false, children);
    }

    public Layout(boolean isHorizontal, Component... children) {
        super(children);
        vl.setSizeFull(); hl.setSizeFull();
        add(vl, hl);
        align(isHorizontal);
    }

    public Layout align(boolean horizontal){
        if(horizontal) {
            vl.setVisible(false);
            hl.setVisible(true);
        } else{
            vl.setVisible(true);
            hl.setVisible(false);
        }
        return this;
    }

    /**
     * Same as {@link #add(Component...)}, but returns itself for chaining. <br>
     * Note that if multiple components are provided and  {@link #widthNext(String)} or {@link #heightNext(String)} was called before,
     * all their widths/heights get set.
     */
    public Layout a(Component... components){
        for (Component c : components) {
            add(c);
            setNextSize(c);
        }
        return this;
    }

    public Layout width(String s){
        setWidth(s);
        return this;
    }

    public Layout height(String s){
        setHeight(s);
        return this;
    }

    /**
     * The width of the component that is added next.
     */
    public Layout widthNext(String s){
        nextWidth = (s);
        return this;
    }

    /**
     * The height of the component that is added next.
     */
    public Layout heightNext(String s){
        nextHeight = (s);
        return this;
    }

    public Layout padding(boolean b){
        vl.setPadding(b);
        hl.setPadding(b);
        return this;
    }

    public Layout margin(boolean b){
        vl.setMargin(b);
        hl.setMargin(b);
        return this;
    }

    public Layout spacing(boolean b){
        vl.setSpacing(b);
        hl.setSpacing(b);
        return this;
    }

    public Layout enable(boolean b){
        vl.setEnabled(b);
        hl.setEnabled(b);
        return this;
    }

    public Layout vertical(){
        Layout l = new Layout(false);
        add(l);
        setNextSize(l);
        return l;
    }

    public Layout horizontal(){
        Layout l = new Layout(true);
        add(l);
        setNextSize(l);
        return l;
    }

    public Layout textXS(String s){
        Label t = new Label(s);
        t.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        add(t);
        setNextSize(t);
        return this;
    }

    public Layout textS(String s){
        Label t = new Label(s);
        t.getStyle().set("font-size", "var(--lumo-font-size-s)");
        add(t);
        setNextSize(t);
        return this;
    }

    public Layout textM(String s){
        Label t = new Label(s);
        t.getStyle().set("font-size", "var(--lumo-font-size-m)");
        add(t);
        setNextSize(t);
        return this;
    }

    public Layout textL(String s){
        Label t = new Label(s);
        t.getStyle().set("font-size", "var(--lumo-font-size-l)");
        add(t);
        return this;
    }

    public Layout textXL(String s){
        Label t = new Label(s);
        t.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        add(t);
        setNextSize(t);
        return this;
    }

    public Layout onClick(ComponentEventListener<ClickEvent<Div>> code){
        addClickListener(code);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#CENTER
     */
    public Layout alignCenter(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#START
     */
    public Layout alignStart(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#END
     */
    public Layout alignChildrenX(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#AUTO
     */
    public Layout alignAuto(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.AUTO);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#BASELINE
     */
    public Layout alignBaseline(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        return this;
    }

    /**
     * @see FlexComponent.Alignment#STRETCH
     */
    public Layout alignStretch(){
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        return this;
    }
}

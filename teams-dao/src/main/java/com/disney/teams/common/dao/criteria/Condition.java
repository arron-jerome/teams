package com.disney.teams.common.dao.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询条件定义类
 */
public class Condition implements Serializable {

    private static final long serialVersionUID = 4915324579328461586L;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性值
     */
    private Object value;

    /**
     * 匹配方式
     */
    private MatchMode matchMode;

    /**
     * and条件
     */
    private List<Condition> and;

    /**
     * or条件，or条件在and条件之后
     */
    private List<Condition> or;

    /**
     * 匹配权重得分，目前仅es实现此功能
     */
    private Float boost;

    public Condition() {}

    public Condition(String name) {
        this(name, null);
    }

    public Condition(String name, Object value) {
        this(name, value, MatchMode.EQ);
    }

    public Condition(String name, Object value, MatchMode matchMode) {
        this.name = name;
        this.value = value;
        this.matchMode = (null == matchMode ? MatchMode.EQ : matchMode);
    }

    public Condition(Condition c) {
        this.name = c.name;
        this.value = c.value;
        this.matchMode = c.matchMode;
        this.and = c.and;
        this.or = c.or;
        this.boost = c.boost;
    }

    public boolean hasAnd() {
        return and != null && !and.isEmpty();
    }

    public boolean hasOr() {
        return or != null && !or.isEmpty();
    }

    private static final boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public boolean equals(String name, Object value, MatchMode matchMode) {
        return this.matchMode == matchMode && equals(this.name, name) && equals(this.value, value);
    }

    public Condition clone() {
        return new Condition(this);
    }

    public static Condition eq(String name, Object value){
        return new Condition(name, value, MatchMode.EQ);
    }

    public static Condition ne(String name, Object value){
        return new Condition(name, value, MatchMode.NE);
    }
    
    public static Condition gt(String name, Object value){
        return new Condition(name, value, MatchMode.GT);
    }
    
    public static Condition gte(String name, Object value){
        return new Condition(name, value, MatchMode.GTE);
    }
    
    public static Condition lt(String name, Object value){
        return new Condition(name, value, MatchMode.LT);
    }
    
    public static Condition lte(String name, Object value){
        return new Condition(name, value, MatchMode.LTE);
    }
    
    public static Condition in(String name, Object value){
        return new Condition(name, value, MatchMode.IN);
    }
    
    public static Condition isNull(String name){
        return new Condition(name, null, MatchMode.NULL);
    }
    
    public static Condition notNull(String name){
        return new Condition(name, null, MatchMode.NOTNULL);
    }
    
    public static Condition like(String name, Object value){
        return new Condition(name, value, MatchMode.LIKE);
    }
    
    public static Condition ilike(String name, Object value){
        return new Condition(name, value, MatchMode.ILIKE);
    }

    public static Condition match(String name, Object value){
        return new Condition(name, value, MatchMode.MATCH);
    }
    
    public static Condition make(String name, Object value, MatchMode matchMode){
        return new Condition(name, value, matchMode);
    }

    /*----------------getter setter start---------------------*/
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String name(){
        return this.name;
    }
    public Condition name(String name){
        this.name = name;
        return this;
    }

    public Object getValue(){
        return this.value;
    }
    public void setValue(Object value){
        this.value = value;
    }

    public Object value(){
        return this.value;
    }
    public Condition value(Object value){
        this.value = value;
        return this;
    }

    public MatchMode getMatchMode(){
        return this.matchMode;
    }
    public void setMatchMode(MatchMode matchMode){
        this.matchMode = matchMode;
    }

    public MatchMode matchMode(){
        return this.matchMode;
    }
    public Condition matchMode(MatchMode matchMode){
        this.matchMode = matchMode;
        return this;
    }

    public List<Condition> getAnd(){
        return this.and;
    }
    public void setAnd(List<Condition> and){
        this.and = and;
    }

    public List<Condition> and(){
        return this.and;
    }
    public Condition and(Condition and){
        if(and == null) {
            return this;
        }
        if(this.and == null) {
            this.and = new ArrayList<>();
        }
        this.and.add(and);
        return this;
    }
    public Condition and(List<Condition> and){
        if(and == null) {
            return this;
        }
        for(Condition c : and) {
            and(c);
        }
        return this;
    }
    public Condition and(Condition... and){
        if(and == null) {
            return this;
        }
        for(Condition c : and) {
            and(c);
        }
        return this;
    }

    public List<Condition> getOr(){
        return this.or;
    }
    public void setOr(List<Condition> or){
        this.or = or;
    }

    public List<Condition> or(){
        return this.or;
    }
    public Condition or(Condition or){
        if(or == null) {
            return this;
        }
        if(this.or == null) {
            this.or = new ArrayList<>();
        }
        this.or.add(or);
        return this;
    }
    public Condition or(List<Condition> or){
        if(or == null) {
            return this;
        }
        for(Condition c : or) {
            or(c);
        }
        return this;
    }
    public Condition or(Condition... or){
        if(or == null) {
            return this;
        }
        for(Condition c : or) {
            or(c);
        }
        return this;
    }

    public Float getBoost() {
        return boost;
    }

    public void setBoost(Float boost) {
        this.boost = boost;
    }

    public Float boost() {
        return boost;
    }

    public Condition boost(Float boost) {
        this.boost = boost;
        return this;
    }

    /*----------------getter setter end---------------------*/

}

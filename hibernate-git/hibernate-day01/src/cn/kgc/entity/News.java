package cn.kgc.entity;

import java.util.Date;

public class News {



    private Integer id;
    private String title;
    private String author;
    private Date createDate;
    private Double score;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    public News() {
    }

    public News(String title, String author, Date createDate) {
        this.title = title;
        this.author = author;
        this.createDate = createDate;
    }
    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", createDate=" + createDate +
                ", score=" + score +
                '}';
    }

}
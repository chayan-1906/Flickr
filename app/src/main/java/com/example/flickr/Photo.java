package com.example.flickr;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final String author;
    private final String authorId;
    private final String link;
    private final String tags;
    private final String image;

    public Photo(String title, String author, String authorId, String link, String tags, String image) {
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.link = link;
        this.tags = tags;
        this.image = image;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getAuthorId() {
        return authorId;
    }

    String getLink() {
        return link;
    }

    String getTags() {
        return tags;
    }

    String getImage() {
        return image;
    }

    @Override
    public @NotNull String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

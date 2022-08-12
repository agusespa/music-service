package com.challenge.musicservice.exceptions;

public class ImageUrlNotFoundException extends RuntimeException {

    public ImageUrlNotFoundException() {
        super("Image URL not found");
    }
}

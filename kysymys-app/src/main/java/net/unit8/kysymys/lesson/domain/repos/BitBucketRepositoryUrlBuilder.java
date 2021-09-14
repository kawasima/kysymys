package net.unit8.kysymys.lesson.domain.repos;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BitBucketRepositoryUrlBuilder extends AbstractRepositoryUrlBuilder {
    public BitBucketRepositoryUrlBuilder(String url) {
        super(url);
    }

    @Override
    public URL build() {
        try {
            return new URL(chopDotGitSuffix(url) + "/blob/" + branch + path);
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
}

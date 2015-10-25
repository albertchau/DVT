package com.intuit.idea.chopsticks.query.copies;

import java.io.*;

/**
 * Metadata class gets the metadata for the table Created by achau1 on 5/25/14.
 */
public class FlatFileBuilder extends QueryBuilder {

    private final String url;

    private FlatFileBuilder(Builder<?> builder) throws Exception {
        super(builder);
        if (builder.url == null)
            throw new Exception("Cannot create Flat File because there is no specified url.");
        this.url = builder.url;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    public String generateCountQuery(boolean useJanusMetadata) {
        return "THIS IS FLAT FILE, NO COUNT QUERY";
    }

    public String generateDataQuery() {
        return "THIS IS FLAT FILE, NO DATA QUERY";
    }

    public String generateExistenceQuery() {
        return "THIS IS FLAT FILE, NO EXISTENCE QUERY";
    }

    public int count() {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(this.url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            byte[] chars = new byte[1024];
            int count = 0;
            int readChars;
            boolean empty = true;
            while ((readChars = inputStream != null ? inputStream.read(chars) : 0) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (chars[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static abstract class Builder<T extends Builder<T>> extends QueryBuilder.Builder<T> {
        private String url;

        public T url(String schema) {
            this.url = schema;
            return self();
        }

        public FlatFileBuilder build() throws Exception {
            return new FlatFileBuilder(this);
        }

        public String getQuery() throws Exception {
            FlatFileBuilder query = new FlatFileBuilder(this);
            return query.generateDataQuery();
        }
    }

    private static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}

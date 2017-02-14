package net.naucu.englishxianshi.ui.account.data;

public interface ResponseListener<T> {

    void success(T result);

    void faliure(int code, String msg);
}

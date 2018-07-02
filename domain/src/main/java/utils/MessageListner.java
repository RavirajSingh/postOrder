package utils;

public interface MessageListner<T> {
    void doProcess(T message) throws Exception;
}

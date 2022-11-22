package eu.org.yexiaoye.passwordgeneration.exception;

import java.io.IOException;

/**
 * 数据为找到Exception
 */
public class NoMatchingDataException extends IOException {
    public NoMatchingDataException() {
        super();
    }
    public NoMatchingDataException(String message) {
        super(message);
    }
}

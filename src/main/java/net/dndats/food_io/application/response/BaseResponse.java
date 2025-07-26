package net.dndats.food_io.application.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public BaseResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<T>(true, "Success", data);
    }

    public static <T> BaseResponse<T> ok(T data, String message) {
        return new BaseResponse<T>(true, message, data);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<T>(false, message, null);
    }

    public static <T> BaseResponse<T> error(String message, T data) {
        return new BaseResponse<T>(false, message, data);
    }

}

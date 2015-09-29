package eu.europa.ec.fisheries.uvms.rules.rest.dto;

import java.util.Objects;

/**
 * @param <T>
 */
public class ResponseDto<T> {

    private final T data;
    private final Integer code;

    public ResponseDto(T data, ResponseCode code) {
        this.data = data;
        this.code = code.getCode();
    }

    public ResponseDto(T data, Integer code) {
        this.data = data;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.data);
        hash = 23 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResponseDto<?> other = (ResponseDto<?>) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "data=" + data + ", code=" + code + '}';
    }

}

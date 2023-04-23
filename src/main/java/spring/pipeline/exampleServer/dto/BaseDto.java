package spring.pipeline.exampleServer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@Builder
public class BaseDto implements Serializable {
    private String name;
    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BaseDto() {
    }

    public BaseDto(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "BaseDto{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}

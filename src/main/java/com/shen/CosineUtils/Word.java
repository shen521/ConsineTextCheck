package com.shen.CosineUtils;
import java.util.Objects;

public class Word implements Comparable {
    private String name;
    private String pos;
    private Float weight;
    private int frequency;

    public Word(String name, String pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return this.name;
    }

    public Float getWeight() {
        return this.weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Word other = (Word)obj;
            return Objects.equals(this.name, other.name);
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (this.name != null) {
            str.append(this.name);
        }

        if (this.pos != null) {
            str.append("/").append(this.pos);
        }

        if (this.frequency > 0) {
            str.append("/").append(this.frequency);
        }

        return str.toString();
    }

    /**
     * 对传入的对象进行比较，并初始赋权重
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        } else if (this.name == null) {
            return -1;
        } else if (o == null) {
            return 1;
        } else if (!(o instanceof Word)) {
            return 1;
        } else {
            String t = ((Word)o).getName();
            return t == null ? 1 : this.name.compareTo(t);
        }
    }
}


package com.shen.CosineUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对 数 操作具有原子性
 * 使用同步synchronized的方法实现了对一个 integer 对象的增、减、赋值（更新）操作.
 * 比如对于++运算符AtomicInteger可以将它持有的integer 能够atomic 地递增。
 * 在需要访问两个或两个以上 atomic变量的程序代码（或者是对单一的atomic变量执行两个或两个以上的操作）
 * 通常都需要被synchronize以便两者的操作能够被当作是一个atomic的单元
 */
public class AtomicFloat {
    private AtomicInteger bits;

    public AtomicFloat() {
        this(0.0F);
    }

    public AtomicFloat(float initialValue) {
        this.bits = new AtomicInteger(Float.floatToIntBits(initialValue));
    }

    public final float addAndGet(float delta) {
        float expect;
        float update;
        do {
            expect = this.get();
            update = expect + delta;
        } while(!this.compareAndSet(expect, update));

        return update;
    }

    public final boolean compareAndSet(float expect, float update) {
        return this.bits.compareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(update));
    }

    public final float get() {
        return Float.intBitsToFloat(this.bits.get());
    }

    public float floatValue() {
        return this.get();
    }

    public double doubleValue() {
        return (double)this.floatValue();
    }

    public String toString() {
        return Float.toString(this.get());
    }
}


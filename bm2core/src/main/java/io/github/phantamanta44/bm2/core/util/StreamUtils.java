package io.github.phantamanta44.bm2.core.util;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class StreamUtils {

	public static <R, T> Stream<R> streamify(T source, int length, BiFunction<T, Integer, R> mapping) {
		Object[] rArray = new Object[length];
		for (int i = 0; i < length; i++)
			rArray[i] = mapping.apply(source, i);
		return Arrays.stream(rArray).map(obj -> (R)obj);
	}
	
}

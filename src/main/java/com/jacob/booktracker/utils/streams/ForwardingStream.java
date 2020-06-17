package com.jacob.booktracker.utils.streams;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

public interface ForwardingStream<T> extends Stream<T> {

	@Override
	default Stream<T> filter(Predicate<? super T> predicate) {
		return this.getStream().filter(predicate);
	}

	@Override
	default <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
		return this.getStream().map(mapper);
	}

	@Override
	default IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return this.getStream().mapToInt(mapper);
	}

	@Override
	default LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return this.getStream().mapToLong(mapper);
	}

	@Override
	default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return this.getStream().mapToDouble(mapper);
	}

	@Override
	default <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return this.getStream().flatMap(mapper);
	}

	@Override
	default IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return this.getStream().flatMapToInt(mapper);
	}

	@Override
	default LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return this.getStream().flatMapToLong(mapper);
	}

	@Override
	default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return this.getStream().flatMapToDouble(mapper);
	}

	@Override
	default Stream<T> distinct() {
		return this.getStream().distinct();
	}

	@Override
	default Stream<T> sorted() {
		return this.getStream().sorted();
	}

	@Override
	default Stream<T> sorted(Comparator<? super T> comparator) {
		return this.getStream().sorted(comparator);
	}

	@Override
	default Stream<T> peek(Consumer<? super T> action) {
		return this.getStream().peek(action);
	}

	@Override
	default Stream<T> limit(long maxSize) {
		return this.getStream().limit(maxSize);
	}

	@Override
	default Stream<T> skip(long n) {
		return this.getStream().skip(n);
	}

	@Override
	default void forEach(Consumer<? super T> action) {
		this.getStream().forEach(action);
	}

	@Override
	default void forEachOrdered(Consumer<? super T> action) {
		this.getStream().forEachOrdered(action);
	}

	@Override
	default Object[] toArray() {
		return this.getStream().toArray();
	}

	@Override
	default <A> A[] toArray(IntFunction<A[]> generator) {
		return this.getStream().toArray(generator);
	}

	@Override
	default T reduce(T identity, BinaryOperator<T> accumulator) {
		return this.getStream().reduce(identity, accumulator);
	}

	@Override
	default Optional<T> reduce(BinaryOperator<T> accumulator) {
		return this.getStream().reduce(accumulator);
	}

	@Override
	default <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return this.getStream().reduce(identity, accumulator, combiner);
	}

	@Override
	default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return this.getStream().collect(supplier, accumulator, combiner);
	}

	@Override
	default <R, A> R collect(Collector<? super T, A, R> collector) {
		return this.getStream().collect(collector);
	}

	@Override
	default Optional<T> min(Comparator<? super T> comparator) {
		return this.getStream().min(comparator);
	}

	@Override
	default Optional<T> max(Comparator<? super T> comparator) {
		return this.getStream().max(comparator);
	}

	@Override
	default long count() {
		return this.getStream().count();
	}

	@Override
	default boolean anyMatch(Predicate<? super T> predicate) {
		return this.getStream().anyMatch(predicate);
	}

	@Override
	default boolean allMatch(Predicate<? super T> predicate) {
		return this.getStream().allMatch(predicate);
	}

	@Override
	default boolean noneMatch(Predicate<? super T> predicate) {
		return this.getStream().noneMatch(predicate);
	}

	@Override
	default Optional<T> findFirst() {
		return this.getStream().findFirst();
	}

	@Override
	default Optional<T> findAny() {
		return this.getStream().findAny();
	}

	Stream<T> getStream();

	@Override
	default Iterator<T> iterator() {
		return this.getStream().iterator();
	}

	@Override
	default Spliterator<T> spliterator() {
		return this.getStream().spliterator();
	}

	@Override
	default boolean isParallel() {
		return this.getStream().isParallel();
	}

	@Override
	default Stream<T> sequential() {
		return this.getStream().sequential();
	}

	@Override
	default Stream<T> parallel() {
		return this.getStream().parallel();
	}

	@Override
	default Stream<T> unordered() {
		return this.getStream().unordered();
	}

	@Override
	default Stream<T> onClose(Runnable closeHandler) {
		return this.getStream().onClose(closeHandler);
	}

	@Override
	default void close() {

	}
}

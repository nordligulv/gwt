package java.util.stream;

public interface BaseStream<T,S extends BaseStream<T,S>> {
  Iterator<T> iterator();

  Spliterator<T> spliterator();

  boolean isParallel();

  S sequential();

  S parallel();

  S unordered();

  S onClose(Runnable closeHandler);

  void close();
}
